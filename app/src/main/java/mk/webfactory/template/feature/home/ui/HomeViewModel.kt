package mk.webfactory.template.feature.home.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mk.webfactory.template.di.scope.UserScope

@UserScope
@HiltViewModel
class HomeViewModel @ViewModelInject constructor() : ViewModel() {

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

