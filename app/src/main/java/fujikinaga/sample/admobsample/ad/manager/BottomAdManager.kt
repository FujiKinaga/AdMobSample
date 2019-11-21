package fujikinaga.sample.admobsample.ad.manager

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.ads.AdSize
import fujikinaga.sample.admobsample.R
import fujikinaga.sample.admobsample.ad.Ad
import fujikinaga.sample.admobsample.ad.adm.BannerAd
import timber.log.Timber

class BottomAdManager : LifecycleObserver {

    private var bottomAd: Ad? = null

    private var isInitialized = false

    val ad: Ad?
        get() {
            if (!isInitialized) {
                return null
            }
            return bottomAd
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Timber.d("onResume")
        if (!isInitialized) {
            return
        }
        bottomAd?.resume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Timber.d("onPause")
        if (!isInitialized) {
            return
        }
        bottomAd?.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Timber.d("onDestroy")
        destroyAdView()
    }

    fun destroyAdView() {
        if (!isInitialized) {
            return
        }
        isInitialized = false
        bottomAd?.destroy()
        bottomAd = null
    }

    fun updateAdView(context: Context) {
        if (isInitialized) {
            return
        }
        initAdView(context)
    }

    private fun initAdView(context: Context) {
        bottomAd = BannerAd(context, R.string.adm_ad_unit_banner_id_dummy, AdSize.BANNER)

        isInitialized = true
    }
}
