package org.example

class Applicative<out A, out B> (val a: A, val b: B) {
    inline fun <C> map(fn: (A, B) -> C): C = fn(a, b)

    inline fun <C> on(fn: A.(A) -> (B) -> C): C = a.fn(a)(b)

    inline fun <C> ap(fn: (A) -> (B) -> C): C = fn(a)(b)

    operator fun <C> invoke(fn: (A) -> (B) -> C): C = fn(a)(b)

    operator fun component1() = a
    operator fun component2() = b
}

//fun <A, B, C, D> curry(fn: (Applicative<A, B>, C) -> D) : (A) -> (B) -> (C) -> D = { fn. }