package com.app.weather.di

import com.app.weather.repository.WeatherRepository
import com.app.weather.repository.WeatherRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideWeatherRepository(weatherRepositoryImp: WeatherRepositoryImp): WeatherRepository
}