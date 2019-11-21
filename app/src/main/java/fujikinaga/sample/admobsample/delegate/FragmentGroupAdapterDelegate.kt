package fujikinaga.sample.admobsample.delegate

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.databinding.GroupieViewHolder
import kotlin.reflect.KProperty

class FragmentGroupAdapterDelegate {
    private var groupAdapter: GroupAdapter<GroupieViewHolder<*>>? = null

    operator fun getValue(
        thisRef: Fragment,
        property: KProperty<*>
    ): GroupAdapter<GroupieViewHolder<*>> {
        return groupAdapter ?: GroupAdapter<GroupieViewHolder<*>>().also {
            groupAdapter = it
            thisRef.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroyView() {
                    thisRef.viewLifecycleOwner.lifecycle.removeObserver(this)
                    groupAdapter?.clear()
                    groupAdapter = null // For Fragment's view recreation
                }
            })
        }
    }
}

@Suppress("unused")
fun Fragment.groupAdapter(): FragmentGroupAdapterDelegate = FragmentGroupAdapterDelegate()