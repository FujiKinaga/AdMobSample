package fujikinaga.sample.admobsample.model.util

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes

class ResourceProvider constructor(val context: Context) {

    val resources: Resources
        get() = context.resources

    fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }

    companion object {

        fun getInstance(context: Context): ResourceProvider {
            return ResourceProvider(context)
        }
    }
}
