package com.example.myapplication.app

import android.app.Application
import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.myapplication.MyCurrentLocationService


open class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                Intent(applicationContext, MyCurrentLocationService::class.java).apply {
                    action = MyCurrentLocationService.ACTION_STOP
                    startService(this)
                }
            }
            override fun onStop(owner: LifecycleOwner) {
                Intent(applicationContext, MyCurrentLocationService::class.java).apply {
                    action = MyCurrentLocationService.ACTION_START
                    startService(this)
                }
            }
        })
    }

}