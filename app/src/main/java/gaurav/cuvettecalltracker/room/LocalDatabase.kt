package gaurav.cuvettecalltracker.room

import androidx.room.Database
import androidx.room.RoomDatabase
import gaurav.cuvettecalltracker.domain.model.CallLog
import gaurav.cuvettecalltracker.domain.model.Contact
import gaurav.cuvettecalltracker.room.dao.CallLogDao

@Database(entities = [CallLog::class, Contact::class], version = 7)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun callLogDao(): CallLogDao
}