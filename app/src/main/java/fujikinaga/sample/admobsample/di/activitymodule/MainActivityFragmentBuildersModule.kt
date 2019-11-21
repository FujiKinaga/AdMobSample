package fujikinaga.sample.admobsample.di.activitymodule

import dagger.Module
import dagger.android.ContributesAndroidInjector
import fujikinaga.sample.admobsample.ui.main.MainFragment

@Module
interface MainActivityFragmentBuildersModule {

    @ContributesAndroidInjector
    fun contributeMainFragment(): MainFragment
}