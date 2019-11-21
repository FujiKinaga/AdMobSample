package fujikinaga.sample.admobsample.ad

import android.view.View
import androidx.lifecycle.LiveData
import com.google.android.gms.ads.formats.UnifiedNativeAd

interface Ad {

    fun isLoading(): Boolean

    fun isOnError(): Boolean

    fun getView(): LiveData<View>?

    fun getNativeAd(): LiveData<UnifiedNativeAd>?

    fun resume()

    fun pause()

    fun destroy()

    fun setCallback(callback: Callback?)

    fun load()

    interface Callback {

        fun onSuccess(view: View?)

        fun onFailure(errorCode: Int)
    }
}