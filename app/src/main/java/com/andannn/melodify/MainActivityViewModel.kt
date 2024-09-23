package com.andannn.melodify

import androidx.lifecycle.ViewModel
import com.andannn.melodify.common.drawer.BottomSheetStateProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
    @Inject
    constructor(
        private val bottomSheetStateProvider: BottomSheetStateProvider
    ) : ViewModel() {
    val bottomSheetModel get() = bottomSheetStateProvider.bottomSheetModel
}
