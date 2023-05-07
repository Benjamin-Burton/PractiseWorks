package burt.music.practiseworks

import android.app.Application
import burt.music.practiseworks.data.AppContainer
import burt.music.practiseworks.data.AppDataContainer

class PractiseWorksApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}