package com.ahmed.vodafonerepos.utils

object Constants {
    object SharedPreference {
        const val SHARED_PREF_NAME = "my_shared_pref"
    }

    object General {
        const val EMPTY_TEXT = ""
        const val DASH_TEXT = "-"
    }


    object Network {
        const val CONNECT_TIMEOUT = 5L
        const val READ_TIMEOUT = 5L
        const val WRITE_TIMEOUT = 5L
    }

    object URL {
        const val BASE_URL = "https://api.github.com/"
        const val GET_REPOS_LIST = "repositories"
        const val OWNER = "owner"
        const val REPO = "repo"
        const val GET_REPO_DETAILS = "repos/{${OWNER}}/{${REPO}}"
        const val GET_REPO_ISSUES = "$GET_REPO_DETAILS/issues"
    }

    object QueryParams {
        const val PAGE = "page"
    }

    object Headers {
        const val AUTHORIZATION = "Authorization"
        const val ACCEPT = "accept"
        const val AUTHORIZATION_VALUE = "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"
        const val ACCEPT_VALUE = "application/json"
    }

    object ViewsTags {
        const val RECYCLER_VIEW_MOVIES = "RECYCLER_VIEW_MOVIES"
    }
}