package fujikinaga.sample.admobsample.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.GroupieViewHolder
import dagger.android.support.DaggerFragment
import fujikinaga.sample.admobsample.R
import fujikinaga.sample.admobsample.ad.Ad
import fujikinaga.sample.admobsample.ad.manager.InFeedAdManager
import fujikinaga.sample.admobsample.ad.manager.InterstitialAdManager
import fujikinaga.sample.admobsample.databinding.MainFragmentBinding
import fujikinaga.sample.admobsample.delegate.dataBinding
import fujikinaga.sample.admobsample.delegate.groupAdapter
import fujikinaga.sample.admobsample.ui.main.item.BannerAdItem
import fujikinaga.sample.admobsample.ui.main.item.NativeAdItem
import fujikinaga.sample.admobsample.ui.main.item.TextItem
import javax.inject.Inject

private const val IN_FEED_ADV_POSITION = 11
private const val IN_FEED_ADV_START_POSITION = 3

class MainFragment : DaggerFragment(), OnFeedActionListener {

    @Inject
    lateinit var inFeedAdManager: InFeedAdManager

    @Inject
    lateinit var interstitialAdManager: InterstitialAdManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private val binding: MainFragmentBinding by dataBinding(R.layout.main_fragment)

    private val groupAdapter: GroupAdapter<GroupieViewHolder<*>> by groupAdapter()

    private var nextAdvertisementPosition = IN_FEED_ADV_START_POSITION

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycle.addObserver(viewModel)
        lifecycle.addObserver(inFeedAdManager)
        lifecycle.addObserver(interstitialAdManager)
    }

    override fun onDetach() {
        super.onDetach()
        lifecycle.removeObserver(viewModel)
        lifecycle.removeObserver(inFeedAdManager)
        lifecycle.removeObserver(interstitialAdManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = groupAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inFeedAdManager.updateAdViewLists(requireActivity())
        interstitialAdManager.updateAdView(requireActivity())

        viewModel.also {
            it.onListLoaded.observe(viewLifecycleOwner, Observer {
                updateListItem(it)
            })
        }
    }

    private fun updateListItem(listItem: List<String>) {
        val items = mutableListOf<BindableItem<*>>()
        items.addAll(listItem.map {
            TextItem(it, this)
        })
        groupAdapter.update(addAdvertisementItem(items))
    }

    private fun addAdvertisementItem(listItem: MutableList<BindableItem<*>>): List<BindableItem<*>> {
        (0..listItem.size)
            .forEach { index ->
                if (groupAdapter.itemCount + index == nextAdvertisementPosition) {
                    listItem.add(
                        index, if (inFeedAdManager.isShowNativeAd) {
                            NativeAdItem(
                                index,
                                this,
                                viewLifecycleOwner
                            )
                        } else {
                            BannerAdItem(
                                index,
                                this,
                                viewLifecycleOwner
                            )
                        }
                    )
                    nextAdvertisementPosition += IN_FEED_ADV_POSITION
                }
            }
        return listItem
    }

    override fun getAd(): Ad? {
        return inFeedAdManager.getAndFetchAd
    }

    override fun onCellClick() {
        interstitialAdManager.ad?.also {
            if (!it.isLoaded) {
                if (!it.isLoading) {
                    interstitialAdManager.load()
                }
                return
            }
            it.adListener = object : AdListener() {
                override fun onAdOpened() {

                }

                override fun onAdClosed() {
                    it.adListener = null
                    interstitialAdManager.load()
                }
            }
            it.show()
        }
    }
}
