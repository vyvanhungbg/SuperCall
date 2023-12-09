package com.android.milo_store.base.config

import android.view.View
import androidx.annotation.DrawableRes
import com.android.milo_store.base.R

data class ToolbarConfig(
    val title: String,
    val onBackClick: View.OnClickListener,
    @DrawableRes val iconBack:Int = R.drawable.icon_back_black_24,
)