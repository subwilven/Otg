package com.islam.otgtask.project_base.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.islam.otgtask.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.io.File

//TODO Setup Monthly Schedule for Cleaning or Setup Cache Limit
object ImageHandler {


    fun ImageView.loadImage(url: String) {
        val creator = initPicasso(url = url)
        creator.into(this)
    }

    fun ImageView.loadImage(file: File?) {
        val creator = initPicasso(file = file)
        creator.into(this)
    }

    fun ImageView.loadImage(@DrawableRes res: Int) {
        val creator = initPicasso(res = res)
        creator.into(this)
    }

    fun loadImageFromNetwork(imageView: ImageView, url: String) {
        val creator = initPicasso(url)
        creator.into(imageView)
    }

    fun loadImageFromFile(imageView: ImageView, file: File) {
        val creator = initPicasso(file = file)
        creator.into(imageView)
    }

    private fun initPicasso(url: String = "", file: File? = null, @DrawableRes res: Int = -1): RequestCreator {

        val creator = when {
            url.isNotEmpty() -> Picasso.get().load(url)
            file != null -> Picasso.get().load(file)
            res != -1 -> Picasso.get().load(res)
            else -> Picasso.get().load(R.drawable.user)
        }

        creator.placeholder(R.drawable.user)
        creator.error(R.drawable.user)
        creator.fit()
        return creator
    }

}
