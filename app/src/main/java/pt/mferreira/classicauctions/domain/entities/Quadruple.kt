package pt.mferreira.classicauctions.domain.entities

data class Quadruple<T1, T2, T3, T4>(
	val first: T1,
	val second: T2,
	val third: T3,
	val fourth: T4
)