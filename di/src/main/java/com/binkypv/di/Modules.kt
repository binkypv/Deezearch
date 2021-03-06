package com.binkypv.di

import com.binkypv.data.datasource.DeezerDataSource
import com.binkypv.data.repository.DeezerRepositoryImpl
import com.binkypv.data.utils.Constants.BASE_URL
import com.binkypv.domain.repository.DeezerRepository
import com.binkypv.presentation.viewmodel.AlbumTracksViewModel
import com.binkypv.presentation.viewmodel.ArtistViewModel
import com.binkypv.presentation.viewmodel.SearchViewModel
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Modules {
    val viewModelModule = module {
        viewModel { SearchViewModel(get()) }
        viewModel { ArtistViewModel(get()) }
        viewModel { AlbumTracksViewModel(get()) }
    }

    val repositoryModule = module {
        fun provideDeezerRepository(dataSource: DeezerDataSource): DeezerRepository =
            DeezerRepositoryImpl(dataSource)

        single { provideDeezerRepository(get()) }
    }

    val dataSourceModule = module {
        fun provideDeezerDataSource(retrofit: Retrofit): DeezerDataSource {
            return retrofit.create(DeezerDataSource::class.java)
        }

        single { provideDeezerDataSource(get()) }
    }

    val netModule = module {

        fun provideHttpClient(): OkHttpClient {
            val okHttpClientBuilder = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
            return okHttpClientBuilder.build()
        }

        fun provideGson(): Gson {
            return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
        }

        fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }

        single { provideHttpClient() }
        single { provideRetrofit(get(), get()) }
        single { provideGson() }

    }
}