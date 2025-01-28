package com.sanstech.iswinning

data class Score(
    val status: Int? = null,
    val min: Int? = 0,
    val sec: Int? = null,
    val homeTeamScore: HashMap<String, Int>? = null,
    val awayTeamScore: HashMap<String, Int>? = null,
    val abbr: String? = null
){
    fun getHomeScore(): HashMap<String, Int> =
        homeTeamScore ?: HashMap()

    fun getAwayScore(): HashMap<String, Int> =
        awayTeamScore ?: HashMap()

    fun getNonNullStatus(): Int = status ?: MatchStatus.SCHEDULED.value

    fun getAbbrStatus(): String {
        return if (abbr == null || abbr.equals("ERT") || abbr.equals("Bitti")
            || abbr.equals("MS") || abbr.equals("UZMS") || abbr.equals("PS")
        )
            LiveScoreStatus.ENDED.value
        else if (abbr.equals("SCHEDULED"))
            LiveScoreStatus.SCHEDULED.value
        else
            LiveScoreStatus.ACTIVE.value
    }

    fun getScore(key: String): String? {
        if (getHomeScore()[key] == null || getAwayScore()[key] == null) {
            return null
        }
        return "${getHomeScore()[key]} - ${getAwayScore()[key]}"
    }

    fun getScore(isHome: Boolean, key: String): Int {
        val map: HashMap<String, Int> = if (isHome) getHomeScore() else getAwayScore()
        return map.get(key)?: 0
    }

    fun getScoreNullable(isHome: Boolean, key: String): String? {
        val map = if (isHome) getHomeScore() else getAwayScore()
        val score = map[key] ?: return null
        return score.toString()
    }

    fun hasScoreKey(key: String): Boolean {
        return getHomeScore()[key] != null && getAwayScore()[key] != null
    }
}
