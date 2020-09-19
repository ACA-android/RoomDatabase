package com.dm.roomdatabase

import androidx.room.*

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Long,
    val name: String,
    val age: Int,
    @Ignore
    var addresses: List<Address>
) {
    constructor(userId: Long,
                name: String,
                age: Int) : this(userId, name, age, listOf())
}

@Entity(tableName = "address")
data class Address(
    @PrimaryKey(autoGenerate = true)
    val addressId: Long,
    var parentUserId: Long,
    val city: String,
    val street: String,
    val building: Int
)

data class UserWithAddresses(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "parentUserId",
        entity = Address::class
    )
    val addresses: List<Address>
)