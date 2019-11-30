package fujikinaga.sample.admobsample.ad.manager

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.ads.AdSize
import fujikinaga.sample.admobsample.R
import fujikinaga.sample.admobsample.ad.Ad
import fujikinaga.sample.admobsample.ad.impl.BannerAd
import fujikinaga.sample.admobsample.ad.impl.NativeAd
import fujikinaga.sample.admobsample.util.AdUtil
import timber.log.Timber

private const val IN_FEED_AD_LIST_SIZE = 3
private const val AD_INITIAL_LOAD_LIMIT_SIZE = 2
private const val AD_NEXT_LOAD_INDEX_OFFSET = 2
private const val AD_INVALID_INDEX = -1

class InFeedAdManager : LifecycleObserver {

    private var inFeedAdList: MutableList<Ad>? = null

    private var shouldReturnInFeedAdIndex = AD_INVALID_INDEX

    private var isInitialized = false

    private var _isShowNativeAd = false
    val isShowNativeAd get() = _isShowNativeAd

    val getAndFetchAd: Ad?
        get() {
            if (!isInitialized) {
                return null
            }
            inFeedAdList ?: return null
            shouldReturnInFeedAdIndex =
                AdUtil.getIncrementedIndex(shouldReturnInFeedAdIndex, IN_FEED_AD_LIST_SIZE)
            val shouldLoadAdIndexForPrefetch = AdUtil.getNextLoadAdIndex(
                AD_NEXT_LOAD_INDEX_OFFSET,
                shouldReturnInFeedAdIndex,
                IN_FEED_AD_LIST_SIZE
            )
            loadAdForPrefetch(shouldLoadAdIndexForPrefetch)

            return AdUtil.getValidAdView(
                inFeedAdList!!,
                shouldReturnInFeedAdIndex,
                IN_FEED_AD_LIST_SIZE
            )
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Timber.d("onResume")

        if (!isInitialized) {
            return
        }
        inFeedAdList?.also {
            for (adView in it) {
                adView.resume()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Timber.d("onPause")

        if (!isInitialized) {
            return
        }
        inFeedAdList?.also {
            for (adView in it) {
                adView.pause()
            }
        }
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

        inFeedAdList?.also {
            for (adView in it) {
                adView.destroy()
            }
            it.clear()
            inFeedAdList = null
        }
    }

    fun updateAdViewLists(context: Context) {
        if (isInitialized) {
            return
        }
        initInFeedAdViewList(context)
    }

    private fun initInFeedAdViewList(context: Context) {
        Timber.d("initAdViewLists")

        inFeedAdList = ArrayList(IN_FEED_AD_LIST_SIZE)
        inFeedAdList?.also {
            for (i in 0 until IN_FEED_AD_LIST_SIZE) {
                val adView =
                    if (isShowNativeAd) {
                        NativeAd(context, R.string.adm_ad_unit_native_id_dummy)
                    } else {
                        BannerAd(context, R.string.adm_ad_unit_banner_id_dummy, AdSize.MEDIUM_RECTANGLE)
                    }
                it.add(adView)
            }

            isInitialized = true

            startInitialLoad()
        }
    }

    private fun startInitialLoad() {
        loadAdForInitialize(0)
    }

    private fun loadAdForInitialize(adLoadCount: Int) {
        if (adLoadCount >= AD_INITIAL_LOAD_LIMIT_SIZE) {
            // 初回は広告Viewが真っ白になる状態を防ぐため、3広告分を先に読み込むようにする
            return
        }
        if (!isInitialized) {
            return
        }
        inFeedAdList?.also {
            if (it.isEmpty()) {
                return
            }
            val adView = it[adLoadCount]
            val callback = object : Ad.Callback {
                override fun onSuccess(view: View?) {
                    Timber.d("success loadCount: $adLoadCount")
                    adView.setCallback(null)
                    loadAdForInitialize(adLoadCount + 1)
                }

                override fun onFailure(errorCode: Int) {
                    Timber.e("failure loadCount: $adLoadCount, errorCode: $errorCode")
                    adView.setCallback(null)
                    loadAdForInitialize(adLoadCount + 1)
                }
            }
            adView.setCallback(callback)
            adView.load()
        }
    }

    private fun loadAdForPrefetch(targetIndex: Int) {
        inFeedAdList?.also {
            if (it.isEmpty()) {
                return
            }
            AdUtil.loadAdForPrefetch(it[targetIndex])
        }
    }
}