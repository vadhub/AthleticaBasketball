package com.vlg.athletica

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("1a33b6c1-5147-45f6-8593-a0e0c01667ba")
    }
}