package com.sanstech.iswinning


class MarketType {
    var market_key = ""

    var market_type = 0

    var market_subtype = 0

    constructor()

    constructor(type: Int, subType: Int) {
        this.market_type = type
        this.market_subtype = subType
        setKey()
    }

    constructor(withUnderScored: String) {
        val splitted =
            withUnderScored.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.market_type = splitted[0].toInt()
        this.market_subtype = splitted[1].toInt()
    }

    fun getType(): Int {
        return market_type
    }

    fun setType(type: Int) {
        this.market_type = type
        setKey()
    }

    fun getSubType(): Int {
        return market_subtype
    }

    fun setSubType(subType: Int) {
        this.market_subtype = subType
        setKey()
    }

    fun equals(marketType: MarketType?): Boolean {
        return marketType != null && (marketType.market_type == this.market_type && marketType.market_subtype == this.market_subtype)
    }

    private fun setKey() {
        this.market_key = market_type.toString() + "_" + this.market_subtype
    }

    fun getKey(): String {
        if (this.market_key == null || market_key.isEmpty()) setKey()
        return market_key
    }
}
