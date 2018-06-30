package yongju.imageloader.data.repository

import android.graphics.Bitmap
import io.reactivex.Single
import yongju.imageloader.model.Image

interface ImageRepository {
    fun getImage(image: Image): Single<Bitmap>
}