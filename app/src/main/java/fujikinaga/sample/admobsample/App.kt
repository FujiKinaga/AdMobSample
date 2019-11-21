package fujikinaga.sample.admobsample

import com.google.android.gms.ads.MobileAds
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import fujikinaga.sample.admobsample.di.DaggerAppComponent
import timber.log.Timber

class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent
            .builder()
            .application(this)
            .context(applicationContext)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this, getString(R.string.ad_mob_app_id))
        plantTimber()
    }

    private fun plantTimber() {
        Timber.plant(Timber.DebugTree())
    }
}