package com.hipaasafe.di

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.hipaasafe.base.BaseApplication
import com.hipaasafe.data.repository.LoginRepositoryImp
import com.hipaasafe.data.source.remote.ApiNames
import com.hipaasafe.data.source.remote.ApiService
import com.hipaasafe.domain.repository.LoginRepository
import com.hipaasafe.domain.usecase.LoginUseCase
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIME_OUT = 30L

val NetworkModule = module {

    single(named("normalService")) {
        createService(get(named("normal")))
    }
    single(named("candidateService")) {
        createService(get(named("candidate")))
    }

    single(named("normal")) {
        createRetrofit(get(), ApiNames.BASE_URL)
    }
    single(named("candidate")) {
        createRetrofit(get(), ApiNames.BASE_URL_CANDIDATE)
    }

    single {
        createOkHttpClient()
    }
    single {
        GsonConverterFactory.create()
    }
    single {
        GsonBuilder().create()
    }

}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder().addInterceptor(
        ChuckerInterceptor.Builder(BaseApplication().getAppContext())
            .collector(ChuckerCollector(BaseApplication().getAppContext()))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()
    )
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .addInterceptor(Interceptor { chain ->
            val original = chain.request()

            val requestBuilder: Request.Builder = original.newBuilder()
                .header("Authorization", "application/json")
                .header("Content-Type", "application/json")
                .method(original.method, original.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        })
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

fun createRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun createService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}

fun createLoginRepository(apiService: ApiService): LoginRepository {
    return LoginRepositoryImp(apiService)
}

fun createLoginUseCase(loginRepository: LoginRepository): LoginUseCase {
    return LoginUseCase(loginRepository)
}
