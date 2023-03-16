package com.jpmc.interview.nycschools.anton.schools

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.flatMap

/**
 * Defines the business-domain of showing schools and getting their average SAT scores.
 */
class SchoolDomain(private val schoolService: SchoolService) {

    /** Returns an unsorted list of [School] in the NYC area, or a [SchoolError]. */
    suspend fun getSchools(): Either<SchoolError, List<School>> {
        return Either.catch { schoolService.getSchools() }.mapLeft { SchoolError(it) }
    }

    /**
     * Returns [average SAT scores][AverageScores] for the school with the given [id], or
     * a [SchoolError].
     * */
    suspend fun getAverageScores(id: SchoolId): Either<SchoolError, AverageScores> {
        return Either.catch { schoolService.getSatScores().firstOrNull { it.id == id } }
            .mapLeft { SchoolError(it) }
            .flatMap { if (it == null) Left(SchoolError()) else Right(it) }
    }
}
