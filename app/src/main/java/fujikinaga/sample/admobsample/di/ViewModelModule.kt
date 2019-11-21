package fujikinaga.sample.admobsample.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import fujikinaga.sample.admobsample.util.ViewModelFactory

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}