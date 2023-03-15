package com.jpmc.interview.nycschools.anton.schools

/**
 * Defines the business-domain of showing schools and getting their average SAT scores.
 */
class SchoolDomain(private val schoolService: SchoolService) {

    /** Returns an unsorted list of [School] in the NYC area */
    suspend fun getSchools() : List<School> {
        return schoolService.getSchools()
    }

    /** Returns [average SAT scores][AverageScores] for the school with the given [id]. */
    suspend fun getAverageScores(id: SchoolId): AverageScores {
        return schoolService.getSatScores().first { it.id == id }
    }
}
