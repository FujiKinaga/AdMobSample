package fujikinaga.sample.admobsample.delegate

import android.os.Handler
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentBindingDelegate<T : ViewDataBinding>
internal constructor(
    @LayoutRes private val layoutResId: Int
) : ReadOnlyProperty<Fragment, T> {
    private var binding: T? = null
    private var lifecycle: Lifecycle? = null
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
        (binding ?: DataBindingUtil.inflate<T>(
            thisRef.layoutInflater,
            layoutResId,
            thisRef.requireActivity().findViewById(thisRef.id) as? ViewGroup,
            false
        )).also {
            binding = it
            if (thisRef.view == null) {
                // Can't access the Fragment View's LifecycleOwner when getView() is null
                return@also
            }
            lifecycle = thisRef.viewLifecycleOwner.lifecycle.also { lifecycle ->
                lifecycle.removeObserver(observer)
                lifecycle.addObserver(observer)
            }
        }

    private val observer = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroyView() {
            lifecycle?.removeObserver(this)
            Handler().post {
                binding = null // For Fragment's view recreation
                lifecycle = null
            }
        }
    }
}

@Suppress("unused")
fun <T : ViewDataBinding> Fragment.dataBinding(@LayoutRes layoutResId: Int): FragmentBindingDelegate<T> =
    FragmentBindingDelegate(layoutResId)