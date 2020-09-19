package com.dm.roomdatabase

import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM address")
    suspend fun getAllAddresses(): List<Address>

    suspend fun getUsersFull(): List<User> {
        val result = mutableListOf<User>()
        getUsersWithAddresses().forEach {
            val user = it.user
            user.addresses = it.addresses
            result.add(user)
        }
        return result
    }

    @Transaction
    @Query("SELECT * FROM user_table")
    suspend fun getUsersWithAddresses(): List<UserWithAddresses>

    @Query("SELECT * FROM user_table WHERE age>:age")
    suspend fun findUsersOlderThan(age: Int): User

    suspend fun insertAddressesForUser(user: User, address: List<Address>) {
        address.forEach {
            it.parentUserId = user.userId
        }
        insertAddresses(address)
    }

    @Insert
    suspend fun insertAddresses(addresses: List<Address>)

    @Insert
    suspend fun insertUser(user: User): Long

    @Insert
    suspend fun insertAll(vararg user: User)

    @Delete
    suspend fun delete(user: User)
}