package org.example


typealias MayBeOf = Any?

class Maybe<out T> private constructor(private val value: T?):
    MayBeOf(),
    MonadKind<MayBeOf, T>,
    ApplicativeFunctorKind<MayBeOf, T> {

    companion object{
        private fun <T> Kind<MayBeOf, T>.downcast() = this as Maybe<T>

        fun <T> T?.toMaybe(): ApplicativeFunctorKind<MayBeOf, T> = Maybe(this)
        fun <T> Kind<MayBeOf, T>.toNullable(): T? = downcast().value
    }

    private inline fun <R> map(fn: (T) -> R): Maybe<R> = flatMap{ Maybe(fn(it)) }
    private inline fun <R> flatMap(fn: (T) -> Maybe<R>): Maybe<R> = when(value){
        null -> Maybe(null)
        else -> fn(value)
    }
    private fun <R> ap(liftedFn: Maybe<((T) -> R)>): Maybe<R> = liftedFn.flatMap(this::map)

    override fun <R> flatMapK(fn: (T) -> MonadKind<MayBeOf, R>): MonadKind<MayBeOf, R> = flatMap { fn(it).downcast() }
    override fun <R> mapK(fn: (T) -> R): FunctorKind<MayBeOf, R> = map(fn)
    override fun <R> apK(liftedFn: FunctorKind<MayBeOf, (T) -> R>): FunctorKind<MayBeOf, R> = ap(liftedFn.downcast())
}