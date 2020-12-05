package org.example.model

import org.example.Result
import org.example.Result2

const val PHONENUMBER_REGEX = "^(\\+\\d{2})?\\s?(\\d\\s?)+\$"

data class PhoneNumber private constructor(override val value: String): UserContact {

    companion object {
        fun from(phoneNumberValue: String?): Result<PhoneNumber?> = phoneNumberValue
            ?.let {
                if(Regex(PHONENUMBER_REGEX).matches(phoneNumberValue))
                    Result.Ok(PhoneNumber(phoneNumberValue))
                    else Result.Error("$phoneNumberValue should match a valid phone number, but it doesn't")
            }
            ?: Result.Ok(null)

        fun from2(phoneNumberValue: String): Result2<PhoneNumber> =
            if(Regex(PHONENUMBER_REGEX).matches(phoneNumberValue))
                Result2.Ok(PhoneNumber(phoneNumberValue))
                else Result2.Error("$phoneNumberValue should match a valid phone number, but it doesn't")
            }
    }

}