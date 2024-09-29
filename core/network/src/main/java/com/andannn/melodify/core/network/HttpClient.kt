package com.andannn.melodify.core.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val TAG = "HttpClient"

internal val lrclibResourceClientBuilder: () -> HttpClient = {
    HttpClient(OkHttp) {
        commonConfig()
        defaultRequest {
            url("https://lrclib.net/")
        }
    }
}

private fun HttpClientConfig<OkHttpConfig>.commonConfig() {
    expectSuccess = true

    install(Logging) {
        logger =
            object : Logger {
                override fun log(message: String) {
                    Timber.tag(TAG).d("HttpLogInfo: $message")
                }
            }
        level = LogLevel.BODY
        sanitizeHeader { header -> header == HttpHeaders.Authorization }
    }
    install(Resources)
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
            },
        )
    }

    engine {
        config {
            connectTimeout(10, TimeUnit.SECONDS)
        }
    }
}