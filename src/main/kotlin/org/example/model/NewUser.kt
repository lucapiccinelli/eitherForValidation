package org.example.model

import org.example.Result
import org.example.exec
import org.example.on

data class NewUser(
    val username: String,
    val name: NameOfAPerson,
    val password: Password,
    val jobDescription: String? = null,
    val contacts: UserContacts?
) {
    companion object {
        fun with(
            username: String,
            firstname: String,
            lastname: String,
            password: String,
            jobDescription: String? = null,
            email: String? = null,
            phoneNumber: String? = null
        ): Result<NewUser> =

            Password.from(password) and
            Email.from(email) and
            PhoneNumber.from(phoneNumber) exec {

            params { x: Password ->
                   { y: Email? ->
                   { z: PhoneNumber? ->

                        NewUser(
                            username = username,
                            name = NameOfAPerson(firstname, lastname),
                            password = x,
                            jobDescription = jobDescription,
                            contacts = UserContacts(y, z)
                        )
                    }
                }
            }
        }
    }
}