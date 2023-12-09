package com.android.milo_store.base.config

import android.view.View
import androidx.annotation.DrawableRes
import com.android.milo_store.base.R


class HeaderConfig(
    val nameHeader: String,
    val actionBack: View.OnClickListener,
    @DrawableRes val iconBack:   Int = R.drawable.icon_back_black_24,
    @DrawableRes val iconRightHeader:  Int? = null,
    val actionIconRightHeader: View.OnClickListener? = null
)