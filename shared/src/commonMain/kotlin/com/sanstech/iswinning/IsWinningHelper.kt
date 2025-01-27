package com.sanstech.iswinning

class IsWinningHelper {
    private var marketType: MarketType? = null
    private var outcomeNo = 0
    private var score: Score? = null
    private var specialOddValue = 0f

    fun setMarketType(marketType: Int, marketSubType: Int): IsWinningHelper {
        this.marketType = MarketType(marketType, marketSubType)
        return this
    }

    fun setSport(sportType: String?): IsWinningHelper {
        return this
    }

    fun setOutcomeNo(outcomeNo: Int): IsWinningHelper {
        this.outcomeNo = outcomeNo
        return this
    }

    fun setScore(score: Score?): IsWinningHelper {
        this.score = score
        return this
    }

    fun setSpecialOddValue(specialOddValue: Float): IsWinningHelper {
        this.specialOddValue = specialOddValue
        return this
    }

    // Maç oynanmadan veya yarıda kalarak bitmiş mi
    fun isCanceled(status: Int): Boolean {
        return when (status) {
            MatchStatus.INTERRUPTED.value,
            MatchStatus.POSTPONED.value,
            MatchStatus.CANCELLED.value,
            MatchStatus.CLOSED.value,
            MatchStatus.ABANDONED.value,
            MatchStatus.SUDDEN_DEATH.value -> true

            else -> false
        }
    }

    val status: String
        get() {
            if (marketType == null || score == null || score!!.getHomeScore() == null || score!!.getHomeScore() == null || score!!.getHomeScore().size == 0 || score!!.getHomeScore().size == 0 ||
                isCanceled(score!!.getNonNullStatus())
            ) return CouponEventStatus.WAITING.value

            when (marketType!!.market_key) {
                "1_1", "4_4", "2_100", "4_12", "1_16", "4_3", "2_163" -> return if (getResultFor3Choices(
                        ScoreType.REGULAR.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "1_10", "4_114", "1_2", "4_71", "4_286", "2_113", "2_187", "4_775", "2_633", "2_9", "4_56", "2_637", "4_777" -> return if (getResultFor2Choices(
                        "c"
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_36", "4_321" -> return if (resultForSecondHalf) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_88", "4_47", "2_621", "4_606" -> return if (getResultFor3Choices(ScoreType.HALF_TIME.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_115", "4_794" -> return if (getResultFor2Choices(ScoreType.HALF_TIME.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_660", "4_673" -> return if (getResultFor3Choices("q1")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_101", "4_14", "2_191", "4_20", "4_778" -> return if (getUnderOverForScoreTypeResult(
                        ScoreType.REGULAR.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_217", "4_18" -> return if (underOverForSetsTotalResult) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_114", "4_140" -> return if (getUnderOverForScoreTypeResult("c")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_603", "4_203" -> return if (isSideUnderOverForScoreType(
                        true,
                        ScoreType.REGULAR.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_619", "4_615" -> return if (isSideUnderOverForScoreType(
                        true,
                        "c"
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_604", "4_207" -> return if (isSideUnderOverForScoreType(
                        false,
                        ScoreType.REGULAR.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_620", "4_616" -> return if (isSideUnderOverForScoreType(
                        false,
                        "c"
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_60", "4_63", "2_116", "4_64", "2_828", "4_806" -> return if (getUnderOverForScoreTypeResult(
                        ScoreType.HALF_TIME.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_90", "2_111" -> return if (iYMSResult) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_186", "2_104" -> return if (isDraw) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_136", "2_91", "2_184", "4_133" -> return if (isOddEven(ScoreType.REGULAR.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_131", "2_89" -> return if (isBothScored(ScoreType.REGULAR.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_643" -> return if (isWinTo0(true)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_644" -> return if (isWinTo0(false)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_6", "4_199" -> return if (isMoreGoal) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_92", "4_129" -> return if (getDoubleChance(ScoreType.REGULAR.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_77", "4_625", "2_185", "4_633" -> return if (getDoubleChance(ScoreType.HALF_TIME.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_7" -> return if (getMatchBetAndUnderOverResult(ScoreType.REGULAR.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_622" -> return if (comboBetWithETResult) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_43" -> return if (teamScoredResult(false)) CouponEventStatus.LOSING.value else CouponEventStatus.WINNING.value
                "2_44" -> return if (teamScoredResult(true)) CouponEventStatus.LOSING.value else CouponEventStatus.WINNING.value
                "2_45" -> return if (whichTeamScored()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_46" -> return if (halfTotalGoalResult(true)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_262" -> return if (halfTotalGoalLiveResult(true)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_47" -> return if (halfTotalGoalResult(false)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_69" -> return if (teamWinBothHalfResult(true)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_72" -> return if (teamWinBothHalfResult(false)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_4" -> return if (totalGoalResult()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_86" -> return if (matchScoreResult()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_252" -> return if (matchScoreLiveResult()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_220", "4_277", "2_172", "4_276" -> {
                    val res = volleyballMatchScoreResult() ?: return CouponEventStatus.WAITING.value
                    return if (res) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                }

                "2_171", "4_274" -> {
                    val result3Set =
                        tennisMatchScoreResult() ?: return CouponEventStatus.WAITING.value
                    return if (result3Set) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                }

                "2_84" -> return if (teamMoreScoredHalfResult(true)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_85" -> return if (teamMoreScoredHalfResult(false)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_107" -> return if (mostScoredQuarterResult()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_105" -> return if (winningMarginsResult()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_11" -> return if (soccerWinningMarginsResult()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_166" -> return if (getResultFor2Choices("s1")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_169" -> return if (getResultFor2Choices("s2")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_57", "4_716" -> return if (setResultWithSpecialOddValue) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_647", "4_607" -> return if (teamScoredResult(true)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_648", "4_608" -> return if (teamScoredResult(false)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_146" -> return if (getUnderOverForScoreTypeResult("s1")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_157" -> return if (getUnderOverForScoreTypeResult("s2")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_167" -> return if (getUnderOverForScoreTypeResult("s3")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_171" -> return if (getUnderOverForScoreTypeResult("s4")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_190" -> return if (getUnderOverForScoreTypeResult("s5")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_703" -> return if (getUnderOverForScoreTypeResult("s6")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_704" -> return if (getUnderOverForScoreTypeResult("s7")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_231", "4_239" -> return if (totalPointForSetsResult) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_650", "4_622" -> return if (getUnderOverForSideSetsTotalResult(true)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_651", "4_623" -> return if (getUnderOverForSideSetsTotalResult(false)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_168", "4_191" -> return if (underOverForSetsTotalResult) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_649", "4_613" -> return if (totalPointForSetsResult) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_167", "4_122" -> return if (setsNumberFor3SetGameResult) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_170", "4_124" -> return if (setsNumberFor5SetGameResult) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_652", "4_609" -> return if (hasTieBreakResult()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_67", "4_215" -> return if (cornerNumberResult) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_48", "4_211" -> return if (getUnderOverForScoreTypeResult(ScoreType.CORNER.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_52", "4_210" -> return if (getResultFor3Choices(ScoreType.CORNER.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_17" -> return if (hasRedCardResult) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_658", "4_792" -> return if (totalCardUnderOverResult) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_700" -> return if (underOverAndBothScored()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_696", "4_706", "2_745", "4_741", "2_160", "4_62" -> {
                    val result =
                        drawNoBetResult(ScoreType.REGULAR.value)
                            ?: return CouponEventStatus.WAITING.value
                    return if (result) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                }

                "4_77" -> {
                    val result2 =
                        drawNoBetResult(ScoreType.HALF_TIME.value)
                            ?: return CouponEventStatus.WAITING.value
                    return if (result2) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                }

                "2_698", "4_708" -> return if (matchBetAndBothScored()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_699", "4_709" -> return if (firstHalfAndBothScored()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_701", "4_711" -> return if (oddEvenForTotalGames()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_702", "4_712" -> return if (totalPointForSetsResult) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_703", "4_713" -> return if (matchBetAndUnderOverForSets()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_704", "4_714" -> return if (isWinTo0ForAnySet) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_705", "4_715" -> return if (firstSetAndMatchBetResult()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_707" -> return if (tennisSetScoreResult(1)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_717" -> return if (tennisSetScoreResult(specialOddValue.toInt())) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_709", "4_719" -> return if (sideScoredOnly1(true)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_710", "4_720" -> return if (sideScoredOnly1(false)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_711" -> return if (teamScoredResult(true)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_712" -> return if (teamScoredResult(false)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_713", "4_723" -> return if (totalSetsResult()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_714" -> return if (setResult) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_724" -> return if (setResultWithSpecialOddValue) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_715", "4_725" -> return if (sideScoredOnly1(true)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_716", "2_726" -> return if (sideScoredOnly1(false)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_717", "4_728" -> return if (matchHalfTimeScoreResult()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_718" -> return if (firstHalfAndMatchScoreResult()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_719" -> return if (isOddEven(ScoreType.HALF_TIME.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_720", "4_735" -> return if (isBothScored(ScoreType.HALF_TIME.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_722", "4_737" -> return if (isSideUnderOverForScoreType(
                        true,
                        ScoreType.HALF_TIME.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_723", "4_738" -> return if (isSideUnderOverForScoreType(
                        false,
                        ScoreType.HALF_TIME.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_724", "4_739" -> return if (getMatchBetAndUnderOverResult(ScoreType.HALF_TIME.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_27" -> return if (getResultFor3Choices(ScoreType.EXTRA_TIME.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_54" -> return if (getUnderOverForScoreTypeResult(ScoreType.EXTRA_TIME.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "1_21", "4_1" -> return if (getResultFor2Choices(ScoreType.REGULAR.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_601", "4_120" -> return if (getResultFor2Choices(ScoreType.CURRENT.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_161", "4_9" -> return if (getResultFor2Choices(ScoreType.REGULAR.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_617", "4_639" -> return if (soccerWinningMarginsResult()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_126" -> return if (getResultFor3Choices("p1")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_137" -> return if (getResultFor3Choices("p2")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_37" -> return if (periodResultWithSpecialOddValue) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_127" -> return if (getResultFor3Choices("p3")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_165", "2_164", "4_21" -> return if (getUnderOverForScoreTypeResult(ScoreType.REGULAR.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_616" -> return if (isSideUnderOverForScoreType(
                        false,
                        ScoreType.REGULAR.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_135" -> return if (mostScoredPeriodResult()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_157", "4_619" -> return if (isBothScored(ScoreType.REGULAR.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_158", "4_135" -> return if (isOddEven(ScoreType.REGULAR.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_128" -> return if (getDoubleChance(ScoreType.REGULAR.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_737", "4_727" -> return if (isOddEven(ScoreType.CORNER.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_822", "4_731" -> return if (isSideUnderOverForScoreType(
                        true,
                        ScoreType.CORNER.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_823", "4_732" -> return if (isSideUnderOverForScoreType(
                        false,
                        ScoreType.CORNER.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "4_733" -> return if (getTotalCardUnderOverResult(true)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_734" -> return if (getTotalCardUnderOverResult(false)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_727" -> return if (teamWonLeastHalfResult(true)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_728" -> return if (teamWonLeastHalfResult(false)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_729", "2_730" -> return if (underOverForEachHalf) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_731", "2_732" -> return if (getResultFor3Choices(ScoreType.REGULAR.value) || isSideUnderOverForScoreType(
                        true,
                        ScoreType.REGULAR.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_733", "2_734" -> return if (getResultFor3Choices(ScoreType.REGULAR.value) || isSideUnderOverForScoreType(
                        false,
                        ScoreType.REGULAR.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_735", "2_736" -> return if (getResultFor3Choices(ScoreType.REGULAR.value) || getUnderOverForScoreTypeResult(
                        ScoreType.REGULAR.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "4_718", "4_743", "4_744", "4_745", "4_746" -> return if (getCorrectScoreForTennis(
                        ScoreType.SET_GAME_SCORE.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_755", "4_750" -> return if (getResultFor3Choices("q2")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_756", "4_751" -> return if (getResultFor3Choices("q3")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_757", "4_752" -> return if (getResultFor3Choices("q4")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_746" -> return if (isSideUnderOverForScoreType(
                        true,
                        ScoreType.HALF_TIME.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_747" -> return if (isSideUnderOverForScoreType(
                        false,
                        ScoreType.HALF_TIME.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_748" -> return if (mostScoredHalfResult()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_749", "4_742" -> return if (getUnderOverForScoreTypeResult("q1")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_758", "4_747" -> return if (getUnderOverForScoreTypeResult("q2")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_759", "4_748" -> return if (getUnderOverForScoreTypeResult("q3")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_760", "4_749" -> return if (getUnderOverForScoreTypeResult("q4")) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_750" -> return if (totalScore180PointRangeResult()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_769" -> return if (totalScore250PointRangeResult()) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_821", "4_790" -> return if (isBothScoredSecondHalf) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_824", "4_791" -> return if (seeMoreCardsResult) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_825", "4_803" -> return if (isSideUnderOverForScoreType(
                        true,
                        ScoreType.REGULAR.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_826", "4_804" -> return if (isSideUnderOverForScoreType(
                        false,
                        ScoreType.REGULAR.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "2_827", "4_805" -> return if (getResultFor3Choices(ScoreType.HALF_TIME.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "2_829", "4_807" -> return if (getResultFor2Choices(ScoreType.HALF_TIME.value)) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value
                "4_808" -> return if (isSideUnderOverForScoreType(
                        true,
                        ScoreType.HALF_TIME.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                "4_809" -> return if (isSideUnderOverForScoreType(
                        false,
                        ScoreType.HALF_TIME.value
                    )
                ) CouponEventStatus.WINNING.value else CouponEventStatus.LOSING.value

                else -> return CouponEventStatus.WAITING.value
            }
        }

    private val seeMoreCardsResult: Boolean
        get() {
            val homeTeamTotalCard =
                score!!.getScore(true, ScoreType.RED_CARD.value) + score!!.getScore(
                    true,
                    ScoreType.YELLOW_CARD.value
                )
            val awayTeamTotalCard =
                score!!.getScore(false, ScoreType.RED_CARD.value) + score!!.getScore(
                    false,
                    ScoreType.YELLOW_CARD.value
                )

            when (outcomeNo) {
                1 -> return homeTeamTotalCard > awayTeamTotalCard
                2 -> return homeTeamTotalCard < awayTeamTotalCard
            }
            return false
        }

    private fun getCorrectScoreForTennis(scoreKey: String): Boolean {
        var scoreKey = scoreKey
        if (scoreKey == ScoreType.HALF_TIME.value && (score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null || score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)) {
            scoreKey = ScoreType.CURRENT.value
        }
        val homeScore = score!!.getScore(true, scoreKey)
        val awayScore = score!!.getScore(false, scoreKey)
        when (outcomeNo) {
            1 -> return homeScore == 0
            2 -> return homeScore == 15
            3 -> return homeScore == 30
            4 -> return homeScore == 40
            5 -> return awayScore == 0
            6 -> return awayScore == 15
            7 -> return awayScore == 30
            8 -> return awayScore == 40
        }
        return false
    }

    private fun totalSetsResult(): Boolean {
        val totalScore =
            score!!.getScore(true, ScoreType.CURRENT.value) + score!!.getScore(
                false,
                ScoreType.CURRENT.value
            )

        when (outcomeNo) {
            1 -> return totalScore == 3
            2 -> return totalScore == 4
            3 -> return totalScore == 5
        }
        return false
    }

    private fun sideScoredOnly1(isHome: Boolean): Boolean {
        val teamScore = score!!.getScore(isHome, ScoreType.CURRENT.value)

        when (outcomeNo) {
            1 -> return teamScore == 1
            2 -> return teamScore != 1
        }

        return false
    }

    private fun tennisSetScoreResult(setNumber: Int): Boolean {
        val homeScore = score!!.getScore(true, "s$setNumber")
        val awayScore = score!!.getScore(false, "s$setNumber")
        val strScore = "$homeScore:$awayScore"

        val scoresList = ArrayList(
            mutableListOf<String?>(
                "6:0", "6:1", "6:2", "6:3", "6:4", "7:5", "7:6", "0:6", "1:6", "2:6",
                "3:6", "4:6", "5:7", "6:7"
            )
        )

        return scoresList[outcomeNo - 1] != null && scoresList[outcomeNo - 1] == strScore
    }

    private fun firstSetAndMatchBetResult(): Boolean {
        val homeScore = score!!.getScore(true, ScoreType.CURRENT.value)
        val awayScore = score!!.getScore(false, ScoreType.CURRENT.value)
        val homeSet1Score = score!!.getScore(true, ScoreType.SET1.value)
        val awaySet1Score = score!!.getScore(false, ScoreType.SET1.value)

        when (outcomeNo) {
            1 -> return homeSet1Score > awaySet1Score && homeScore > awayScore
            2 -> return homeSet1Score < awaySet1Score && homeScore > awayScore
            3 -> return homeSet1Score > awaySet1Score && homeScore < awayScore
            4 -> return homeSet1Score < awaySet1Score && homeScore < awayScore
        }
        return false
    }

    private val isWinTo0ForAnySet: Boolean
        get() {
            var isWinTo0 = false
            for (i in 1..7) {
                val homeSetScore = score!!.getScore(true, "s$i")
                val awaySetScore = score!!.getScore(false, "s$i")

                if (homeSetScore > 0 || awaySetScore > 0) {
                    if (homeSetScore == 0 || awaySetScore == 0) {
                        isWinTo0 = true
                        break
                    }
                }
            }

            when (outcomeNo) {
                1 -> return isWinTo0
                2 -> return !isWinTo0
            }
            return false
        }

    private fun matchBetAndUnderOverForSets(): Boolean {
        val homeTotal = (score!!.getScore(true, "s1")
                + score!!.getScore(true, "s2")
                + score!!.getScore(true, "s3")
                + score!!.getScore(true, "s4")
                + score!!.getScore(true, "s5")
                + score!!.getScore(true, "s6")
                + score!!.getScore(true, "s7"))
        val awayTotal = (score!!.getScore(false, "s1")
                + score!!.getScore(false, "s2")
                + score!!.getScore(false, "s3")
                + score!!.getScore(false, "s4")
                + score!!.getScore(false, "s5")
                + score!!.getScore(false, "s6")
                + score!!.getScore(false, "s7"))

        val total = homeTotal + awayTotal

        val homeScore = score!!.getScore(true, ScoreType.CURRENT.value)
        val awayScore = score!!.getScore(false, ScoreType.CURRENT.value)

        when (outcomeNo) {
            1 -> return homeScore > awayScore && total > specialOddValue
            2 -> return homeScore < awayScore && total > specialOddValue
            3 -> return homeScore > awayScore && total < specialOddValue
            4 -> return homeScore < awayScore && total < specialOddValue
        }
        return false
    }

    private fun firstHalfAndBothScored(): Boolean {
        val homeScore = score!!.getScore(true, ScoreType.HALF_TIME.value)
        val awayScore = score!!.getScore(false, ScoreType.HALF_TIME.value)
        val htKey = if ((score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null ||
                    score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)
        ) ScoreType.CURRENT.value else ScoreType.HALF_TIME.value
        val homeHTScore = score!!.getScore(true, htKey)
        val awayHTScore = score!!.getScore(false, htKey)
        when (outcomeNo) {
            1 -> return homeHTScore + specialOddValue > awayHTScore && (homeScore > 0 && awayScore > 0)
            2 -> return homeHTScore + specialOddValue > awayHTScore && !(homeScore > 0 && awayScore > 0)
            3 -> return homeHTScore + specialOddValue == awayHTScore.toFloat() && (homeScore > 0 && awayScore > 0)
            4 -> return homeHTScore + specialOddValue == awayHTScore.toFloat() && !(homeScore > 0 && awayScore > 0)
            5 -> return homeHTScore + specialOddValue < awayHTScore && (homeScore > 0 && awayScore > 0)
            6 -> return homeHTScore + specialOddValue < awayHTScore && !(homeScore > 0 && awayScore > 0)
        }
        return true
    }

    private fun underOverAndBothScored(): Boolean {
        val homeScore = score!!.getScore(true, ScoreType.REGULAR.value)
        val awayScore = score!!.getScore(false, ScoreType.REGULAR.value)
        when (outcomeNo) {
            1 -> return homeScore + awayScore < specialOddValue && (homeScore > 0 && awayScore > 0)
            2 -> return homeScore + awayScore > specialOddValue && (homeScore > 0 && awayScore > 0)
            3 -> return homeScore + awayScore < specialOddValue && !(homeScore > 0 && awayScore > 0)
            4 -> return homeScore + awayScore > specialOddValue && !(homeScore > 0 && awayScore > 0)
        }
        return true
    }

    private fun matchBetAndBothScored(): Boolean {
        val homeScore = score!!.getScore(true, ScoreType.REGULAR.value)
        val awayScore = score!!.getScore(false, ScoreType.REGULAR.value)
        when (outcomeNo) {
            1 -> return homeScore + specialOddValue > awayScore && (homeScore > 0 && awayScore > 0)
            2 -> return homeScore + specialOddValue > awayScore && !(homeScore > 0 && awayScore > 0)
            3 -> return homeScore + specialOddValue == awayScore.toFloat() && (homeScore > 0 && awayScore > 0)
            4 -> return homeScore + specialOddValue == awayScore.toFloat() && !(homeScore > 0 && awayScore > 0)
            5 -> return homeScore + specialOddValue < awayScore && (homeScore > 0 && awayScore > 0)
            6 -> return homeScore + specialOddValue < awayScore && !(homeScore > 0 && awayScore > 0)
        }
        return true
    }

    private fun drawNoBetResult(scoreKey: String): Boolean? {
        var scoreKey = scoreKey
        if (scoreKey == ScoreType.HALF_TIME.value && (score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null
                    || score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)
        ) {
            scoreKey = ScoreType.CURRENT.value
        }
        val homeScore = score!!.getScore(true, scoreKey)
        val awayScore = score!!.getScore(false, scoreKey)
        if (homeScore == awayScore) return null
        when (outcomeNo) {
            1 -> return homeScore + specialOddValue > awayScore
            2 -> return homeScore + specialOddValue < awayScore
        }
        return null
    }

    private fun getResultFor3Choices(scoreKey: String): Boolean {
        var scoreKey = scoreKey
        if (scoreKey == ScoreType.HALF_TIME.value
            && (score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null
                    || score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null
                    )
        ) {
            scoreKey = ScoreType.CURRENT.value
        }
        val homeScore = score!!.getScore(true, scoreKey)
        val awayScore = score!!.getScore(false, scoreKey)
        when (outcomeNo) {
            1 -> return homeScore + specialOddValue > awayScore
            2 -> return homeScore + specialOddValue == awayScore.toFloat()
            3 -> return homeScore + specialOddValue < awayScore
        }
        return false
    }

    private fun getDoubleChance(scoreKey: String): Boolean {
        var scoreKey = scoreKey
        if (scoreKey == ScoreType.HALF_TIME.value && (score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null || score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)) {
            scoreKey = "c"
        }
        val homeScore = score!!.getScore(true, scoreKey)
        val awayScore = score!!.getScore(false, scoreKey)
        when (outcomeNo) {
            1 -> return homeScore > awayScore || homeScore == awayScore
            2 -> return homeScore != awayScore
            3 -> return homeScore < awayScore || homeScore == awayScore
        }
        return false
    }

    private fun getResultFor2Choices(scoreKey: String): Boolean {
        var scoreKey = scoreKey
        if (scoreKey == ScoreType.HALF_TIME.value && (score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null || score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)) {
            scoreKey = "c"
        }
        val homeScore = score!!.getScore(true, scoreKey)
        val awayScore = score!!.getScore(false, scoreKey)
        when (outcomeNo) {
            1 -> return homeScore + specialOddValue > awayScore
            2 -> return homeScore + specialOddValue < awayScore
        }
        return false
    }

    private fun getUnderOverForScoreTypeResult(scoreKey: String): Boolean {
        var scoreKey = scoreKey
        if (scoreKey == ScoreType.HALF_TIME.value && (score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null || score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)) {
            scoreKey = ScoreType.CURRENT.value
        }
        val homeScore = score!!.getScore(true, scoreKey)
        val awayScore = score!!.getScore(false, scoreKey)
        when (outcomeNo) {
            1 -> return homeScore + awayScore < specialOddValue
            2 -> return homeScore + awayScore > specialOddValue
        }
        return false
    }

    private val underOverForEachHalf: Boolean
        get() {
            val htKey =
                if ((score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null || score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)
                ) ScoreType.CURRENT.value else ScoreType.HALF_TIME.value
            val homeFHScore = score!!.getScore(true, htKey)
            val awayFHScore = score!!.getScore(false, htKey)
            val firstHalfScore = homeFHScore + awayFHScore

            val t =
                score!!.getScore(true, ScoreType.REGULAR.value) + score!!.getScore(
                    false,
                    ScoreType.REGULAR.value
                )
            val secondHalfScore = t - firstHalfScore


            when (outcomeNo) {
                1 -> return firstHalfScore < specialOddValue && secondHalfScore < specialOddValue
                2 -> return firstHalfScore > specialOddValue && secondHalfScore > specialOddValue
            }
            return false
        }

    private fun isSideUnderOverForScoreType(isHome: Boolean, key: String): Boolean {
        var key = key
        if (key == ScoreType.HALF_TIME.value && !score!!.hasScoreKey(ScoreType.HALF_TIME.value)) {
            key = ScoreType.CURRENT.value
        }

        val teamScore = score!!.getScore(isHome, key)
        when (outcomeNo) {
            1 -> return teamScore < specialOddValue
            2 -> return teamScore > specialOddValue
        }
        return false
    }

    private fun isBothScored(key: String): Boolean {
        var key = key
        if (key == ScoreType.HALF_TIME.value && !score!!.hasScoreKey(ScoreType.HALF_TIME.value)) {
            key = ScoreType.CURRENT.value
        }

        val homeScore = score!!.getScore(true, key)
        val awayScore = score!!.getScore(false, key)
        return if (outcomeNo == 1) {
            homeScore > 0 && awayScore > 0
        } else {
            homeScore == 0 || awayScore == 0
        }
    }

    private fun mostScoredPeriodResult(): Boolean {
        val p1 = score!!.getScore(true, "p1") + score!!.getScore(false, "p1")
        val p2 = score!!.getScore(true, "p2") + score!!.getScore(false, "p2")
        val p3 = score!!.getScore(true, "p3") + score!!.getScore(false, "p3")

        when (outcomeNo) {
            1 -> return p1 > p2 && p1 > p3
            2 -> return p2 > p1 && p2 > p3
            3 -> return p3 > p1 && p3 > p2
            4 -> return (p1 == p2 && p1 > p3) || (p1 == p3 && p1 > p2) || (p2 == p3 && p2 > p1) || (p1 == p2 && p1 == p3)
        }
        return false
    }

    private val isMoreGoal: Boolean
        get() {
            val homeScore = score!!.getScore(true, ScoreType.REGULAR.value)
            val awayScore = score!!.getScore(false, ScoreType.REGULAR.value)
            val htKey =
                if ((score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null || score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)
                ) "c" else ScoreType.HALF_TIME.value

            val homeHTScore = score!!.getScore(true, htKey)
            val awayHTScore = score!!.getScore(false, htKey)

            val firstHalf = homeHTScore + awayHTScore
            val secondHalf = homeScore + awayScore - firstHalf

            when (outcomeNo) {
                1 -> return firstHalf > secondHalf
                2 -> return firstHalf == secondHalf
                3 -> return firstHalf < secondHalf
            }
            return false
        }

    private val resultForSecondHalf: Boolean
        get() {
            val homeScore = score!!.getScore(true, ScoreType.REGULAR.value)
            val awayScore = score!!.getScore(false, ScoreType.REGULAR.value)
            val htKey =
                if ((score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null || score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)
                ) "c" else ScoreType.HALF_TIME.value
            val homeHTScore = score!!.getScore(true, htKey)
            val awayHTScore = score!!.getScore(false, htKey)
            val homeSHScore = homeScore - homeHTScore
            val awaySHScore = awayScore - awayHTScore
            when (outcomeNo) {
                1 -> return homeSHScore > awaySHScore
                2 -> return homeSHScore == awaySHScore
                3 -> return homeSHScore < awaySHScore
            }
            return false
        }

    private val isDraw: Boolean
        get() {
            val homeScore = score!!.getScore(true, ScoreType.REGULAR.value)
            val awayScore = score!!.getScore(false, ScoreType.REGULAR.value)
            return if (outcomeNo == 1) {
                homeScore == awayScore
            } else {
                homeScore != awayScore
            }
        }

    private fun isOddEven(key: String): Boolean {
        var key = key
        if (key == ScoreType.HALF_TIME.value && !score!!.hasScoreKey(ScoreType.HALF_TIME.value)) {
            key = ScoreType.CURRENT.value
        }

        val homeScore = score!!.getScore(true, key)
        val awayScore = score!!.getScore(false, key)
        return if (outcomeNo == 1) {
            (homeScore + awayScore) % 2 == 1
        } else {
            (homeScore + awayScore) % 2 == 0
        }
    }

    private fun isWinTo0(isHome: Boolean): Boolean {
        val score1 = score!!.getScore(isHome, ScoreType.REGULAR.value)
        val score2 = score!!.getScore(!isHome, ScoreType.REGULAR.value)

        when (outcomeNo) {
            1 -> return score1 > 0 && score2 == 0
            2 -> return score1 == 0 || score2 != 0
        }
        return false
    }

    private fun getMatchBetAndUnderOverResult(key: String): Boolean {
        var key = key
        if (key == ScoreType.HALF_TIME.value && !score!!.hasScoreKey(ScoreType.HALF_TIME.value)) {
            key = ScoreType.CURRENT.value
        }

        val homeScore = score!!.getScore(true, key)
        val awayScore = score!!.getScore(false, key)
        when (outcomeNo) {
            1 -> return homeScore > awayScore && homeScore + awayScore < specialOddValue
            2 -> return homeScore == awayScore && homeScore + awayScore < specialOddValue
            3 -> return homeScore < awayScore && homeScore + awayScore < specialOddValue
            4 -> return homeScore > awayScore && homeScore + awayScore > specialOddValue
            5 -> return homeScore == awayScore && homeScore + awayScore > specialOddValue
            6 -> return homeScore < awayScore && homeScore + awayScore > specialOddValue
        }
        return false
    }

    private val comboBetWithETResult: Boolean
        get() {
            val homeScore = score!!.getScore(true, "c")
            val awayScore = score!!.getScore(false, "c")
            when (outcomeNo) {
                1 -> return homeScore > awayScore && homeScore + awayScore < specialOddValue
                2 -> return homeScore < awayScore && homeScore + awayScore < specialOddValue
                3 -> return homeScore > awayScore && homeScore + awayScore > specialOddValue
                4 -> return homeScore < awayScore && homeScore + awayScore > specialOddValue
            }
            return false
        }

    private val iYMSResult: Boolean
        get() {
            if (score!!.status == null) return false
            if (score!!.status == MatchStatus.FIRST_HALF.value) {
                return calculateFirstHalfResult()
            } else if (score!!.status == MatchStatus.SECOND_HALF.value) {
                return calculateSecondHalfResult()
            }
            return false
        }

    private fun calculateSecondHalfResult(): Boolean {
        val homeScore = score!!.getScore(true, ScoreType.REGULAR.value)
        val awayScore = score!!.getScore(false, ScoreType.REGULAR.value)
        val htKey =
            if ((score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null || score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)) "c" else ScoreType.HALF_TIME.value
        val homeHTScore = score!!.getScore(true, htKey)
        val awayHTScore = score!!.getScore(false, htKey)
        when (outcomeNo) {
            1 -> return homeHTScore > awayHTScore && homeScore > awayScore
            2 -> return homeHTScore > awayHTScore && homeScore == awayScore
            3 -> return homeHTScore > awayHTScore && homeScore < awayScore
            4 -> return homeHTScore == awayHTScore && homeScore > awayScore
            5 -> return homeHTScore == awayHTScore && homeScore == awayScore
            6 -> return homeHTScore == awayHTScore && homeScore < awayScore
            7 -> return homeHTScore < awayHTScore && homeScore > awayScore
            8 -> return homeHTScore < awayHTScore && homeScore == awayScore
            9 -> return homeHTScore < awayHTScore && homeScore < awayScore
        }
        return false
    }

    private fun calculateFirstHalfResult(): Boolean {
        val htKey =
            if ((score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null || score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)) "c" else ScoreType.HALF_TIME.value
        val homeHTScore = score!!.getScore(true, htKey)
        val awayHTScore = score!!.getScore(false, htKey)
        when (outcomeNo) {
            1, 2, 3 -> return homeHTScore > awayHTScore
            4, 5, 6 -> return homeHTScore == awayHTScore
            7, 8, 9 -> return homeHTScore < awayHTScore
        }
        return false
    }

    private fun teamScoredResult(isHome: Boolean): Boolean {
        val teamScore = score!!.getScore(isHome, "c")
        when (outcomeNo) {
            1 -> return teamScore != 0
            2 -> return teamScore == 0
        }
        return false
    }

    private fun whichTeamScored(): Boolean {
        val homeScore = score!!.getScore(true, "c")
        val awayScore = score!!.getScore(false, "c")
        when (outcomeNo) {
            1 -> return homeScore != 0 && awayScore == 0
            2 -> return homeScore == 0 && awayScore != 0
            3 -> return homeScore != 0 && awayScore != 0
            4 -> return homeScore == 0 && awayScore == 0
        }
        return false
    }

    private fun halfTotalGoalResult(firstHalf: Boolean): Boolean {
        val htKey =
            if ((score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null || score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)) ScoreType.CURRENT.value else ScoreType.HALF_TIME.value
        val homeFHScore = score!!.getScore(true, htKey)
        val awayFHScore = score!!.getScore(false, htKey)
        var totalGoal = homeFHScore + awayFHScore
        if (!firstHalf) {
            val t = score!!.getScore(true, ScoreType.REGULAR.value) + score!!.getScore(
                false,
                ScoreType.REGULAR.value
            )
            totalGoal = t - totalGoal
        }

        when (outcomeNo) {
            1 -> return totalGoal == 0
            2 -> return totalGoal == 1
            3 -> return totalGoal >= 2
        }
        return false
    }

    private fun halfTotalGoalLiveResult(firstHalf: Boolean): Boolean {
        val htKey =
            if ((score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null || score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)) "c" else ScoreType.HALF_TIME.value
        val homeFHScore = score!!.getScore(true, htKey)
        val awayFHScore = score!!.getScore(false, htKey)
        var totalGoal = homeFHScore + awayFHScore
        if (!firstHalf) {
            val t = score!!.getScore(true, ScoreType.REGULAR.value) + score!!.getScore(
                false,
                ScoreType.REGULAR.value
            )
            totalGoal = t - totalGoal
        }

        when (outcomeNo) {
            1 -> return totalGoal == 0
            2 -> return totalGoal == 1
            3 -> return totalGoal == 2
            4 -> return totalGoal == 3
            5 -> return totalGoal >= 4
        }
        return false
    }

    private fun teamWinBothHalfResult(isHome: Boolean): Boolean {
        val htKey =
            if ((score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null || score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)) "c" else ScoreType.HALF_TIME.value
        val homeFHScore = score!!.getScore(true, htKey)
        val awayFHScore = score!!.getScore(false, htKey)
        val homeScore = score!!.getScore(true, ScoreType.REGULAR.value)
        val awayScore = score!!.getScore(false, ScoreType.REGULAR.value)
        val homeSHScore = homeScore - homeFHScore
        val awaySHScore = awayScore - awayFHScore

        val teamWonBothHalf = if (isHome)
            homeFHScore > awayFHScore && homeSHScore > awaySHScore
        else
            awayFHScore > homeFHScore && awaySHScore > homeSHScore
        when (outcomeNo) {
            1 -> return teamWonBothHalf
            2 -> return !teamWonBothHalf
        }
        return false
    }

    private fun teamWonLeastHalfResult(isHome: Boolean): Boolean {
        val htKey =
            if ((score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null || score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)) "c" else ScoreType.HALF_TIME.value
        val homeFHScore = score!!.getScore(true, htKey)
        val awayFHScore = score!!.getScore(false, htKey)
        val homeScore = score!!.getScore(true, ScoreType.REGULAR.value)
        val awayScore = score!!.getScore(false, ScoreType.REGULAR.value)
        val homeSHScore = homeScore - homeFHScore
        val awaySHScore = awayScore - awayFHScore

        val teamWonBothHalf = if (isHome)
            homeFHScore > awayFHScore || homeSHScore > awaySHScore
        else
            awayFHScore > homeFHScore || awaySHScore > homeSHScore
        when (outcomeNo) {
            1 -> return teamWonBothHalf
            2 -> return !teamWonBothHalf
        }
        return false
    }

    private fun totalGoalResult(): Boolean {
        val totalGoal = score!!.getScore(true, ScoreType.REGULAR.value) +
                score!!.getScore(false, ScoreType.REGULAR.value)

        when (outcomeNo) {
            1 -> return totalGoal <= 1
            2 -> return totalGoal >= 2 && totalGoal <= 3
            3 -> return totalGoal >= 4 && totalGoal <= 5
            4 -> return totalGoal >= 6
        }
        return false
    }

    private fun totalScore180PointRangeResult(): Boolean {
        val totalScore = score!!.getScore(true, "r") + score!!.getScore(false, "r")

        when (outcomeNo) {
            1 -> return totalScore <= 100
            2 -> return totalScore >= 101 && totalScore <= 110
            3 -> return totalScore >= 111 && totalScore <= 120
            4 -> return totalScore >= 121 && totalScore <= 130
            5 -> return totalScore >= 131 && totalScore <= 140
            6 -> return totalScore >= 141 && totalScore <= 150
            7 -> return totalScore >= 151 && totalScore <= 160
            8 -> return totalScore >= 161 && totalScore <= 170
            9 -> return totalScore >= 171 && totalScore <= 180
            10 -> return totalScore >= 181

        }
        return false
    }

    private fun totalScore250PointRangeResult(): Boolean {
        val totalScore = score!!.getScore(true, "r") + score!!.getScore(false, "r")

        when (outcomeNo) {
            1 -> return totalScore <= 150
            2 -> return totalScore >= 151 && totalScore <= 160
            3 -> return totalScore >= 161 && totalScore <= 170
            4 -> return totalScore >= 171 && totalScore <= 180
            5 -> return totalScore >= 181 && totalScore <= 190
            6 -> return totalScore >= 191 && totalScore <= 200
            7 -> return totalScore >= 201 && totalScore <= 210
            8 -> return totalScore >= 211 && totalScore <= 220
            9 -> return totalScore >= 221 && totalScore <= 230
            10 -> return totalScore >= 231 && totalScore <= 240
            11 -> return totalScore >= 241 && totalScore <= 250
            12 -> return totalScore >= 251
        }
        return false
    }

    private fun mostScoredQuarterResult(): Boolean {
        val q1 = score!!.getScore(true, "q1") + score!!.getScore(false, "q1")
        val q2 = score!!.getScore(true, "q2") + score!!.getScore(false, "q2")
        val q3 = score!!.getScore(true, "q3") + score!!.getScore(false, "q3")
        val q4 = score!!.getScore(true, "q4") + score!!.getScore(false, "q4")

        when (outcomeNo) {
            1 -> return q1 > q2 && q1 > q3 && q1 > q4
            2 -> return q2 > q1 && q2 > q3 && q2 > q4
            3 -> return q3 > q1 && q3 > q2 && q3 > q4
            4 -> return q4 > q1 && q4 > q2 && q4 > q3
            5 -> return q1 == q2 || q1 > q3 || q1 > q4 || q2 == q3 || q2 > q4 || q3 > q4
        }
        return false
    }

    private fun mostScoredHalfResult(): Boolean {
        val q1 = score!!.getScore(true, "q1") + score!!.getScore(false, "q1")
        val q2 = score!!.getScore(true, "q2") + score!!.getScore(false, "q2")
        val q3 = score!!.getScore(true, "q3") + score!!.getScore(false, "q3")
        val q4 = score!!.getScore(true, "q4") + score!!.getScore(false, "q4")

        when (outcomeNo) {
            1 -> return q1 + q2 > q3 + q4
            2 -> return q1 + q2 < q3 + q4
        }
        return false
    }

    private fun teamMoreScoredHalfResult(isHome: Boolean): Boolean {
        val htKey =
            if ((score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null || score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)) "c" else ScoreType.HALF_TIME.value

        val firstHalf = score!!.getScore(isHome, htKey)
        val teamScore = score!!.getScore(isHome, ScoreType.REGULAR.value)

        val secondHalf = teamScore - firstHalf

        when (outcomeNo) {
            1 -> return firstHalf > secondHalf
            2 -> return firstHalf == secondHalf
            3 -> return firstHalf < secondHalf
        }
        return false
    }

    private fun winningMarginsResult(): Boolean {
        val homeScore = score!!.getScore(true, "c")
        val awayScore = score!!.getScore(false, "c")

        val diff = homeScore - awayScore

        when (outcomeNo) {
            1 -> return diff > 10
            2 -> return diff >= 6 && diff <= 10
            3 -> return diff >= 1 && diff <= 5
            4 -> return diff <= -1 && diff >= -5
            5 -> return diff <= -6 && diff >= -10
            6 -> return diff < -10
        }
        return false
    }

    private fun soccerWinningMarginsResult(): Boolean {
        val homeScore = score!!.getScore(true, ScoreType.REGULAR.value)
        val awayScore = score!!.getScore(false, ScoreType.REGULAR.value)

        val diff = homeScore - awayScore

        when (outcomeNo) {
            1 -> return diff > 2
            2 -> return diff == 2
            3 -> return diff == 1
            4 -> return diff == -1
            5 -> return diff == -2
            6 -> return diff < -2
            7 -> return diff == 0
        }
        return false
    }

    private fun matchHalfTimeScoreResult(): Boolean {
        val htKey = if ((score!!.getHomeScore()[ScoreType.HALF_TIME.value] == null ||
                    score!!.getAwayScore()[ScoreType.HALF_TIME.value] == null)
        ) ScoreType.CURRENT.value else ScoreType.HALF_TIME.value

        val strScore =
            score!!.getScore(true, htKey).toString() + ":" + score!!.getScore(false, htKey)

        val scoresList = ArrayList(
            mutableListOf<String?>(
                "0:0", "1:1", "2:2",
                "1:0", "2:0", "2:1",
                "0:1", "0:2", "1:2"
            )
        )

        if (outcomeNo == 10) {
            return !scoresList.contains(strScore)
        }
        return scoresList[outcomeNo - 1] != null && scoresList[outcomeNo - 1] == strScore
    }

    private fun matchScoreResult(): Boolean {
        val strScore = score!!.getScore(true, ScoreType.REGULAR.value).toString() + ":" +
                score!!.getScore(false, ScoreType.REGULAR.value)

        val scoresList = ArrayList(
            mutableListOf<String?>(
                "1:0", "2:0", "2:1", "3:0", "3:1", "3:2", "4:0", "4:1", "4:2", "5:0", "5:1", "6:0",
                "0:0", "1:1", "2:2", "3:3",
                "0:1", "0:2", "1:2", "0:3", "1:3", "2:3", "0:4", "1:4", "2:4", "0:5", "1:5", "0:6"
            )
        )

        if (outcomeNo == 29) {
            return !scoresList.contains(strScore)
        }
        return scoresList[outcomeNo - 1] != null && scoresList[outcomeNo - 1] == strScore
    }

    private fun matchScoreLiveResult(): Boolean {
        val strScore = score!!.getScore(true, ScoreType.REGULAR.value).toString() + ":" +
                score!!.getScore(false, ScoreType.REGULAR.value)

        val scoresList = ArrayList(
            mutableListOf<String?>(
                "1:0", "2:0", "2:1", "3:0", "3:1", "3:2",
                "0:0", "1:1", "2:2", "3:3",
                "0:1", "0:2", "1:2", "0:3", "1:3", "2:3"
            )
        )

        if (outcomeNo == 17) {
            return !scoresList.contains(strScore)
        }
        return scoresList[outcomeNo - 1] != null && scoresList[outcomeNo - 1] == strScore
    }

    private fun firstHalfAndMatchScoreResult(): Boolean {
        if (score != null) {
            if (score!!.status != null && score!!.status == MatchStatus.FIRST_HALF.value) {
                return firstHalfScoreResult()
            } else if (score!!.status == MatchStatus.SECOND_HALF.value) {
                return fullTimeScoreResult()
            }
        }
        return false
    }

    private fun firstHalfScoreResult(): Boolean {
        val homeHalfTimeScore = score!!.getScore(true, ScoreType.HALF_TIME.value)
        val awayHalfTimeScore = score!!.getScore(false, ScoreType.HALF_TIME.value)
        var halfTimeScore = "$homeHalfTimeScore:$awayHalfTimeScore"
        if (homeHalfTimeScore + awayHalfTimeScore > 3) halfTimeScore = "4+"

        val strScore = halfTimeScore

        val scoresList = ArrayList(
            mutableListOf(
                "0:0",
                "1:0",
                "0:1",
                "2:0",
                "1:1",
                "0:2",
                "3:0",
                "2:1",
                "1:2",
                "0:3",
                "4+"
            )
        )

        val outcomeIndex = getIYMSScoreOutcomeIndex(outcomeNo)
        return scoresList[outcomeIndex - 1] == strScore
    }


    fun getIYMSScoreOutcomeIndex(outcomeNo: Int): Int {

        //First half and match score marketinde gelen outcome'ları yakalamak için
        when (outcomeNo) {
            in 1..11 -> return 1
            in 12..18 -> return 2
            in 19..25 -> return 3
            in 26..29 -> return 4
            in 30..33 -> return 5
            in 34..37 -> return 6
            in 38..39 -> return 7
            in 40..41 -> return 8
            in 42..43 -> return 9
            in 44..45 -> return 10
            in 46..46 -> return 11
        }
        return 0
    }

    private fun fullTimeScoreResult(): Boolean {
        val homeHalfTimeScore = score!!.getScore(true, ScoreType.HALF_TIME.value)
        val awayHalfTimeScore = score!!.getScore(false, ScoreType.HALF_TIME.value)
        var halfTimeScore = "$homeHalfTimeScore:$awayHalfTimeScore"
        if (homeHalfTimeScore + awayHalfTimeScore > 3) halfTimeScore = "4+"

        val homeFullTimeScore = score!!.getScore(true, ScoreType.REGULAR.value)
        val awayFullTimeScore = score!!.getScore(false, ScoreType.REGULAR.value)
        var fullTimeScore = "$homeFullTimeScore:$awayFullTimeScore"
        if (homeFullTimeScore + awayFullTimeScore > 3) fullTimeScore = "4+"

        val strScore = halfTimeScore + " " +
                fullTimeScore

        val scoresList = ArrayList(
            mutableListOf<String?>(
                "0:0 0:0", "0:0 0:1", "0:0 0:2", "0:0 0:3", "0:0 1:0", "0:0 1:1",
                "0:0 1:2", "0:0 2:0", "0:0 2:1", "0:0 3:0", "0:0 4+", "1:0 1:0",
                "1:0 1:1", "1:0 1:2", "1:0 2:0", "1:0 2:1", "1:0 3:0", "1:0 4+",
                "0:1 0:1", "0:1 0:2", "0:1 0:3", "0:1 1:1", "0:1 1:2", "0:1 2:1",
                "0:1 4+", "2:0 2:0", "2:0 2:1", "2:0 3:0", "2:0 4+", "1:1 1:1",
                "1:1 1:2", "1:1 2:1", "1:1 4+", "0:2 0:2", "0:2 0:3", "0:2 1:2",
                "0:2 4+", "3:0 3:0", "3:0 4+", "2:1 2:1", "2:1 4+", "1:2 1:2",
                "1:2 4+", "0:3 0:3", "0:3 4+", "4+ 4+"
            )
        )

        return scoresList[outcomeNo - 1] != null && scoresList[outcomeNo - 1] == strScore
    }

    private fun volleyballMatchScoreResult(): Boolean? {
        val strScore = score!!.getScore(true, ScoreType.REGULAR.value).toString() + ":" +
                score!!.getScore(false, ScoreType.REGULAR.value)

        val scoresList = ArrayList(
            mutableListOf<String?>("3:0", "3:1", "3:2", "0:3", "1:3", "2:3")
        )

        if (scoresList[outcomeNo - 1] == null) return null

        return scoresList[outcomeNo - 1] == strScore
    }

    private fun tennisMatchScoreResult(): Boolean? {
        val strScore = score!!.getScore(true, ScoreType.REGULAR.value).toString() + ":" +
                score!!.getScore(false, ScoreType.REGULAR.value)

        val scoresList = ArrayList(
            mutableListOf<String?>("2:0", "2:1", "0:2", "1:2")
        )

        if (scoresList[outcomeNo - 1] == null) return null

        return scoresList[outcomeNo - 1] == strScore
    }

    private val underOverForSetsTotalResult: Boolean
        get() {
            //getScore returns 0 if not available
            val total = (score!!.getScore(true, "s1") + score!!.getScore(false, "s1")
                    + score!!.getScore(true, "s2") + score!!.getScore(false, "s2")
                    + score!!.getScore(true, "s3") + score!!.getScore(false, "s3")
                    + score!!.getScore(true, "s4") + score!!.getScore(false, "s4")
                    + score!!.getScore(true, "s5") + score!!.getScore(false, "s5")
                    + score!!.getScore(true, "s6") + score!!.getScore(false, "s6")
                    + score!!.getScore(true, "s7") + score!!.getScore(false, "s7"))

            when (outcomeNo) {
                1 -> return total < specialOddValue
                2 -> return total > specialOddValue
            }
            return false
        }

    private val totalPointForSetsResult: Boolean
        get() {
            //getScore returns 0 if not available
            val homeTotal = (score!!.getScore(true, "s1")
                    + score!!.getScore(true, "s2")
                    + score!!.getScore(true, "s3")
                    + score!!.getScore(true, "s4")
                    + score!!.getScore(true, "s5")
                    + score!!.getScore(true, "s6")
                    + score!!.getScore(true, "s7"))
            val awayTotal = (score!!.getScore(false, "s1")
                    + score!!.getScore(false, "s2")
                    + score!!.getScore(false, "s3")
                    + score!!.getScore(false, "s4")
                    + score!!.getScore(false, "s5")
                    + score!!.getScore(false, "s6")
                    + score!!.getScore(false, "s7"))

            when (outcomeNo) {
                1 -> return homeTotal + specialOddValue > awayTotal
                2 -> return homeTotal + specialOddValue < awayTotal
            }
            return false
        }

    private fun oddEvenForTotalGames(): Boolean {
        //getScore returns 0 if not available
        val homeTotal = (score!!.getScore(true, "s1")
                + score!!.getScore(true, "s2")
                + score!!.getScore(true, "s3")
                + score!!.getScore(true, "s4")
                + score!!.getScore(true, "s5")
                + score!!.getScore(true, "s6")
                + score!!.getScore(true, "s7"))
        val awayTotal = (score!!.getScore(false, "s1")
                + score!!.getScore(false, "s2")
                + score!!.getScore(false, "s3")
                + score!!.getScore(false, "s4")
                + score!!.getScore(false, "s5")
                + score!!.getScore(false, "s6")
                + score!!.getScore(false, "s7"))

        val total = homeTotal + awayTotal

        when (outcomeNo) {
            1 -> return total.isOdd()
            2 -> return total.isEven()
        }
        return false
    }

    fun Int.isOdd() = this % 2 != 0
    fun Int.isEven() = this % 2 == 0

    private fun getUnderOverForSideSetsTotalResult(isHome: Boolean): Boolean {
        //getScore returns 0 if not available
        val totalWon = (score!!.getScore(isHome, "s1")
                + score!!.getScore(isHome, "s2")
                + score!!.getScore(isHome, "s3")
                + score!!.getScore(isHome, "s4")
                + score!!.getScore(isHome, "s5")
                + score!!.getScore(isHome, "s6")
                + score!!.getScore(isHome, "s7"))


        when (outcomeNo) {
            1 -> return totalWon < specialOddValue
            2 -> return totalWon > specialOddValue
        }
        return false
    }

    private val setsNumber: Int
        get() {
            var setsNumber = 0
            setsNumber += if (score!!.getHomeScore()["s1"] != null) 1 else 0
            setsNumber += if (score!!.getHomeScore()["s2"] != null) 1 else 0
            setsNumber += if (score!!.getHomeScore()["s3"] != null) 1 else 0
            setsNumber += if (score!!.getHomeScore()["s4"] != null) 1 else 0
            setsNumber += if (score!!.getHomeScore()["s5"] != null) 1 else 0

            return setsNumber
        }

    private val setsNumberFor3SetGameResult: Boolean
        get() {
            val setsNumber = setsNumber

            when (outcomeNo) {
                1 -> return setsNumber == 2
                2 -> return setsNumber == 3
            }
            return false
        }

    private val setsNumberFor5SetGameResult: Boolean
        get() {
            val setsNumber = setsNumber

            when (outcomeNo) {
                1 -> return setsNumber == 3
                2 -> return setsNumber == 4
                3 -> return setsNumber == 5
            }
            return false
        }

    private fun hasTieBreakResult(): Boolean {
        val hasTieBreak =
            (score!!.getScore(true, "s1") >= 6 && score!!.getScore(false, "s1") >= 6) ||
                    (score!!.getScore(true, "s2") >= 6 && score!!.getScore(false, "s2") >= 6) ||
                    (score!!.getScore(true, "s3") >= 6 && score!!.getScore(false, "s3") >= 6) ||
                    (score!!.getScore(true, "s4") >= 6 && score!!.getScore(false, "s4") >= 6) ||
                    (score!!.getScore(true, "s5") >= 6 && score!!.getScore(false, "s5") >= 6)
        when (outcomeNo) {
            1 -> return hasTieBreak
            2 -> return !hasTieBreak
        }
        return false
    }

    private val cornerNumberResult: Boolean
        get() {
            if (!score!!.hasScoreKey(ScoreType.CORNER.value)) return false
            val totalCorner = (score!!.getScore(true, ScoreType.CORNER.value)
                    + score!!.getScore(false, ScoreType.CORNER.value))

            when (outcomeNo) {
                1 -> return totalCorner <= 8
                2 -> return totalCorner > 8 && totalCorner < 12
                3 -> return totalCorner >= 12
            }
            return false
        }

    private val hasRedCardResult: Boolean
        get() {
            if (!score!!.hasScoreKey(ScoreType.RED_CARD.value)) return false
            val totalRedCard = (score!!.getScore(true, ScoreType.RED_CARD.value)
                    + score!!.getScore(false, ScoreType.RED_CARD.value))

            when (outcomeNo) {
                1 -> return totalRedCard > 0
                2 -> return totalRedCard == 0
            }
            return false
        }

    private val totalCardUnderOverResult: Boolean
        get() {
            val totalCard =
                (((score!!.getScore(true, ScoreType.RED_CARD.value)
                        + score!!.getScore(false, ScoreType.RED_CARD.value)
                        ) * 2
                        ) + score!!.getScore(true, ScoreType.YELLOW_CARD.value)
                        + score!!.getScore(false, ScoreType.YELLOW_CARD.value))

            when (outcomeNo) {
                1 -> return totalCard < specialOddValue
                2 -> return totalCard > specialOddValue
            }
            return false
        }

    private fun getTotalCardUnderOverResult(isHome: Boolean): Boolean {
        val totalCard =
            ((score!!.getScore(isHome, ScoreType.RED_CARD.value)
                    ) * 2
                    + score!!.getScore(isHome, ScoreType.YELLOW_CARD.value))

        when (outcomeNo) {
            1 -> return totalCard < specialOddValue
            2 -> return totalCard > specialOddValue
        }
        return false
    }

    private val setResultWithSpecialOddValue: Boolean
        get() {
            val scoreKey = "s" + specialOddValue.toInt()

            val homeScore = score!!.getScore(true, scoreKey)
            val awayScore = score!!.getScore(false, scoreKey)

            when (outcomeNo) {
                1 -> return homeScore > awayScore
                2 -> return homeScore < awayScore
            }
            return false
        }

    private val setResult: Boolean
        get() {
            val homeScore = score!!.getScore(true, "s1")
            val awayScore = score!!.getScore(false, "s1")

            when (outcomeNo) {
                1 -> return homeScore > awayScore
                2 -> return homeScore < awayScore
            }
            return false
        }

    private val periodResultWithSpecialOddValue: Boolean
        get() {
            val scoreKey = "p" + specialOddValue.toInt()

            val homeScore = score!!.getScore(true, scoreKey)
            val awayScore = score!!.getScore(false, scoreKey)

            when (outcomeNo) {
                1 -> return homeScore > awayScore
                2 -> return homeScore == awayScore
                3 -> return homeScore < awayScore
            }
            return false
        }

    val isBothScoredSecondHalf: Boolean
        get() {
            val homeScore = score!!.getScore(true, ScoreType.REGULAR.value)
            val awayScore = score!!.getScore(false, ScoreType.REGULAR.value)

            val homeHalfTimeScore = score!!.getScore(true, ScoreType.HALF_TIME.value)
            val awayHalfTimeScore = score!!.getScore(false, ScoreType.HALF_TIME.value)

            val diffHome = homeScore - homeHalfTimeScore
            val diffAway = awayScore - awayHalfTimeScore

            return diffHome > 0 && diffAway > 0
        }
}
