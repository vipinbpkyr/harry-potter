package com.vipin.data.di

import android.content.Context
import androidx.room.Room
import com.vipin.data.datasource.CharacterLocalDataSource
import com.vipin.data.datasource.CharacterLocalDataSourceImpl
import com.vipin.data.datasource.CharacterRemoteDataSource
import com.vipin.data.datasource.CharacterRemoteDataSourceImpl
import com.vipin.data.local.AppDatabase
import com.vipin.data.local.CharacterDao
import com.vipin.data.remote.HarryPotterApiService
import com.vipin.data.repository.CharacterRepositoryImpl
import com.vipin.domain.repository.CharacterRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideHarryPotterApiService(retrofit: Retrofit): HarryPotterApiService =
        retrofit.create(HarryPotterApiService::class.java)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "harry_potter_database"
        ).build()

    @Provides
    @Singleton
    fun provideCharacterDao(appDatabase: AppDatabase): CharacterDao =
        appDatabase.characterDao()
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {
    @Binds
    abstract fun bindCharacterRemoteDataSource(
        characterRemoteDataSourceImpl: CharacterRemoteDataSourceImpl
    ): CharacterRemoteDataSource

    @Binds
    abstract fun bindCharacterLocalDataSource(
        characterLocalDataSourceImpl: CharacterLocalDataSourceImpl
    ): CharacterLocalDataSource
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepoModule {
    @Binds
    abstract fun bindCharacterRepository(
        characterRepositoryImpl: CharacterRepositoryImpl
    ): CharacterRepository
}
