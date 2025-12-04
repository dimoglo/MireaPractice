package com.example.mireapractice.di

import com.example.mireapractice.data.CurrencyRepositoryImpl
import com.example.mireapractice.data.remote.banks.BankApiService
import com.example.mireapractice.data.remote.currency.CurrencyApiService
import com.example.mireapractice.data.remote.news.NewsApiService
import com.example.mireapractice.domain.CurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import com.example.mireapractice.data.local.CurrencyEntity
import com.example.mireapractice.data.local.ExchangeRateEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideCurrencyRetrofit(): CurrencyApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api-flag-5ti4.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    ).build()
            ).build()
        return retrofit.create(CurrencyApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRetrofit(): NewsApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://search.worldbank.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    ).build()
            ).build()
        return retrofit.create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDaDataRetrofit(): BankApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", "Token")
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://suggestions.dadata.ru/suggestions/api/4_1/rs/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(BankApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRealmConfiguration(): RealmConfiguration {
        return RealmConfiguration.Builder(
            schema = setOf(
                ExchangeRateEntity::class,
                CurrencyEntity::class
            )
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideRealm(
        config: RealmConfiguration
    ): Realm {
        val realm = Realm.open(config)
        return realm
    }

    @Module
    @InstallIn(ViewModelComponent::class)
    internal abstract class MappingModule {

        @Binds
        abstract fun bindCurrencyRepository(impl: CurrencyRepositoryImpl): CurrencyRepository
    }
}