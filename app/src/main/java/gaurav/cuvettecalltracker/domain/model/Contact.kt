package gaurav.cuvettecalltracker.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Contact(
    @PrimaryKey
    val number: String,
    val name: String? = null,
    val profilePic: String? = null
)