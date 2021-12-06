package com.example.mobileworkforcemanagementapp.di.components

import com.example.mobileworkforcemanagementapp.di.modules.AppModule
import com.example.mobileworkforcemanagementapp.ui.MainActivity
import com.example.mobileworkforcemanagementapp.ui.fragments.AddEditFragment
import com.example.mobileworkforcemanagementapp.ui.fragments.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(addEditFragment: AddEditFragment)
}