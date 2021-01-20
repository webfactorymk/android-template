package mk.webfactory.template.feature.home.ui.mvvm

interface IHomeContract {

    interface View {

        fun observe()
        fun setUpClickListeners()
    }

    interface ViewModel {

        fun handleEvent(event: HomeEvent)
        fun handleOnStop()
        fun handleOnStart()
    }
}

sealed class HomeEvent {

    // Here add all view events possible. If you need to pass some values throw the events use class instead of object.
    object OnStart : HomeEvent()
    object OnStop : HomeEvent()
}




