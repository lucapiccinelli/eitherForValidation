package org.example
import org.assertj.core.api.Assertions.assertThat
import org.example.model.*
import org.junit.jupiter.api.Test
class ApplicativeBuilderTests {
    data class Person(val isCustomer: Boolean, val id: Int, val name: String)
    val expected = Person(false, 4, "Foo")
    @Test
    fun `make Person`() {
        val person = Person(false, 4, "Foo")
        assertThat(person).isEqualTo(expected)
    }
    @Test
    fun `currying Person`() {
        val person =
                ::Person.curry() `@` false `@` 4 `@` "Foo"
        assertThat(person).isEqualTo(expected)
    }
    @Test
    fun `applicative Person`() {
        val result =
                map3(Result.pure(false), Result.pure(4), Result.pure("Foo"),
                        ::Person)
        val person = result.get()
        assertThat(person).isEqualTo(expected)
    }
    @Test
    fun `monoidal applicative Person`() {
        val result =
                (Result.pure(false) `**` Result.pure(4) `**` Result.pure("Foo"))
                        .map { x ->
                            val (a, b) = x
                            val (c, d) = a
                            Person(c, d, b)
                        }
        val person = result.get()
        assertThat(person).isEqualTo(expected)
    }
    @Test
    fun `applicative builder Person`() {
        val result =
                Result.pure(::Person.curry())
                        .`*`(Result.pure(false))
                        .`*`(Result.pure(4))
                        .`*`(Result.pure("Foo"))
        val person = result.get()
        assertThat(person).isEqualTo(expected)
    }
    @Test
    fun `map + applicative builder Person`() {
        val result =
                ::Person.curry()
                        .`$`(Result.pure(false))
                        .`*`(Result.pure(4))
                        .`*`(Result.pure("Foo"))
        val person = result.get()
        assertThat(person).isEqualTo(expected)
    }
    @Test
    fun `NewUser example`() {
        val userResult: Result<NewUser> = BuildNewUser(
                username = "luca.piccinelli",
                firstname = "Luca",
                lastname = "Piccinelli",
                password = "banane",
                jobDescription = "Developer",
                email = "luca.piccigmail.com",
                phoneNumber = "+39 34755555555"
        )
        when (userResult) {
            is Result.Ok -> println("bene: ${userResult.value}")
            is Result.Error -> println("male: ${userResult.description}")
        }.safe
    }
    fun BuildNewUser(
            username: String,
            firstname: String,
            lastname: String,
            password: String,
            jobDescription: String? = null,
            email: String? = null,
            phoneNumber: String? = null
    ): Result<NewUser> {
        val contacts = ::UserContacts.curry()
                .`$`(Email.from(email))
                .`*`(PhoneNumber.from(phoneNumber))
        return ::NewUser.curry()
                .`$`(Result.pure(username))
                .`*`(Result.pure(NameOfAPerson(firstname, lastname)))
                .`*`(Password.from(password))
                .`*`(Result.pure(jobDescription))
                .`*`(contacts)
    }
    fun <A, B, Z> ((A, B) -> Z).curry(): (A) -> (B) -> Z = { a -> { b -> this(a, b) } }
    fun <A, B, C, Z> ((A, B, C) -> Z).curry(): (A) -> (B) -> (C) -> Z = { a -> { b -> { c -> this(a, b, c) } } }
    fun <A, B, C, D, E, Z> ((A, B, C, D, E) -> Z).curry(): (A) -> (B) -> (C) -> (D) -> (E) -> Z = { a -> { b -> { c -> { d -> { e -> this(a, b, c, d, e) } } } } }
    // aka apply function
    infix fun <A, B> ((A) -> B).`@`(a: A): B = this(a)
    // aka map in Functor
    infix fun <A, B> ((A) -> B).`$`(a: Result<A>): Result<B> = a.map(this)
    // aka ap in Applicative functor
    infix fun <A, B> Result<(A) -> B>.`*`(a: Result<A>): Result<B> = flatMap { fn -> a.map(fn) }
    // aka Monoidal Applicative
    infix fun <A, B> Result<A>.`**`(b: Result<B>): Result<Pair<A, B>> = flatMap { a -> b.map { bb -> a to bb } }
}