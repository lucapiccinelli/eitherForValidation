package org.example

open class Result2Of
fun <T> Kind<Result2Of, T>.downcast() = this as Result2<T>

sealed class Result2<out T>: Result2Of(), MonadKind<Result2Of, T>, ApplicativeFunctorKind<Result2Of, T>{

    data class Ok<out T>(val value: T): Result2<T>()
    data class Error(val description: String, val prev: Error? = null): Result2<Nothing>(){
        override fun toString(): String = "${prev?.let { "${prev}," } ?: ""}$description"
    }

    inline fun <R> flatMap(fn: (T) -> Result2<R>): Result2<R> = when(this){
        is Ok -> fn(value)
        is Error -> this
    }

    inline fun <R> map(fn: (T) -> R): Result2<R> = flatMap { Ok(fn(it)) }

    fun <R> ap(fnLifted: Result2<(T) -> R>): Result2<R> = when(fnLifted){
        is Ok -> map(fnLifted.value)
        is Error -> when(this){
            is Ok -> fnLifted
            is Error -> Error(description, fnLifted)
        }
    }

    override fun <R> mapK(fn: (T) -> R): FunctorKind<Result2Of, R> = map(fn)
    override fun <R> flatMapK(fn: (T) -> MonadKind<Result2Of, R>): MonadKind<Result2Of, R> = flatMap { fn(it).downcast() }
    override fun <R> apK(liftedFn: FunctorKind<Result2Of, (T) -> R>): FunctorKind<Result2Of, R> = ap(liftedFn.downcast())
}

fun <T> T?.toResult(errorMessage: String) = when(this){
    null -> Result2.Error(errorMessage)
    else -> Result2.Ok(this)
}
