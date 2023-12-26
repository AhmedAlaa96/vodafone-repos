package com.ahmed.vodafonerepos.ui.reposlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
        items(reposList, key = {
            it.repoId ?: 0
        }) { item ->
            RepoItem(item)
        }
        item {
            if (loadingMore) {
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
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .padding(dimensionResource(id = R.dimen.size_16)),
        elevation = 5.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.name.alternate(),
                        style = MaterialTheme.typography.h4,
                        color = colorResource(id = R.color.black)
                    )
                    Text(
                        text = item.owner?.login.alternate(),
                        modifier = Modifier.padding(top = 16.dp),
                        color = colorResource(id = R.color.gray),
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = item.description.alternate(),
                        modifier = Modifier.padding(top = 16.dp),
                        color = colorResource(id = R.color.gray),
                        style = MaterialTheme.typography.subtitle1,
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = "star",
                        tint = colorResource(
                            id = R.color.yellow
                        )
                    )
                    Text(
                        text = "110".alternate(),
                        color = colorResource(id = R.color.black),
                        style = MaterialTheme.typography.subtitle1,
                    )
                }
            }
        }
    }
}