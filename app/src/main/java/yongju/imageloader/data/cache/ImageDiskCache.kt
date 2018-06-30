package yongju.imageloader.data.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

class ImageDiskCache(private val dir: String): IImageCache {

    override fun saveImage(name: String, image: Bitmap) {
        image.compress(Bitmap.CompressFormat.PNG, 100, File(dir, name).outputStream())
    }

    override fun getImage(name: String): Bitmap? {
        val file = File(dir, name)
        return if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
    }
}