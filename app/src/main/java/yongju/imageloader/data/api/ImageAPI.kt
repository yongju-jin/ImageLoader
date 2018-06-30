package yongju.imageloader.data.api

import io.reactivex.Maybe
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class ImageAPI {
    private val url = "/collections/archive/slim-aarons.aspx"

    fun getImages() = Maybe.fromCallable<Document> {
        Jsoup.connect(host+url).get()
    }.map { it.select("img")
    }

    companion object {
        const val host = "http://www.gettyimagesgallery.com"
    }
}