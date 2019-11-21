package fujikinaga.sample.admobsample.ui.main.item

import android.view.View
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

            it.lifecycleOwner?.let { lifecycleOwner ->
                ad?.getView()?.observe(lifecycleOwner, adViewObserver)
            }
        }
        binding.lifecycleOwner?.let { lifecycleOwner ->
            ad?.getView()?.observe(lifecycleOwner, Observer {
                AdUtil.bindAdView(binding.inFeedContainer, it)
                binding.executePendingBindings()
            })
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemBannerAdBinding>) {
        AdUtil.unbindAdView(viewHolder.binding.inFeedContainer)
        ad?.getView()?.removeObserver(adViewObserver)
        ad = null
        super.unbind(viewHolder)
    }

    private val adViewObserver = Observer<View> { view: View? ->
        binding?.also {
            AdUtil.bindAdView(it.inFeedContainer, view)
            it.executePendingBindings()
        }
    }

    override fun providerEqualableContents(): Array<*> =
        arrayOf(index.toLong())

    override fun equals(other: Any?): Boolean = isSameContents(other)

    override fun hashCode(): Int = contentsHash()

    override fun getLayout(): Int = R.layout.item_banner_ad
}