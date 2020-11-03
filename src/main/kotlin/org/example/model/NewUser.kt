package org.example.model

import org.example.Result

data class NewUser(
    val username: String,
    val name: NameOfAPerson,
    val password: Password,
    val jobDescription: String? = null,
    val contacts: UserContacts?
) {
    companion object{

        fun with(
            username: String,
            firstname: String,
            lastname: String,
            password: String,
            jobDescription: String? = null,
            email: String? = null,
            phoneNumber: String? = null
        ) : Result<NewUser> {

            val maybePassword = Password.from(password)
            val maybeContacts: Result<UserContacts> = Result.Ok(UserContacts())
                .let { maybeContacts -> parseEmail(email, maybeContacts) }
                .let { maybeContacts -> parsePhone(phoneNumber, maybeContacts)}

            return maybePassword
                .map { passwordObj -> userWithoutContacts(username, firstname, lastname, passwordObj, jobDescription) }
                .flatMap { user -> addContacts(maybeContacts, user) }
        }

        private fun addContacts(maybeContacts: Result<UserContacts>, user: NewUser) =
            maybeContacts.map { contacts -> user.copy(contacts = contacts) }

        private fun userWithoutContacts(username: String, firstname: String, lastname: String, passwordObj: Password, jobDescription: String?): NewUser {
            return NewUser(
                username = username,
                name = NameOfAPerson(firstname, lastname),
                password = passwordObj,
                jobDescription = jobDescription,
                contacts = null
            )
        }

        private fun parsePhone(phoneNumber: String?, maybeContacts: Result<UserContacts>): Result<UserContacts> =
            parse(phoneNumber, maybeContacts,
                { phoneNumberValue -> PhoneNumber.from(phoneNumberValue) },
                { contacts, number -> contacts.copy(phoneNumber = number) })

        private fun parseEmail(email: String?, maybeContacts: Result.Ok<UserContacts>): Result<UserContacts> =
            parse(email, maybeContacts,
                { emailValue -> Email.from(emailValue) },
                { contacts, emailObj -> contacts.copy(email = emailObj) })

        private fun <T> parse(
            value: String?,
            maybeContacts: Result<UserContacts>,
            parser: (String) -> Result<T>,
            howToCopy: (UserContacts, T) -> UserContacts): Result<UserContacts> = value
                ?.let(parser)
                ?.flatMap { contact -> maybeContacts.map { howToCopy(it, contact) } }
                ?: maybeContacts

    }
}