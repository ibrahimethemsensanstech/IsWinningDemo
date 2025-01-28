package com.sanstech.iswinning

enum class CouponEventStatus(val value: String) {
    WAITING("COMPLETED"),
    LOST("LOST"),
    WON("WON"),
    LOSING("LOSING"),
    WINNING("WINNING");

    companion object {
        fun fromValue(value: String): CouponEventStatus? = values().find { it.value == value }
    }
}