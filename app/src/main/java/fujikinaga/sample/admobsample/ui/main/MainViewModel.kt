package fujikinaga.sample.admobsample.ui.main

import android.view.View
import androidx.lifecycle.*
import javax.inject.Inject

class MainViewModel @Inject constructor(): ViewModel(), LifecycleObserver {

    private val _onListLoaded: MutableLiveData<List<String>> = MutableLiveData()
    val onListLoaded: LiveData<List<String>> get() = _onListLoaded

    init {
        _onListLoaded.value = listOf(
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturdays",
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        )
    }
}
