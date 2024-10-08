package com.andannn.melodify.core.datastore.model

data class UserSettingPref(

    /**
     * play mode of player
     *
     * Key: [PreferencesKeyName.PLAY_MODE_KEY_NAME]
     * Values: [PlatModeValues]
     */
    val playMode: Int,

    /**
     * shuffle mode of player
     *
     * Key: [PreferencesKeyName.IS_SHUFFLE_KEY_NAME]
     * Values: [Boolean]
     */
    val isShuffle: Boolean,

    /**
     * media preview mode of player
     *
     * Key: [PreferencesKeyName.MEDIA_PREVIEW_MODE_KEY_NAME]
     * Values: [PreviewModeValues]
     */
    val mediaPreviewMode: Int,

    /**
     * custom tabs
     *
     * Key: [PreferencesKeyName.CUSTOM_TABS_KEY_NAME]
     * Values: [String]
     */
    val customTabs: String?
)