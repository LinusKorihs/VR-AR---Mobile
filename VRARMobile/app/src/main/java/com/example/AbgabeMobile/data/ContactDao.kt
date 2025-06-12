package com.example.AbgabeMobile.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAll(): Flow<List<Contact>>

    @Query("SELECT * FROM contacts WHERE id = :id")
    fun getContact(id: Int): Flow<Contact?>

    @Query("SELECT * FROM contacts ORDER BY name ASC")
    suspend fun getAllContactsList(): List<Contact>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contacts: List<Contact>)

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Query("DELETE FROM contacts")
    suspend fun deleteAllContacts()
}