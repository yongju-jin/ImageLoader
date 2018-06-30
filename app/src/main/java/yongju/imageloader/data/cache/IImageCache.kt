package yongju.imageloader.data.cache

import android.graphics.Bitmap

interface IImageCache {
    fun saveImage(name: String, image: Bitmap)
    fun getImage(name: String): Bitmap?
}