package com.example.mobileworkforcemanagementapp

import android.app.Application
import android.content.Context
import com.example.mobileworkforcemanagementapp.di.components.AppComponent
import com.example.mobileworkforcemanagementapp.di.components.DaggerAppComponent
import com.example.mobileworkforcemanagementapp.di.modules.AppModule

open class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        appComponent = initDaggerComponent()
    }

    fun getApplicationComponent(): AppComponent {
        return appComponent
    }

    private fun initDaggerComponent():AppComponent{
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
        return  appComponent
    }

    companion object {
        fun get(context: Context): MyApplication {
            return context.applicationContext as MyApplication
        }
        lateinit var appComponent: AppComponent
    }
}