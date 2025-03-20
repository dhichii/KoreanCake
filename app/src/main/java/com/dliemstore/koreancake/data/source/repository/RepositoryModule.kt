package com.dliemstore.koreancake.data.source.repository

import com.dliemstore.koreancake.data.api.PersistentCookieStore
import com.dliemstore.koreancake.data.api.TokenManager
import com.dliemstore.koreancake.data.api.service.AuthService
import com.dliemstore.koreancake.data.api.service.OrderService
import com.dliemstore.koreancake.data.api.service.ProcessService
import com.dliemstore.koreancake.data.api.service.UserService
import com.dliemstore.koreancake.data.source.repository.auth.AuthRepository
import com.dliemstore.koreancake.data.source.repository.order.OrderRepository
import com.dliemstore.koreancake.data.source.repository.process.ProcessRepository
import com.dliemstore.koreancake.data.source.repository.user.UserRepository
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
        tokenManager: TokenManager,
        persistentCookieStore: PersistentCookieStore
    ): AuthRepository {
        return AuthRepository(authService, tokenManager, persistentCookieStore)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(orderService: OrderService): OrderRepository {
        return OrderRepository(orderService)
    }

    @Provides
    @Singleton
    fun provideProcessRepository(processService: ProcessService): ProcessRepository {
        return ProcessRepository(processService)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userService: UserService): UserRepository {
        return UserRepository(userService)
    }
}