package org.example


typealias NullableOf = Any?

sealed class Nullable<out T>:
    NullableOf(),
    MonadKind<NullableOf, T>,
    ApplicativeFunctorKind<NullableOf, T> {

    companion object{
        private fun <T> Kind<NullableOf, T>.downcast() = this as Nullable<T>

        fun <T> T?.toNullable(): ApplicativeFunctorKind<NullableOf, T> = when(this){
            null -> None
            else -> Some(this)
        }

        fun <T> Kind<NullableOf, T>.toNull(): T? = when(val me = downcast()){
            is Some<T> -> me.value
            is None -> null
        }
    }

    private data class Some<T>(val value: T) : Nullable<T>()
    private object None: Nullable<Nothing>()

    private inline fun <R> map(fn: (T) -> R): Nullable<R> = flatMap{ Some(fn(it)) }
    private inline fun <R> flatMap(fn: (T) -> Nullable<R>): Nullable<R> = when(this){
        is Some -> fn(value)
        is None -> None
    }
    private fun <R> ap(liftedFn: Nullable<((T) -> R)>): Nullable<R> = liftedFn.flatMap(this::map)

    override fun <R> flatMapK(fn: (T) -> MonadKind<NullableOf, R>): MonadKind<NullableOf, R> = flatMap { fn(it).downcast() }
    override fun <R> mapK(fn: (T) -> R): FunctorKind<NullableOf, R> = map(fn)
    override fun <R> apK(liftedFn: FunctorKind<NullableOf, (T) -> R>): FunctorKind<NullableOf, R> = ap(liftedFn.downcast())
}