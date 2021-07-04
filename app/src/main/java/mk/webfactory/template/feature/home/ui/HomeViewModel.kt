package mk.webfactory.template.feature.home.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import mk.webfactory.template.di.UserScopeComponentManager
import mk.webfactory.template.di.scope.UserScope
import javax.inject.Inject

@UserScope
@HiltViewModel
class HomeViewModel @Inject constructor(
    userScopeComponentManager: UserScopeComponentManager) : ViewModel() {

    private val homeRepository = userScopeComponentManager.entryPoint.homeRepository()

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

