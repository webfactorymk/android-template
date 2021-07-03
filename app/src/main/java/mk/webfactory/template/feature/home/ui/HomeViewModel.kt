package mk.webfactory.template.feature.home.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    var progressBarState = MutableLiveData<Boolean>()

    fun handleEvent(event: Any) {
        when (event) {
            //is HomeEvent.OnStart -> handleOnStart()
        }
    }

    private fun handleOnStop() {
        progressBarState.value = false
    }

    private fun handleOnStart() {
        progressBarState.value = true
    }
}

