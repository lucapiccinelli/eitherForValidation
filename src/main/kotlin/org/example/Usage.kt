package org.example

import org.example.model.Email
import org.example.model.PhoneNumber
import org.example.Maybe.Companion.toNullable
import org.example.Maybe.Companion.toMaybe

data class Usage(val name: String, val email: Email, val phoneNumber: PhoneNumber, val bla: String){

    companion object{
        fun new(email: String, phone: String, name: String, bla: String?): Result2<Usage> =
            ::Usage.curry()
                .pure(name)
                .map(Email.from2(email))
                .ap(PhoneNumber.from2(phone))
                .ap(bla.toResult("should not be null"))
                .downcast()
    }

}

data class Bla(val x: Int, val y: String, val z: Float){

    companion object {
        fun new(x: Int?, y: String?, z: Float): Bla? =
            ::Bla.curry()
                .map(x.toMaybe())
                .ap(y.toMaybe())
                .pure(z)
                .toNullable()
    }

}