package gaurav.cuvettecalltracker.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import gaurav.cuvettecalltracker.domain.model.CallLog
import gaurav.cuvettecalltracker.presentation.util.CallType
import kotlinx.coroutines.flow.Flow

@Dao
interface CallLogDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCallLog(callLog: CallLog)

    @Query("SELECT * FROM calllog ORDER BY timestamp DESC")
    fun getRecentLogs(): Flow<List<CallLog>>

    @Upsert
    suspend fun upsertCallLog(callLog: CallLog)

    @Query("SELECT * FROM calllog WHERE number = :number AND settled = 0 LIMIT 1")
    fun getRecentUnsettledLog(number: String): Flow<CallLog>

    @Query("SELECT * FROM calllog WHERE number = :number AND settled = 1")
    fun getCallHistory(number: String): Flow<List<CallLog>>

    @Query("UPDATE calllog SET active = 0, duration = :duration WHERE number = :number")
    suspend fun updateCallStateToInactive(number: String, duration: Int)

    @Query("SELECT COUNT(id) FROM calllog")
    fun getTotalLogs(): Flow<Int>

    @Query("SELECT COUNT(1) FROM calllog WHERE callType = :callType")
    fun getTotalIncomingCallLogs(callType: CallType = CallType.INCOMING): Flow<Int>

    @Query("SELECT COUNT(1) FROM calllog WHERE callType = :callType")
    fun getTotalOutgoingCallLogs(callType: CallType = CallType.OUTGOING): Flow<Int>

    @Query("SELECT COUNT(1) FROM calllog WHERE callType = :callType")
    fun getTotalMissedCallLogs(callType: CallType = CallType.MISSED): Flow<Int>
}