package yongju.imageloader.viewmodel

import android.arch.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import yongju.imageloader.data.api.ImageAPI
import yongju.imageloader.model.Image

class ImageViewModel: ViewModel() {
    private val imageAPI = ImageAPI()
    private var imageResp: List<Image> = emptyList()

    fun getImages(): Single<List<Image>> = Single.fromCallable<List<Image>>{
        imageResp
        }.filter { it.isNotEmpty() }.concatWith(
                imageAPI.getImages()
                        .subscribeOn(Schedulers.io())
                        .map {
                            it.map {
                                val src = it.attr("src")
                            Image(getFileName(src), src)
                            }
                        }.doOnSuccess {
                            imageResp = it
                        }
        ).firstOrError()


    private fun getFileName(url: String) = url.substring(url.lastIndexOf('/') + 1, url.length)
}