package yongju.imageloader.view.adapter

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.image.view.*
import yongju.imageloader.R
import yongju.imageloader.data.cache.ImageDiskCache
import yongju.imageloader.data.cache.ImageMemoryCache
import yongju.imageloader.data.repository.ImageRepository
import yongju.imageloader.data.repository.ImageRepositoryImpl
import yongju.imageloader.model.Image
import yongju.imageloader.view.presenter.MainPresenter

class ImageAdapter(private val context: Context, private val view: MainPresenter.View): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private var images: List<Image> = emptyList()

    private val imageRepository: ImageRepository by lazy {
        ImageRepositoryImpl(ImageMemoryCache(), ImageDiskCache(context.filesDir.absolutePath))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageViewHolder(context, parent)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindView(android.R.drawable.ic_menu_report_image)
        imageRepository.getImage(images[position]).observeOn(AndroidSchedulers.mainThread()).subscribe(holder::bindView, Throwable::printStackTrace)
    }

    override fun getItemCount() = images.size

    fun bind(source: Single<List<Image>>): Disposable {
        data class DataWithDiff(val data: List<Image>, val diff: DiffUtil.DiffResult?)

        return source.map {
            DataWithDiff(it, DiffUtil.calculateDiff(DiffCallback(images, it)))
        }.observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            images = it.data
            if (it.diff != null) it.diff.dispatchUpdatesTo(this@ImageAdapter)
            else notifyDataSetChanged()
            view.hideProgressBar()
        }, {
            it.printStackTrace()
            view.hideProgressBar()
            view.showErrorDialog()
        })
    }

    private class DiffCallback(private val old: List<Image>, private val new:List<Image>): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = old[oldItemPosition] == new[newItemPosition]
        override fun getOldListSize() = old.size
        override fun getNewListSize() = new.size
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = old[oldItemPosition] == new[newItemPosition]
    }

    inner class ImageViewHolder(context: Context, parent: ViewGroup?):
            RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.image, parent, false)) {
        val imageView = itemView.imageView
        fun bindView(res: Int) = imageView.setImageResource(res)
        fun bindView(bmp: Bitmap) = imageView.setImageBitmap(bmp)
    }
}