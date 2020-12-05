package org.example

infix fun <F, T, R> ((T) -> R).map(f: FunctorKind<F, T>): FunctorKind<F, R> = f.mapK(this)
infix fun <F, T, R> FunctorKind<F, ((T) -> R)>.ap(f: ApplicativeFunctorKind<F, T>): FunctorKind<F, R> = f.apK(this)