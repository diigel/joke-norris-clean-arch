package com.utsman.jokenorris

import com.utsman.jokenorris.data.Services
import com.utsman.jokenorris.usecase.JokeRepository
import com.utsman.jokenorris.usecase.JokeRepositoryImpl
import com.utsman.jokenorris.data.source.JokeDataSource
import com.utsman.jokenorris.data.source.JokeDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object MainModule {

    @Provides
    fun provideDataSource(): JokeDataSource {
        val service = Services.create("https://api.chucknorris.io/")
        return JokeDataSourceImpl(service)
    }

    @Provides
    fun provideRepository(dataSource: JokeDataSource): JokeRepository {
        return JokeRepositoryImpl(dataSource)
    }
}