package yongju.imageloader.view.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import yongju.imageloader.R
import yongju.imageloader.view.adapter.ImageAdapter
import yongju.imageloader.view.presenter.MainPresenter
import yongju.imageloader.viewmodel.ImageViewModel

class MainActivity : AppCompatActivity(), MainPresenter.View {
    private val disposables by lazy {
        CompositeDisposable()
    }

    private val imageViewModel by lazy {
        ViewModelProviders.of(this).get(ImageViewModel::class.java)
    }

    private val imageAdapter by lazy {
        ImageAdapter(applicationContext, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageRecyclerView.apply {
            adapter = imageAdapter
            itemAnimator = DefaultItemAnimator().apply {
                supportsChangeAnimations = false
            }
        }

        progressBar.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        disposables.add(imageAdapter.bind(imageViewModel.getImages()))
    }

    override fun onStop() {
        disposables.clear()
        super.onStop()
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun showErrorDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("onError")
            setMessage("Error")
            setPositiveButton(android.R.string.ok, {
                _, _ -> finish()
            })
        }.create().show()
    }
}