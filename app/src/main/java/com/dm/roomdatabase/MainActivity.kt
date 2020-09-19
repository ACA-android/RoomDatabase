package com.dm.roomdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java, "users"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saveBtn.setOnClickListener {


            val user = User(
                0,
                nameEditText.text.toString(),
                ageEditText.text.toString().toInt()
            )
            GlobalScope.launch(Dispatchers.IO) {
                val id = db.userDao().insertUser(user)
                user.userId = id
                db.userDao().insertAddressesForUser(user,
                    listOf(
                        Address(0, 0, "Yerevan", "Abovyan", 28),
                        Address(0, 0, "Yerevan", "Saryan", 45),
                        Address(0, 0, "Yerevan", "Mashtoc", 55))
                )
                refreshUsers()
            }
        }

        refreshUsers()
    }

    private fun refreshUsers() {
        GlobalScope.launch(Dispatchers.IO) {
            val text = StringBuilder("Users:\n")
            db.userDao().getUsersFull().forEach {
                text.append("$it\n")
            }
            withContext(Dispatchers.Main) {
                usersTextView.text = text
            }
        }
    }
}