package yongju.imageloader.view.presenter

interface MainPresenter {
    interface View {
        fun showErrorDialog()
        fun hideProgressBar()
    }
}