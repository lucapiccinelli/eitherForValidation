package org.example

fun <A, B, R> ((A, B) -> R).curry() = { a: A -> { b: B -> this(a, b) } }
fun <A, B, C, R> ((A, B, C) -> R).curry() = { a: A -> { b: B -> { c: C -> this(a, b, c) } } }
fun <A, B, C, D, R> ((A, B, C, D) -> R).curry() = { a: A -> { b: B -> { c: C -> { d: D -> this(a, b, c, d) } } } }
fun <A, B, C, D, E, R> ((A, B, C, D, E) -> R).curry() = { a: A -> { b: B -> { c: C -> { d: D -> {e: E -> this(a, b, c, d, e) } } } } }