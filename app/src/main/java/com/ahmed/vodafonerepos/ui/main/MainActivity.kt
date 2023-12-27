package com.ahmed.vodafonerepos.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.ahmed.vodafonerepos.R
import com.ahmed.vodafonerepos.ui.repodetails.RepoDetailsScreen
import com.ahmed.vodafonerepos.ui.repoissues.RepoIssuesList
import com.ahmed.vodafonerepos.ui.repoissues.RepoIssuesListScreen
import com.ahmed.vodafonerepos.ui.reposlist.ReposListScreen
import com.ahmed.vodafonerepos.ui.theme.VodafoneReposTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VodafoneReposTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppNavHost(navController = rememberNavController())
                }
            }
        }
    }
}
