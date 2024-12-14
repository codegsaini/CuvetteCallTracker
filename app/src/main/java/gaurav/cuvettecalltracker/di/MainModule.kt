package gaurav.cuvettecalltracker.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gaurav.cuvettecalltracker.data.CallLogRepository
import gaurav.cuvettecalltracker.data.DataStoreRepository
import gaurav.cuvettecalltracker.room.LocalDatabase
import gaurav.cuvettecalltracker.room.dao.CallLogDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {


    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return  PreferenceDataStoreFactory.create (
            corruptionHandler = ReplaceFileCorruptionHandler (
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile("prefs") }
        )
    }

    @Provides
    @Singleton
    fun providesLocalDatabase(@ApplicationContext context: Context) : LocalDatabase {
        return Room.databaseBuilder(
            context = context,
            name = "local_database",
            klass = LocalDatabase::class.java,
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providesDataStoreRepository(dataStore: DataStore<Preferences>) : DataStoreRepository {
        return DataStoreRepository(dataStore)
    }

    @Provides
    @Singleton
    fun providesCallLogRepository(
        @ApplicationContext context: Context,
        dataStoreRepository: DataStoreRepository,
        callLogDao: CallLogDao) : CallLogRepository
    {
        return CallLogRepository(context, dataStoreRepository, callLogDao)
    }

    @Provides
    @Singleton
    fun providesCallLogDao(database: LocalDatabase) : CallLogDao {
        return database.callLogDao()
    }
}