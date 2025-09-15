package com.app.feelweather.di

import com.app.feelweather.repository.WeatherRepository
import com.app.feelweather.repository.WeatherRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideWeatherRepository(weatherRepositoryImp: WeatherRepositoryImp): WeatherRepository
}