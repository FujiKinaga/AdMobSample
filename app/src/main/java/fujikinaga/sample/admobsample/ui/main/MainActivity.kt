package fujikinaga.sample.admobsample.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import dagger.Module
import dagger.Provides
import dagger.android.support.DaggerAppCompatActivity
import fujikinaga.sample.admobsample.R
import fujikinaga.sample.admobsample.ad.Ad
import fujikinaga.sample.admobsample.ad.manager.BottomAdManager
import fujikinaga.sample.admobsample.databinding.MainActivityBinding
import fujikinaga.sample.admobsample.di.ActivityScope
import fujikinaga.sample.admobsample.util.AdUtil
import fujikinaga.sample.admobsample.util.LifeCycleProvider
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var bottomAdManager: BottomAdManager

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        lifecycle.addObserver(bottomAdManager)
        toggleBottomAdView()
    }

    private fun toggleBottomAdView() {
        // アドジェネのメディエーション配信がActivityのインスタンスを期待しているため、Activity Contextを渡す
        bottomAdManager.also {
            it.updateAdView(this)
            it.ad?.setCallback(object : Ad.Callback {
                override fun onSuccess(view: View?) {
                    AdUtil.bindAdView(binding.bottomAdContainer, view)
                    showBottomAdView()
                }

                override fun onFailure(errorCode: Int) {
                    hideBottomAdView()
                }
            })
            AdUtil.loadAdForPrefetch(it.ad)
        }
    }

    private fun hideBottomAdView() {
        if (binding.bottomAdContainer.isGone) {
            return
        }
        binding.bottomAdContainer.isGone = true
    }

    private fun showBottomAdView() {
        if (binding.bottomAdContainer.isVisible) {
            return
        }
        binding.bottomAdContainer.isVisible = true
    }

    override fun onDestroy() {
        lifecycle.removeObserver(bottomAdManager)
        super.onDestroy()
    }
}

@Module
abstract class MainActivityChildModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        @ActivityScope
        fun providesLifeCycleProvider(mainActivity: MainActivity): LifeCycleProvider {
            return LifeCycleProvider(mainActivity.lifecycle)
        }
    }
}
