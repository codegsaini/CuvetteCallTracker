package gaurav.cuvettecalltracker.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class DataStoreRepository(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun <T: Any> setValue(key: Preferences.Key<T>, value: T) {
        dataStore.edit {
            it[key] = value
        }
    }

    fun <T: Any> getValue(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return dataStore.data.catch {
            if (it is IOException) emit(emptyPreferences())
            else throw it
        }.map {
            it[key] ?: defaultValue
        }
    }
}