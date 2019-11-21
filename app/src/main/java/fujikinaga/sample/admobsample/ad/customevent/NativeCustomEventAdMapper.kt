package fujikinaga.sample.admobsample.ad.customevent

import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.mediation.UnifiedNativeAdMapper

class NativeCustomEventAdMapper(private val nativeAd: UnifiedNativeAd) : UnifiedNativeAdMapper() {

    init {
        // ネイティブ広告の内容をDFPにマッピングします。
        advertiser = nativeAd.advertiser
        body = nativeAd.body
        callToAction = nativeAd.callToAction
        headline = nativeAd.headline

        // 動画広告を表示する場合はtrueにします。
        setHasVideoContent(true)

        overrideImpressionRecording = true
        overrideClickHandling = false
    }

    override fun handleClick(view: View) {
        nativeAd.performClick(Bundle())
    }
}