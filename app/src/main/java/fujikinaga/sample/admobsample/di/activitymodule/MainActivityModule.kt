package fujikinaga.sample.admobsample.di.activitymodule

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import fujikinaga.sample.admobsample.di.ActivityScope
import fujikinaga.sample.admobsample.di.ViewModelKey
import fujikinaga.sample.admobsample.ui.main.MainActivity
import fujikinaga.sample.admobsample.ui.main.MainActivityChildModule
import fujikinaga.sample.admobsample.ui.main.MainViewModel

@Module
abstract class MainActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityFragmentBuildersModule::class, MainActivityChildModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindBlockUserListViewModel(mainViewModel: MainViewModel): ViewModel
}