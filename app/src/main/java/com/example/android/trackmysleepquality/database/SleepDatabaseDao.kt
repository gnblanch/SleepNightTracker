/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SleepDatabaseDao {
    @Insert
    suspend fun insert(night: SleepNight)

    @Update
    suspend fun update(night: SleepNight)

    // get() has a :Long key argument selecting all columns in the table WHERE matching
    // the nightId key (passed in) and returns the rows of a specific nullable :SleepNight?
    // Since keys are unique, this will return a single SleepNight row or NULL if none.
    // Note the :key is how we access a function parameter from within the query
    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key")
    suspend fun get(key:Long):SleepNight?

    // clear() has no arguments and will DELETE all entities FROM the table since no
    // where constraint was included
    @Query("DELETE FROM daily_sleep_quality_table")
    suspend fun clear()

    // getAllNights() has no arguments and returns a LiveData<List> of SleepNight(s)
    // The query SELECTS all records FROM the table and returns them BY the table
    // key nightId in DESCending order.  Since it is LiveData, Room will ensure
    // the data is updated whenever the database is updated so we only need to get this
    // list once, attach an observer to it and the UI will show any changed data.
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>>

    // getTonight() SELECTs all records from the table in DEScending ORDER by key with
    // a LIMIT set to 1 meaning only the highest key value (the latest night) is returned.
    // We make the return SleepNight nullable ? as there is no way to guarantee the table
    // hasn't been cleared.
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    suspend fun getTonight(): SleepNight?

}