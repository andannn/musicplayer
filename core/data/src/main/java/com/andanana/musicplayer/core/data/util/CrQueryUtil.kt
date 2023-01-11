package com.andanana.musicplayer.core.data.util

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle

/**
 * Query parameter for ContentResolver.
 */
class CrQueryParameter(
    var projection: Array<String>? = null,
    var where: String? = null,
    var selectionArgs: Array<String>? = null,
    var sortOrder: String? = null,
    var limit: Int = 0,
    var offset: Int = 0
) {
    override fun toString(): String {
        val builder = StringBuilder()
        projection?.let {
            builder.append("project:[")
            it.forEach { s -> builder.append("$s,") }
            builder.append("] ")
        }
        where?.let { builder.append("where:$it ") }
        selectionArgs?.let {
            builder.append("selectionarg:[")
            it.forEach { s -> builder.append("$s,") }
            builder.append("] ")
        }
        builder.append("] ")
        sortOrder?.let { builder.append("sort:$it ") }
        builder.append("limit:$limit ")
        builder.append("offset:$offset ")
        return builder.toString()
    }
}

/**
 * Util class for content resolver query.
 */
object CrQueryUtil {

    /**
     * Query from ContentResolver by giving uir and query parameter.
     *
     * @param context context
     * @param uri uri for query
     * @param params query parameters
     * @return cursor
     */
    fun query(context: Context, uri: Uri, params: CrQueryParameter): Cursor? {
        var result: Cursor? = null
        try {
            val queryArgs = Bundle()
            queryArgs.putCharSequence(ContentResolver.QUERY_ARG_SQL_SELECTION, params.where)
            queryArgs.putCharSequenceArray(
                ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS,
                params.selectionArgs
            )
            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER, params.sortOrder)
            if (params.limit > 0) {
                queryArgs.putInt(ContentResolver.QUERY_ARG_LIMIT, params.limit)
                queryArgs.putInt(ContentResolver.QUERY_ARG_OFFSET, params.offset)
            }
            result = context.contentResolver.query(uri, params.projection, queryArgs, null)
        } catch (e: RuntimeException) {
        }
        return result
    }
}
