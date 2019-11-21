package fujikinaga.sample.admobsample.ui.main.item

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.GroupieViewHolder
import fujikinaga.sample.admobsample.R
import fujikinaga.sample.admobsample.ad.Ad
import fujikinaga.sample.admobsample.databinding.ItemNativeAdBinding
import fujikinaga.sample.admobsample.ui.main.OnFeedActionListener
import fujikinaga.sample.admobsample.util.AdUtil
import fujikinaga.sample.admobsample.util.EqualableContentsProvider

class NativeAdItem(
    private val index: Int,
    private val listener: OnFeedActionListener,
    private val lifecycleOwner: LifecycleOwner
) : BindableItem<ItemNativeAdBinding>(
    index.toLong()
), EqualableContentsProvider {

    private var ad: Ad? = null
    private var binding: ItemNativeAdBinding? = null

    override fun createViewHolder(itemView: View): GroupieViewHolder<ItemNativeAdBinding> {
        return super.createViewHolder(itemView).also {
            it.binding.lifecycleOwner = lifecycleOwner
        }
    }

    override fun bind(binding: ItemNativeAdBinding, position: Int) {
        this.binding = binding
        binding.also {
            ad = listener.getAd()

            it.lifecycleOwner?.let { lifecycleOwner ->
                ad?.getNativeAd()?.observe(lifecycleOwner, nativeAdViewObserver)
            }
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemNativeAdBinding>) {
        binding = null
        ad?.getNativeAd()?.removeObserver(nativeAdViewObserver)
        ad = null
        super.unbind(viewHolder)
    }

    private val nativeAdViewObserver = Observer<UnifiedNativeAd> { nativeAd: UnifiedNativeAd? ->
        binding?.also {
            AdUtil.populateNativeAdView(nativeAd, it.adView)
            it.executePendingBindings()
        }
    }

    override fun providerEqualableContents(): Array<*> =
        arrayOf(index.toLong())

    override fun equals(other: Any?): Boolean = isSameContents(other)

    override fun hashCode(): Int = contentsHash()

    override fun getLayout(): Int = R.layout.item_native_ad
}