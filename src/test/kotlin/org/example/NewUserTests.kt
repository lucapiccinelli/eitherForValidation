package org.example

import org.assertj.core.api.Assertions.assertThat
import org.example.model.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows

class NewUserTests{

    @Test
    internal fun `can use factory method to create a valid user`() {
        val expectedUser = NewUser("luca.piccinelli", NameOfAPerson("Luca", "Piccinelli"),
            jobDescription = "developer",
            password = Password.from("blablabla").get(),
            contacts = UserContacts(
                Email.from("luca.picci@gmail.com").get(),
                PhoneNumber.from("34765654").get()))

        val user = NewUser.with(
            username = expectedUser.username,
            firstname = expectedUser.name.firstname,
            lastname = expectedUser.name.lastname,
            password = expectedUser.password.value,
            jobDescription = expectedUser.jobDescription,
            email = expectedUser.contacts?.email?.value,
            phoneNumber = expectedUser.contacts?.phoneNumber?.value,
        )

        assertThat(user.get()).isEqualTo(expectedUser)
    }

    @TestFactory
    internal fun `can use factory to validate password`() = listOf(NewUser.with(
        username = "luca.piccinelli",
        firstname = "Luca",
        lastname = "Piccinelli",
        password = "bla",
        jobDescription = "developer",
        email = "luca.picci@gmail.com",
        phoneNumber = "34765654",),

        NewUser.with(
            username = "luca.piccinelli",
            firstname = "Luca",
            lastname = "Piccinelli",
            password = "blablabla",
            jobDescription = "developer",
            email = "luca.picci@gmail.com",
            phoneNumber = "xxx",
        ),

        NewUser.with(
            username = "luca.piccinelli",
            firstname = "Luca",
            lastname = "Piccinelli",
            password = "blablabla",
            jobDescription = "developer",
            email = "luca.picci",
            phoneNumber = "34765654",
        )
    ).map { user ->

        DynamicTest.dynamicTest("$user"){
            assertThrows<ResultException> { user.get() }
        }
    }
}