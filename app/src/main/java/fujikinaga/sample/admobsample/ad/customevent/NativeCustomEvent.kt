package fujikinaga.sample.admobsample.ad.customevent

import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.mediation.NativeMediationAdRequest
import com.google.android.gms.ads.mediation.customevent.CustomEventNative
import com.google.android.gms.ads.mediation.customevent.CustomEventNativeListener
import fujikinaga.sample.admobsample.ad.Ad
import fujikinaga.sample.admobsample.ad.adm.NativeAd

class NativeCustomEvent : CustomEventNative {

    private var ad: Ad? = null

    override fun requestNativeAd(
        context: Context,
        customEventNativeListener: CustomEventNativeListener,
        serverParameter: String,
        nativeMediationAdRequest: NativeMediationAdRequest,
        customEventExtras: Bundle?
    ) {
        val unitIdRes = customEventExtras?.getInt("unitIdRes") ?: return

        ad = NativeAd(context, unitIdRes)
        ad?.also {
            it.setCallback(object : Ad.Callback {
                override fun onSuccess(view: View?) {
                    it.getNativeAd()?.value?.let {
                        customEventNativeListener.onAdLoaded(NativeCustomEventAdMapper(it))
                    }
                }

                override fun onFailure(errorCode: Int) {
                    customEventNativeListener.onAdFailedToLoad(errorCode)
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