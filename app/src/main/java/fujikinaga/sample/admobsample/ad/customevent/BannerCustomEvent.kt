package fujikinaga.sample.admobsample.ad.customevent

import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.mediation.MediationAdRequest
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener
import fujikinaga.sample.admobsample.ad.Ad
import fujikinaga.sample.admobsample.ad.adm.BannerAd

class BannerCustomEvent : CustomEventBanner {

    private var ad: Ad? = null

    override fun requestBannerAd(
        context: Context, customEventBannerListener: CustomEventBannerListener,
        serverParameter: String, adSize: AdSize, mediationAdRequest: MediationAdRequest,
        customEventExtras: Bundle?
    ) {

        val unitIdRes = customEventExtras?.getInt("unitIdRes") ?: return

        ad = BannerAd(context, unitIdRes, adSize)
        ad?.also {
            it.setCallback(object : Ad.Callback {
                override fun onSuccess(view: View?) {
                    customEventBannerListener.onAdLoaded(view)
                }

                override fun onFailure(errorCode: Int) {
                    customEventBannerListener.onAdFailedToLoad(errorCode)
                }
            })
            it.load()
        }
    }

    override fun onResume() {
        ad?.resume()
    }

    override fun onPause() {
        ad?.pause()
    }

    override fun onDestroy() {
        ad = null
    }
}