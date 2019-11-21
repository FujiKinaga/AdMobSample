package fujikinaga.sample.admobsample.di

import android.content.Context
import dagger.Module
import dagger.Provides
import fujikinaga.sample.admobsample.model.util.ResourceProvider

@Module
object AppModule {

    @JvmStatic
    @Provides
    fun provideResourceProvider(context: Context): ResourceProvider {
        return ResourceProvider.getInstance(context)
    }
}