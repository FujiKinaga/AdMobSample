package fujikinaga.sample.admobsample.ui.main.item

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.GroupieViewHolder
import fujikinaga.sample.admobsample.R
import fujikinaga.sample.admobsample.ad.Ad
import fujikinaga.sample.admobsample.databinding.ItemBannerAdBinding
import fujikinaga.sample.admobsample.ui.main.OnFeedActionListener
import fujikinaga.sample.admobsample.util.AdUtil
import fujikinaga.sample.admobsample.util.EqualableContentsProvider

class BannerAdItem(
    private val index: Int,
    private val listener: OnFeedActionListener,
    private val lifecycleOwner: LifecycleOwner
) : BindableItem<ItemBannerAdBinding>(
    index.toLong()
), EqualableContentsProvider {

    private var ad: Ad? = null
    private var binding: ItemBannerAdBinding? = null

    override fun createViewHolder(itemView: View): GroupieViewHolder<ItemBannerAdBinding> {
        return super.createViewHolder(itemView).also {
            it.binding.lifecycleOwner = lifecycleOwner
        }
    }

    override fun bind(binding: ItemBannerAdBinding, position: Int) {
        this.binding = binding
        binding.also {
            ad = listener.getAd()

            it.inFeedContainer.isGone = true

            it.lifecycleOwner?.let { lifecycleOwner ->
                ad?.getView()?.observe(lifecycleOwner, adViewObserver)
            }
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemBannerAdBinding>) {
        ad?.getView()?.removeObserver(adViewObserver)
        ad = null

        binding?.also {
            AdUtil.unbindAdView(it.inFeedContainer)

            it.inFeedContainer.isGone = true
            binding = null
        }
        super.unbind(viewHolder)
    }

    private val adViewObserver = Observer<View> { view: View? ->
        binding?.also {
            AdUtil.bindAdView(it.inFeedContainer, view)
            val shouldShowAdView = view != null
            it.inFeedContainer.isVisible = shouldShowAdView
            it.executePendingBindings()
        }
    }

    override fun providerEqualableContents(): Array<*> =
        arrayOf(index.toLong())

    override fun equals(other: Any?): Boolean = isSameContents(other)

    override fun hashCode(): Int = contentsHash()

    override fun getLayout(): Int = R.layout.item_banner_ad
}