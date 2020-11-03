package org.example.model

import org.example.Result

data class Password private constructor(val value: String){

    companion object {
        fun from(passwordValue: String): Result<Password> = if(passwordValue.length >= 6)
            Result.Ok(Password(passwordValue))
            else Result.Error("Password should be at least 6 characters length. It was ${passwordValue.length} characters")
    }

}