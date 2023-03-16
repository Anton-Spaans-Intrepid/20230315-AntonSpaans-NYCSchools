package com.jpmc.interview.nycschools.anton.schools

import com.google.gson.annotations.SerializedName
import java.io.IOException

/**
 * Represents an id of a School entity.
 *
 * A value class is used to prevent regular [Strings][String] from being used as IDs.
 */
@JvmInline
value class SchoolId(val id: String)

/**
 * Represents a NYC area school
 *
 * @property id ID of the school entity
 * @property name User readable name of the school.
 */
data class School(
    @SerializedName("dbn")
    val id: SchoolId,
    @SerializedName("school_name")
    val name: String,
)

/**
 * Represents a SAT score.
 *
 * It is wrapped in a value class, since it can be either an [Int] value or the value "s".
 */
@JvmInline
value class Score(private val score: String) {
    fun value(): Int? = score.toIntOrNull()
}

/**
 * Represents the average reading, writing and math score for the school with the provided [id].
 *
 * @property id Id of the school entity whose sat scores are part of this entity
 * @property math Average math SAT score for the school
 * @property reading Average reading SAT score for the school
 * @property writing Average writing SAT score for the school
 */
data class AverageScores(
    @SerializedName("dbn")
    val id: SchoolId,
    @SerializedName("sat_math_avg_score")
    val math: Score,
    @SerializedName("sat_critical_reading_avg_score")
    val reading: Score,
    @SerializedName("sat_writing_avg_score")
    val writing: Score,
)

/** Domain specific Errors that can be produced by this [SchoolDomain]. */
sealed class SchoolError {

    /** A network error occurred, represented by the exception [t]. */
    class NetworkError internal constructor(private val t: Throwable) : SchoolError() {
        override fun toString(): String = "NetworkError: $t"
    }

    /** A service error, eg 404, parse-exception, unexpected data, etc. */
    class ServiceError internal constructor(private val t: Throwable?) : SchoolError() {
        override fun toString(): String = "ServiceError: $t"
    }

    companion object {
        /** Returns the appropriate sub-type of [SchoolError] based on the input [t]. */
        operator fun invoke(t: Throwable? = null): SchoolError {
            return when (t) {
                is IOException -> NetworkError(t)
                else -> ServiceError(t)
            }
        }
    }
}
