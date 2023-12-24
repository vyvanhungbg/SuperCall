package com.android.milo_store.base.extension

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.io.File
import java.nio.ByteBuffer


@BindingAdapter("visible")
fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}


@SuppressLint("ClickableViewAccessibility")
@BindingAdapter("touchHaptic")
fun View.touchHaptic(enabled: Boolean) {
    if(enabled){
        this.setOnTouchListener { _, event ->
            if(event.action == MotionEvent.ACTION_DOWN){
                this.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }
            false
        }
    }
}


@SuppressLint("ClickableViewAccessibility")
@BindingAdapter("setTouchListener")
fun View.setTouchListener(onTouch: OnTouchListener) {
    this.setOnTouchListener(onTouch)
}

@BindingAdapter("invisible")
fun View.invisible(isInvisible: Boolean) {
    visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("onPreventDoubleClick")
fun View.onPreventDoubleClick(onItemPress: View.OnClickListener) {
    setPreventDoubleClick {
        onItemPress.onClick(this)
    }
}

@BindingAdapter("onClickBackToolBar")
fun androidx.appcompat.widget.Toolbar.onClickBackToolBar(onItemPress: View.OnClickListener) {
    this.setNavigationOnClickListener(onItemPress)
}

@BindingAdapter("gone")
fun View.gone(isGone: Boolean) {
    visibility = if (isGone) View.GONE else View.VISIBLE
}

// Text view
@BindingAdapter("setTextOrGone")
fun TextView.setTextOrGone(text: String?) {
    this.isVisible = text.isNullOrEmpty().not()
    this.text = text
}

@BindingAdapter(
        value = ["url", "uri", "file", "drawableRes", "drawable", "bitmap", "byteArray", "byteBuffer"],
        requireAll = false
)
fun ImageView.loadImage(
        url: String? = null,
        uri: Uri? = null,
        file: File? = null,
        drawableRes: Int? = null,
        drawable: Drawable? = null,
        bitmap: Bitmap? = null,
        byteArray: ByteArray? = null,
        byteBuffer: ByteBuffer? = null,
) {
    when {
        url != null -> Glide.with(context).load(url).into(this)
        uri != null -> Glide.with(context).load(uri).into(this)
        file != null -> Glide.with(context).load(file).into(this)
        drawableRes != null -> Glide.with(context).load(drawableRes).into(this)
        drawable != null -> Glide.with(context).load(drawable).into(this)
        bitmap != null -> Glide.with(context).load(bitmap).into(this)
        byteArray != null -> Glide.with(context).load(byteArray).into(this)
        byteBuffer != null -> Glide.with(context).load(byteBuffer).into(this)
    }
}