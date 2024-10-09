package com.andannn.melodify.feature.customtab.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.andannn.melodify.feature.customtab.CustomTabSettingScreen

const val CUSTOM_TAB_SETTING_ROUTE = "custom_tab_setting_route"

fun NavController.navigateToCustomTabSetting() {
    this.navigate(CUSTOM_TAB_SETTING_ROUTE)
}

fun NavGraphBuilder.customTabSetting(onBackPressed: () -> Unit) {
    composable(
        route = CUSTOM_TAB_SETTING_ROUTE,
    ) {
        CustomTabSettingScreen(onBackPressed = onBackPressed)
    }
}
