package org.example

import org.example.model.NewUser

fun main(args: Array<String>) {

    val user: Result<NewUser> = NewUser.with(
        username = "luca.piccinelli",
        firstname = "Luca",
        lastname = "Piccinelli",
        password = "banane",
        jobDescription = "Developer",
        email = "luca.piccinelli@cgm.com",
        phoneNumber = "+39 34755555555"
    )

    when(user){
        is Result.Ok -> println("bene")
        is Result.Error -> println("male")
    }.safe

}