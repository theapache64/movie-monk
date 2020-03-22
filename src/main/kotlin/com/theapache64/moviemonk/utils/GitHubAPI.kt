package com.theapache64.moviemonk.utils

import com.google.gson.reflect.TypeToken
import com.theapache64.moviemonk.models.external.github.*
import java.io.IOException

object GitHubAPI {

    private const val BASE_URL = "https://api.github.com"

    private val headers = mapOf(
        "Authorization" to "token ${System.getenv("MOVIE_MONK_GITHUB_ACCESS_TOKEN")}"
    )

    /**
     * To create a comment on an issue
     */
    @Throws(IOException::class)
    fun createIssueComment(
        ownerName: String,
        repoName: String,
        issueId: Int,
        comment: String
    ) {

        val url = "$BASE_URL/repos/$ownerName/$repoName/issues/$issueId/comments"
        val responseCode = RestClient.post(
            url,
            headers,
            AddCommentRequest(comment)
        ).code


        if (responseCode != 201) {
            throw IOException("Failed to create issue comment $#")
        }
    }

    /**
     * To get issues
     */
    fun getIssues(
        ownerName: String,
        repoName: String
    ): List<Issue> {
        val url = "$BASE_URL/repos/$ownerName/$repoName/issues"
        val respString = RestClient.get(url, headers).body!!.string()
        val type = object : TypeToken<List<Issue>>() {}.type
        return GsonUtil.gson.fromJson(respString, type)
    }


    /**
     * To create an issue
     */
    @Throws(IOException::class)
    fun createIssue(
        ownerName: String,
        repoName: String,
        title: String,
        comment: String,
        labels: List<String>
    ): CreateIssueResponse {

        val url = "$BASE_URL/repos/$ownerName/$repoName/issues"

        // Create the issue
        val createIssueResp = RestClient.post(
            url,
            headers,
            CreateIssueRequest(
                comment,
                labels,
                title
            )
        )

        if (createIssueResp.code != 201) {
            throw IOException("Failed to create issue")
        }

        val newIssueJson = createIssueResp.body!!.string()
        return GsonUtil.gson.fromJson(newIssueJson, CreateIssueResponse::class.java)
    }

    /**
     * To lock an issue
     */
    @Throws(IOException::class)
    fun lockIssue(ownerName: String, repoName: String, number: Int) {

        val url = "$BASE_URL/repos/$ownerName/$repoName/issues/$number/lock"


        val lockStatus = RestClient.put(
            url,
            headers,
            LockIssueRequest(
                "",
                true
            )
        ).code

        if (lockStatus != 204) {
            throw IOException("Failed to lock issue")
        }
    }
}