package gaurav.cuvettecalltracker.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import gaurav.cuvettecalltracker.presentation.util.CallType
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class CallLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val number: String,
    val timestamp: Long,
    val callType: CallType,
    val duration: Int, // In seconds
    val active: Boolean = false,
    val settled: Boolean = false
)