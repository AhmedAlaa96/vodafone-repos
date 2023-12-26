package com.ahmed.vodafonerepos.utils

import com.ahmed.vodafonerepos.BuildConfig

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
        const val BASE_URL = BuildConfig.BASE_NETWORK_URL
        const val GET_REPOS_LIST = "repositories"
        const val OWNER = "owner"
        const val REPO = "repo"
        const val GET_REPO_DETAILS = "repos/{${OWNER}}/{${REPO}}"
        const val GET_REPO_ISSUES = "$GET_REPO_DETAILS/issues"
    }
    object Headers {
        const val ACCEPT = "accept"
        const val ACCEPT_VALUE = "application/json"
    }

}