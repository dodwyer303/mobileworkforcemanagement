package com.example.mobileworkforcemanagementapp.di.modules

import android.app.Application
import com.example.mobileworkforcemanagementapp.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule constructor(private val application: Application) {

    @Singleton
    @Provides
    fun provideViewModelFactory(): ViewModelFactory {
        return ViewModelFactory(application)
    }
}