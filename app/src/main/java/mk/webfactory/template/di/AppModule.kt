package mk.webfactory.template.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import mk.webfactory.template.di.qualifier.ApplicationContext

@Module
class AppModule {

    @Provides
    @ApplicationContext
    fun provideContext(application: Application): Context {
        return application
    }
}