package fujikinaga.sample.admobsample.ad.customevent

import android.content.Context
import android.os.Bundle
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.mediation.NativeMediationAdRequest
import com.google.android.gms.ads.mediation.customevent.CustomEventNative
import com.google.android.gms.ads.mediation.customevent.CustomEventNativeListener

class NativeCustomEvent : CustomEventNative {

    override fun requestNativeAd(
        context: Context,
        customEventNativeListener: CustomEventNativeListener,
        serverParameter: String,
        nativeMediationAdRequest: NativeMediationAdRequest,
        customEventExtras: Bundle?
    ) {
        val unitIdRes = customEventExtras?.getInt("unitIdRes") ?: return

        val builder = AdLoader.Builder(context, context.getString(unitIdRes))

        val adRequest = AdRequest.Builder().build()

        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .setCustomControlsRequested(true)
            .build()

        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .setMediaAspectRatio(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_LANDSCAPE)
            .build()

        builder
            .forUnifiedNativeAd { nativeAd ->
                customEventNativeListener.onAdLoaded(NativeCustomEventAdMapper(nativeAd))
            }
            .withAdListener(object : AdListener() {
                override fun onAdImpression() {
                    customEventNativeListener.onAdImpression()
                }

                override fun onAdLeftApplication() {
                    customEventNativeListener.onAdLeftApplication()
                }

                override fun onAdClicked() {
                    customEventNativeListener.onAdClicked()
                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    customEventNativeListener.onAdFailedToLoad(errorCode)
                }

                override fun onAdClosed() {
                    customEventNativeListener.onAdClosed()
                }

                override fun onAdOpened() {
                    customEventNativeListener.onAdOpened()
                }
            })
            .withNativeAdOptions(adOptions)
            .build().also {
                it.loadAd(adRequest)
            }
    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onDestroy() {

    }
}