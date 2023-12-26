package com.ahmed.vodafonerepos.ui.repoissues

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmed.vodafonerepos.R
import com.ahmed.vodafonerepos.data.models.dto.RepoIssueResponse
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.utils.alternate


@Composable
fun RepoIssuesList(reposList: ArrayList<RepoIssueResponse>) {
    LazyColumn() {
        items(reposList) { item ->
            RepoItem(item)
        }
    }
}

@Composable
fun RepoItem(item: RepoIssueResponse) {
    // Compose UI for each item in the list
    Card(modifier = Modifier
        .fillMaxHeight()
        .padding(dimensionResource(id = R.dimen.size_16))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = item.title.alternate(),
                color = colorResource(id = R.color.black),
                fontSize = 18.sp
            )
            Text(
                text = item.user?.login.alternate(),
                modifier = Modifier.padding(top = 16.dp),
                color = colorResource(id = R.color.black),
                fontSize = 14.sp
            )
            Text(
                text = item.createdAt.alternate(),
                modifier = Modifier.padding(top = 16.dp),
                color = colorResource(id = R.color.black),
                fontSize = 14.sp
            )
            Text(
                text = item.state.alternate(),
                modifier = Modifier.padding(top = 16.dp),
                color = colorResource(id = R.color.black),
                fontSize = 14.sp
            )
        }
    }
}