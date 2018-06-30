package yongju.imageloader.data.cache

import android.graphics.Bitmap
import android.support.v4.util.LruCache

class ImageMemoryCache: IImageCache {

    private val memCache: LruCache<String, Bitmap> by lazy {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        // Use 1/8th of the available memory for this memory cache.
        val cacheSize = maxMemory / 8

        object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String?, bitmap: Bitmap?): Int {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap!!.byteCount / 1024
            }
        }
    }

    override fun saveImage(name: String, image: Bitmap) {
        if (getImage(name) == null) memCache.put(name, image)
    }

    override fun getImage(name: String): Bitmap? = memCache[name]
}