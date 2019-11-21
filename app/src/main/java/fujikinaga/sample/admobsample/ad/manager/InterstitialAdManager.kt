package fujikinaga.sample.admobsample.ad.manager

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import fujikinaga.sample.admobsample.R
import timber.log.Timber

class InterstitialAdManager : LifecycleObserver {

    private var interstitialAd: InterstitialAd? = null

    private var isInitialized = false

    val ad: InterstitialAd?
        get() {
            if (!isInitialized) {
                return null
            }
            return interstitialAd
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Timber.d("onDestroy")
        destroyAdViews()
    }

    private fun destroyAdViews() {
        if (!isInitialized) {
            return
        }
        isInitialized = false

        interstitialAd = null
    }

    fun updateAdView(context: Context) {
        if (isInitialized) {
            return
        }
        initAdView(context)
        load()
    }

    fun load() {
        interstitialAd?.loadAd(AdRequest.Builder().build())
    }

    private fun initAdView(context: Context) {
        Timber.d("initAdViewLists")

        initInterstitialAdView(context)

        isInitialized = true

        load()
    }

    private fun initInterstitialAdView(context: Context) {
        interstitialAd = InterstitialAd(context).also {
            it.adUnitId = context.getString(R.string.adm_ad_unit_interstitial_id_dummy)
        }
    }
}