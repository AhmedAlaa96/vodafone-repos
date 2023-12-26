package com.ahmed.vodafonerepos.data.models.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class RepoIssueResponse(
    @SerializedName("active_lock_reason")
    val activeLockReason: Any? = null,
    @SerializedName("assignee")
    val assignee: Any? = null,
    @SerializedName("assignees")
    val assignees: List<Any>? = null,
    @SerializedName("author_association")
    val authorAssociation: String? = null,
    @SerializedName("body")
    val body: String? = null,
    @SerializedName("closed_at")
    val closedAt: String? = null,
    @SerializedName("comments")
    val comments: Int? = null,
    @SerializedName("comments_url")
    val commentsUrl: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("events_url")
    val eventsUrl: String? = null,
    @SerializedName("html_url")
    val htmlUrl: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("labels")
    val labels: List<IssueLabel>? = null,
    @SerializedName("labels_url")
    val labelsUrl: String? = null,
    @SerializedName("locked")
    val locked: Boolean? = null,
    @SerializedName("milestone")
    val milestone: Any? = null,
    @SerializedName("node_id")
    val nodeId: String? = null,
    @SerializedName("number")
    val number: Int? = null,
    @SerializedName("performed_via_github_app")
    val performedViaGithubApp: Any? = null,
    @SerializedName("reactions")
    val reactions: Reactions? = null,
    @SerializedName("repository_url")
    val repositoryUrl: String? = null,
    @SerializedName("state")
    val state: String? = null,
    @SerializedName("state_reason")
    val stateReason: Any? = null,
    @SerializedName("timeline_url")
    val timelineUrl: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("user")
    val user: User? = null
)