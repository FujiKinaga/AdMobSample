package fujikinaga.sample.admobsample.ad.impl

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.formats.UnifiedNativeAd
import fujikinaga.sample.admobsample.ad.Ad

class TestAd : Ad {

    private val adView: View? = null

    private val _view = MutableLiveData<View>(adView)

    private var callback: Ad.Callback? = null

    private var isOnError: Boolean = false

    override fun isLoading(): Boolean {
        return false
    }

    override fun isOnError(): Boolean {
        return isOnError
    }

    override fun getView(): LiveData<View>? {
        return _view
    }

    override fun getNativeAd(): LiveData<UnifiedNativeAd>? {
        return null
    }

    override fun resume() {

    }

    override fun pause() {

    }

    override fun destroy() {
        callback = null
    }

    override fun setCallback(callback: Ad.Callback?) {
        this.callback = callback
    }

    override fun load() {
        isOnError = false
    }
}