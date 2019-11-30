package fujikinaga.sample.admobsample.ad.customevent

import android.content.Context
import android.os.Bundle
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.mediation.MediationAdRequest
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener

class BannerCustomEvent : CustomEventBanner {

    private var adView: AdView? = null

    override fun requestBannerAd(
        context: Context, customEventBannerListener: CustomEventBannerListener,
        serverParameter: String, adSize: AdSize, mediationAdRequest: MediationAdRequest,
        customEventExtras: Bundle?
    ) {

        val unitIdRes = customEventExtras?.getInt("unitIdRes") ?: return

        adView = AdView(context).also {
            it.adSize = adSize
            it.adUnitId = context.getString(unitIdRes)
            it.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    customEventBannerListener.onAdLoaded(it)
                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    customEventBannerListener.onAdFailedToLoad(errorCode)
                }

                override fun onAdLeftApplication() {
                    customEventBannerListener.onAdLeftApplication()
                }

                override fun onAdClicked() {
                    customEventBannerListener.onAdClicked()
                }

                override fun onAdClosed() {
                    customEventBannerListener.onAdClosed()
                }

                override fun onAdOpened() {
                    customEventBannerListener.onAdOpened()
                }
            }
        }.also {
            val adRequest = AdRequest.Builder().build()
            it.loadAd(adRequest)
        }
    }

    override fun onResume() {
        adView?.resume()
    }

    override fun onPause() {
        adView?.pause()
    }

    override fun onDestroy() {
        adView?.destroy()
        adView = null
    }
}