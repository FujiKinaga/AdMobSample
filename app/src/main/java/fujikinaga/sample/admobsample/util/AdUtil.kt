package fujikinaga.sample.admobsample.util

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import fujikinaga.sample.admobsample.R
import fujikinaga.sample.admobsample.ad.Ad

object AdUtil {

    fun getIncrementedIndex(targetIndex: Int, adListSize: Int): Int {
        var returnNextIndex = targetIndex + 1
        if (returnNextIndex >= adListSize) {
            // 配列のサイズを超えたら0に戻す
            returnNextIndex = 0
        }
        return returnNextIndex
    }

    fun getNextLoadAdIndex(nextLoadAdOffset: Int, targetIndex: Int, adListSize: Int): Int {
        var returnNextIndex = targetIndex
        for (i in 0 until nextLoadAdOffset) {
            returnNextIndex = getIncrementedIndex(returnNextIndex, adListSize)
        }
        return returnNextIndex
    }

    fun getValidAdView(adList: List<Ad>, shouldReturnAdIndex: Int, adListSize: Int): Ad? {
        val targetAd = adList[shouldReturnAdIndex]
        if (!targetAd.isOnError()) {
            // 白板でなければそのまま利用する
            return targetAd
        }
        var nextAdView: Ad? = null
        var incrementedIndex = getIncrementedIndex(shouldReturnAdIndex, adListSize)
        // adViewListを一周して有効な広告がないか探してくる
        while (incrementedIndex != shouldReturnAdIndex) {
            val adView = adList[incrementedIndex]
            val isLoadedAd = !adView.isLoading() && !adView.isOnError()
            if (isLoadedAd) {
                nextAdView = adView
                break
            }
            incrementedIndex = getIncrementedIndex(incrementedIndex, adListSize)
        }
        return nextAdView
    }

    fun bindAdView(container: FrameLayout, view: View?) {
        if (container.childCount > 0 && (container.getChildAt(0) === view)) {
            return
        }
        unbindAdView(container)

        view?.let {
            (it.parent as? ViewGroup)?.removeView(it)
            container.addView(it)
        }
    }

    fun unbindAdView(container: FrameLayout) {
        if (container.childCount > 0) {
            container.removeAllViews()
        }
    }

    fun loadAdForPrefetch(ad: Ad?) {
        ad ?: return
        if (ad.isLoading()) {
            // 読み込み中ならスキップ
            return
        }
        ad.load()
    }

    fun populateNativeAdView(
        nativeAd: UnifiedNativeAd?,
        adView: UnifiedNativeAdView
    ) {
        nativeAd ?: return

        // Set other ad assets.
        val mediaView: MediaView = adView.findViewById(R.id.ad_media)
        mediaView.setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View, child: View) {
                if (child is ImageView) {
                    child.adjustViewBounds = true
                }
            }

            override fun onChildViewRemoved(parent: View, child: View) {}
        })

        adView.mediaView = mediaView
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.adChoicesView = adView.findViewById(R.id.ad_choice_info)

        // The headline is guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as? TextView)?.text = nativeAd.headline
        (adView.bodyView as? TextView)?.text = nativeAd.body

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.INVISIBLE
        } else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd)
    }
}