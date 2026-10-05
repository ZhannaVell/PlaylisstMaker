package com.example.playlisstmaker.di


import com.example.playlisstmaker.search.data.SearchHistoryDataStore
import com.example.playlisstmaker.search.data.network.NetworkClient
import com.example.playlisstmaker.search.data.network.RetrofitNetworkClient
import com.example.playlisstmaker.search.data.network.iTunesApi
import com.example.playlisstmaker.settings.data.SettingsDataStore
import com.example.playlisstmaker.sharing.data.ExternalNavigator

import com.example.playlisstmaker.sharing.domain.SharingNavigator

import com.example.playlisstmaker.utils.AndroidResourceProvider
import com.example.playlisstmaker.utils.ResourceProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.playlisstmaker.utils.Constants.ITUNES_BASE_URL
import com.google.gson.Gson

val dataModule = module {
//--------Network--------------
    single<iTunesApi> {
        Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(iTunesApi::class.java)
    }
    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }
    //-----------Storage----------
    single {
        Gson()

    }
    single {
        SearchHistoryDataStore(androidContext(), get())
    }

    single {
        SettingsDataStore(androidContext())
    }
    //---------Sharing----------
    single<SharingNavigator> {
        ExternalNavigator(androidContext())
    }



    //-----------Utils-----------
    single<ResourceProvider> {
        AndroidResourceProvider(androidContext())
    }


}