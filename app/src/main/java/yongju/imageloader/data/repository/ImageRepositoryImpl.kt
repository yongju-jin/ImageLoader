package yongju.imageloader.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import yongju.imageloader.data.api.ImageAPI
import yongju.imageloader.data.cache.ImageDiskCache
import yongju.imageloader.data.cache.ImageMemoryCache
import yongju.imageloader.model.Image
import java.net.URL

class ImageRepositoryImpl(private val memeory: ImageMemoryCache, private val disk: ImageDiskCache): ImageRepository {
    override fun getImage(image: Image): Single<Bitmap>
        = Single.fromCallable<Bitmap> { getImageBitMap(image) }.subscribeOn(Schedulers.io())

    private fun getImageBitMap(image: Image): Bitmap
        = getImageFromMem(image.name) ?: getImageFromDisk(image.name) ?: getImageFromAPI(image) ?: throw Exception("no Image")

    private fun getImageFromMem(name: String): Bitmap? = memeory.getImage(name)

    private fun getImageFromDisk(name: String): Bitmap?
        = disk.getImage(name)?.also { storeImageMemory(name, it) }

    private fun getImageFromAPI(image: Image): Bitmap? {
        val decodeStream = BitmapFactory.decodeStream(URL(getURL(image.url)).openConnection().getInputStream())
        storeImageMemory(image.name, decodeStream)
        storeImageDisk(image.name, decodeStream)
        return decodeStream
    }

    private fun getURL(url: String) =
        "${ImageAPI.host}${if (url.startsWith("/")) url else "/$url"}"

    private fun storeImageMemory(name: String, image: Bitmap) = memeory.saveImage(name, image)

    private fun storeImageDisk(name: String, image:Bitmap) = disk.saveImage(name, image)
}