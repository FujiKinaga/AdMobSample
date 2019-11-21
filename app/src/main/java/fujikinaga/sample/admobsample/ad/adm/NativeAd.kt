package fujikinaga.sample.admobsample.ad.adm

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import fujikinaga.sample.admobsample.ad.Ad
import fujikinaga.sample.admobsample.ad.customevent.NativeCustomEvent

class NativeAd constructor(context: Context, @StringRes unitIdRes: Int) : Ad {

    private val builder = AdLoader.Builder(context, context.getString(unitIdRes))

    private val adRequest = AdRequest.Builder()
        .addCustomEventExtrasBundle(NativeCustomEvent::class.java, Bundle().also { bundle ->
            bundle.putInt("unitIdRes", unitIdRes)
        })
        .build()

    private val videoOptions = VideoOptions.Builder()
        .setStartMuted(true)
        .setCustomControlsRequested(true)
        .build()

    private val adOptions = NativeAdOptions.Builder()
        .setVideoOptions(videoOptions)
        .setMediaAspectRatio(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_LANDSCAPE)
        .build()

    private val adLoader = builder.forUnifiedNativeAd { nativeAd ->
        isOnError = false

        _nativeAd.value = nativeAd
        callback?.onSuccess(null)

    }.withAdListener(object : AdListener() {
        override fun onAdFailedToLoad(errorCode: Int) {
            isOnError = true

            _nativeAd.value = null
            callback?.onFailure(errorCode)
        }
    }).withNativeAdOptions(adOptions).build()

    private val _nativeAd = MutableLiveData<UnifiedNativeAd>()

    private var callback: Ad.Callback? = null

    private var isOnError: Boolean = false

    override fun isLoading(): Boolean {
        return adLoader.isLoading
    }

    override fun isOnError(): Boolean {
        return isOnError
    }

    override fun getView(): LiveData<View>? {
        return null
    }

    override fun getNativeAd(): LiveData<UnifiedNativeAd>? {
        return _nativeAd
    }

    override fun resume() {

    }

    override fun pause() {

    }

    override fun destroy() {
        callback = null

        _nativeAd.value?.destroy()
        _nativeAd.value = null
    }

    override fun setCallback(callback: Ad.Callback?) {
        this.callback = callback
    }

    override fun load() {
        destroy()

        isOnError = false

        adLoader.loadAd(adRequest)
    }
}