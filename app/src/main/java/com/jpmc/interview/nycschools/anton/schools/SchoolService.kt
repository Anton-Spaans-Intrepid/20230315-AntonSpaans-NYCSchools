package com.jpmc.interview.nycschools.anton.schools

import retrofit2.http.GET

/** Defines the remote HTTP services for the [SchoolDomain]. */
interface SchoolService {

    /** Returns an unordered list of [Schools][School] from the remote server. */
    @GET("s3k6-pzi2.json")
    suspend fun getSchools(): List<School>

    /** Returns an unordered list of [AverageScores] from the remote server. */
    @GET("f9bf-2cp4.json")
    suspend fun getSatScores(): List<AverageScores>
}
