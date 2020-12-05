package org.example.model

import org.example.Result
import org.example.Result2

const val EMAIL_REGEX = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

data class Email private constructor (override val value: String): UserContact {
    companion object{
        fun from(value: String?): Result2<Email?> = value?.let {
            if (Regex(EMAIL_REGEX).matches(value))
                Result2.Ok(Email(value))
                else Result2.Error("$value doesn't match an email format")
        } ?: Result2.Ok(null)

        fun from2(value: String): Result2<Email> =
            if (Regex(EMAIL_REGEX).matches(value))
                Result2.Ok(Email(value))
            else Result2.Error("$value doesn't match an email format")
    }
}
