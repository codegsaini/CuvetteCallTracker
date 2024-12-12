package gaurav.cuvettecalltracker.domain.model

import gaurav.cuvettecalltracker.presentation.util.CallType
import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val id: String,
    val name: String?,
    val number: String,
    val profilePic: String
)

@Serializable
data class CallLog(
    val id: String,
    val contactId: String,
    val timestamp: Long,
    val callType: CallType,
    val duration: Int, // In seconds
    val sim: Int
)