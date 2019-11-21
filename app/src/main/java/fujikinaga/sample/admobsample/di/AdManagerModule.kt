package fujikinaga.sample.admobsample.di

import dagger.Module
import dagger.Provides
import fujikinaga.sample.admobsample.ad.manager.BottomAdManager
import fujikinaga.sample.admobsample.ad.manager.InFeedAdManager
import fujikinaga.sample.admobsample.ad.manager.InterstitialAdManager

@Module
object AdManagerModule {

    @JvmStatic
    @Provides
    fun provideBottomAdManager(): BottomAdManager {
        return BottomAdManager()
    }

    @JvmStatic
    @Provides
    fun provideInFeedAdManager(): InFeedAdManager {
        return InFeedAdManager()
    }

    @JvmStatic
    @Provides
    fun provideInterstitialAdManager(): InterstitialAdManager {
        return InterstitialAdManager()
    }
}