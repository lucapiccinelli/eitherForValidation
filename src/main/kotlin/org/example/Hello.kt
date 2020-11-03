package org.example

import org.example.model.NewUser

fun main(args: Array<String>) {

    val userResult: Result<NewUser> = NewUser.with(
        username = "luca.piccinelli",
        firstname = "Luca",
        lastname = "Piccinelli",
        password = "banane",
        jobDescription = "Developer",
        email = "luca.picci@gmail.com",
        phoneNumber = "+39 34755555555"
    )

    when(userResult){
        is Result.Ok -> println("bene: ${userResult.value}")
        is Result.Error -> println("male: ${userResult.description}")
    }.safe

}