package gaurav.cuvettecalltracker.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import gaurav.cuvettecalltracker.domain.model.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert
    fun addContact(contact: Contact)

    @Query("SELECT * FROM contact WHERE number = :number")
    fun getContact(number: String) : Flow<Contact>
}