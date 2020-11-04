package org.example.model

import org.example.Result
import org.example.map3

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
        ): Result<NewUser> = map3(
            Password.from(password),
            Email.from(email),
            PhoneNumber.from(phoneNumber)) { validPassword, validEmail, validPhone ->

            NewUser(
                username = username,
                name = NameOfAPerson(firstname, lastname),
                password = validPassword,
                jobDescription = jobDescription,
                contacts = UserContacts(validEmail, validPhone)
            )
        }
    }
}