package com.milo.store.utils.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.milo.store.R
import java.io.File
import java.nio.ByteBuffer


/**
- Create by :Vy Hùng
- Create at :02,January,2024
 **/

@BindingAdapter("loadImageWithDefault")
fun ImageView.loadImageWithDefault(
    url: String? = null,
) {
    val array = listOf(
        R.drawable.avatar_1,
        R.drawable.avatar_2,
        R.drawable.avatar_3,
        R.drawable.avatar_4,
        R.drawable.avatar_5,
        R.drawable.avatar_6,
        R.drawable.avatar_7,
        R.drawable.avatar_8,
        R.drawable.avatar_9,
        R.drawable.avatar_10,
    )

    fun provideAvatarColor(name: String?): Int {

        return try {
            val code = name?.lowercase()?.first()?.code

//            if (code != null) array[code % array.size] else array.random()
            array.random()
        } catch (e: Exception) {
            array.first()
        }
    }

    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: com.bumptech.glide.request.target.Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {

            val textViewName =
                (this@loadImageWithDefault.parent as? View)?.findViewById<TextView>(R.id.image_view_first_name)

            this@loadImageWithDefault.background =
                ContextCompat.getDrawable(
                    context,
                    provideAvatarColor(textViewName?.text.toString())
                )
            textViewName?.isVisible = true
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: com.bumptech.glide.request.target.Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            (this@loadImageWithDefault.parent as? View)?.findViewById<TextView>(R.id.image_view_first_name)?.isVisible =
                false
            return false
        }
    }
    Glide.with(context).load(url).listener(listener).into(this)

}