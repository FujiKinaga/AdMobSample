package fujikinaga.sample.admobsample.ad.impl

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import fujikinaga.sample.admobsample.ad.Ad
import fujikinaga.sample.admobsample.ad.customevent.BannerCustomEvent

class BannerAd constructor(context: Context, @StringRes val unitIdRes: Int, adSize: AdSize) : Ad {

    private val adView: AdView = AdView(context).also {
        it.adSize = adSize
        it.adUnitId = context.getString(unitIdRes)
        it.adListener = object : AdListener() {
            override fun onAdLoaded() {
                isOnError = false

                _view.value = it
                callback?.onSuccess(it)
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                isOnError = true

                _view.value = null
                callback?.onFailure(errorCode)
            }
        }
    }
    private val _view = MutableLiveData<View>(adView)

    private var callback: Ad.Callback? = null

    private var isOnError: Boolean = false

    override fun isLoading(): Boolean {
        return adView.isLoading
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
        adView.resume()
    }

    override fun pause() {
        adView.pause()
    }

    override fun destroy() {
        callback = null

        adView.destroy()
    }

    override fun setCallback(callback: Ad.Callback?) {
        this.callback = callback
    }

    override fun load() {
        isOnError = false

        val builder = AdRequest.Builder()
            .addCustomEventExtrasBundle(BannerCustomEvent::class.java, Bundle().also { bundle ->
                bundle.putInt("unitIdRes", unitIdRes)
            })

        adView.loadAd(builder.build())
    }
}