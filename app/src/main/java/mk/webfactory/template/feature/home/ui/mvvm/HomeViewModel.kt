package mk.webfactory.template.feature.home.ui.mvvm

import androidx.lifecycle.MutableLiveData

class HomeViewModel : IHomeContract.ViewModel{

    var progressBarState = MutableLiveData<Boolean>()

    override fun handleEvent(event: HomeEvent) {
        when(event){
            is HomeEvent.OnStart -> handleOnStart()
            is HomeEvent.OnStop -> handleOnStop()
        }
    }

    override fun handleOnStop() {
        progressBarState.value = false
    }

    override fun handleOnStart() {
        progressBarState.value = true
    }
}

