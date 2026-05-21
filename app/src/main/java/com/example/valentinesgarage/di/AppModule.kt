package com.example.valentinesgarage.di

import com.google.firebase.firestore.FirebaseFirestore
import com.example.valentinesgarage.data.repository.*
import com.example.valentinesgarage.domain.repository.*
import com.example.valentinesgarage.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    // Repositories
    @Provides
    @Singleton
    fun provideTruckRepository(firestore: FirebaseFirestore): TruckRepository =
        FirestoreTruckRepository(firestore)

    @Provides
    @Singleton
    fun provideJobRepository(firestore: FirebaseFirestore): JobRepository =
        FirestoreJobRepository(firestore)

    @Provides
    @Singleton
    fun provideEmployeeRepository(firestore: FirebaseFirestore): EmployeeRepository =
        FirestoreEmployeeRepository(firestore)

    @Provides
    @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore): UserRepository =
        FirestoreUserRepository(firestore)

    // Use Cases
    @Provides
    fun provideCheckInTruckUseCase(
        truckRepo: TruckRepository,
        jobRepo: JobRepository
    ) = CheckInTruckUseCase(truckRepo, jobRepo)

    @Provides
    fun provideGetActiveJobsUseCase(jobRepo: JobRepository) = GetActiveJobsUseCase(jobRepo)

    @Provides
    fun provideGetJobUseCase(jobRepo: JobRepository) = GetJobUseCase(jobRepo)

    @Provides
    fun provideUpdateJobUseCase(jobRepo: JobRepository) = UpdateJobUseCase(jobRepo)

    @Provides
    fun provideAddNoteToTaskUseCase(jobRepo: JobRepository): AddNoteToTaskUseCase =
        AddNoteToTaskUseCase(jobRepo)

    @Provides
    fun provideCompleteRepairTaskUseCase(jobRepo: JobRepository) = CompleteRepairTaskUseCase(jobRepo)

    @Provides
    fun provideGetEmployeesUseCase(empRepo: EmployeeRepository) = GetEmployeesUseCase(empRepo)

    @Provides
    fun provideGetEmployeeReportUseCase(empRepo: EmployeeRepository) = GetEmployeeReportUseCase(empRepo)
}