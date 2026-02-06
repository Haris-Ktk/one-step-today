package com.harrydev.onesteptoday.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StepDao {

    @Insert
    suspend fun insert(step: StepEntity)

    @Query("DELETE FROM steps")
    suspend fun deleteAll()

    @Query("SELECT * FROM steps ORDER BY id DESC")
    fun getAllLive(): LiveData<List<StepEntity>>

    @Query("SELECT * FROM steps WHERE isCompleted = 1 ORDER BY completedAt ASC")
    fun getCompletedLive(): LiveData<List<StepEntity>>

    @Delete
    suspend fun delete(step: StepEntity)

    @Query("""
        UPDATE steps 
        SET isCompleted = :done,
            completedAt = :time
        WHERE id = :id
    """)
    suspend fun updateDone(id: Int, done: Boolean, time: Long?)

    @Query("""
        UPDATE steps 
        SET title = :title,
            shortDesc = :shortDesc,
            longDesc = :longDesc
        WHERE id = :id
    """)
    suspend fun update(
        id: Int,
        title: String,
        shortDesc: String,
        longDesc: String
    )
}
