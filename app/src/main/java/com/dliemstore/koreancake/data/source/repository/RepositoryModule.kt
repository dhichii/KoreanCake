package com.dliemstore.koreancake.data.source.repository

import com.dliemstore.koreancake.data.api.TokenManager
import com.dliemstore.koreancake.data.api.service.AuthService
import com.dliemstore.koreancake.data.source.repository.auth.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        authService: AuthService,
        tokenManager: TokenManager
    ): AuthRepository {
        return AuthRepository(authService, tokenManager)
    }
}