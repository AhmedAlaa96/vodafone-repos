package com.ahmed.vodafonerepos.ui.reposlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmed.vodafonerepos.R
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.ui.base.PagingLoadingScreen
import com.ahmed.vodafonerepos.utils.alternate


@Composable
fun ReposList(
    reposList: ArrayList<RepoResponse>,
    loadingMore: Boolean,
    scrollState: LazyListState,
    isScrolledToBottom: Boolean,
    onScroll: () -> Unit
) {
    LazyColumn(
        state = scrollState
    ) {
        items(reposList) { item ->
            RepoItem(item)
        }
        item {
            if (loadingMore) {
                // Show a loading indicator or text
                PagingLoadingScreen()
            }
        }
    }

    DisposableEffect(isScrolledToBottom) {
        if (isScrolledToBottom && !loadingMore) {
            onScroll.invoke()
        }
        onDispose { }
    }
}

@Composable
fun RepoItem(item: RepoResponse) {
    // Compose UI for each item in the list
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .padding(dimensionResource(id = R.dimen.size_16))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = item.name.alternate(),
                color = colorResource(id = R.color.black),
                fontSize = 18.sp
            )
            Text(
                text = item.owner?.login.alternate(),
                modifier = Modifier.padding(top = 16.dp),
                color = colorResource(id = R.color.black),
                fontSize = 14.sp
            )
        }
    }
}