package com.andannn.melodify

import androidx.lifecycle.ViewModel
import com.andannn.melodify.common.drawer.BottomSheetController
import com.andannn.melodify.common.drawer.BottomSheetStateProvider
import com.andannn.melodify.common.drawer.DeleteMediaItemEventProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject
constructor(
    private val controller: BottomSheetController,
) : BottomSheetStateProvider by controller, DeleteMediaItemEventProvider by controller, ViewModel()
