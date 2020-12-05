package org.example

import org.example.model.Email
import org.example.model.PhoneNumber

open class Result2Of
fun <T> Kind<Result2Of, T>.downcast() = this as Result2<T>

sealed class Result2<out T>: Result2Of(), MonadKind<Result2Of, T>, ApplicativeFunctorKind<Result2Of, T>{

    data class Ok<out T>(val value: T): Result2<T>()
    data class Error<out T>(val description: String): Result2<T>()

    inline fun <R> flatMap(fn: (T) -> Result2<R>): Result2<R> {
        return when(this){
            is Ok -> fn(value)
            is Error -> Error(description)
        }
    }

    inline fun <R> map(fn: (T) -> R): Result2<R> = flatMap { Ok(fn(it)) }

    fun <R> ap(fnLifted: Result2<(T) -> R>): Result2<R> = fnLifted.flatMap(this::map)

    override fun <R> mapK(fn: (T) -> R): FunctorKind<Result2Of, R> = map(fn)
    override fun <R> flatMap(fn: (T) -> MonadKind<Result2Of, R>): MonadKind<Result2Of, R> = flatMap(fn)
    override fun <R> apK(liftedFn: FunctorKind<Result2Of, (T) -> R>): FunctorKind<Result2Of, R> = ap(liftedFn.downcast())
}

data class Usage(val email: Email, val phoneNumber: PhoneNumber, val name: String){

    companion object{
        fun new(email: String, phone: String, name: String): Result2<Usage> =
            ::Usage.curry()
                .map(Email.from2(email))
                .ap(PhoneNumber.from2(phone))
                .ap(Result2.Ok(name))
                .downcast()
    }

}