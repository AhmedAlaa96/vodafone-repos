package com.ahmed.vodafonerepos.ui.reposlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import com.ahmed.vodafonerepos.R
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.ui.base.PagingLoadingScreen
import com.ahmed.vodafonerepos.ui.theme.LINE_1
import com.ahmed.vodafonerepos.ui.theme.LINE_2
import com.ahmed.vodafonerepos.utils.alternate
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun ReposList(
    reposList: ArrayList<RepoResponse>,
    onItemClickListener: (item: RepoResponse) -> Unit,
    loadingMore: Boolean,
    scrollState: LazyListState,
    isScrolledToBottom: Boolean,
    refreshState: Boolean,
    onScroll: () -> Unit,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshState),
        onRefresh = { onRefresh.invoke() }) {
        LazyColumn(
            state = scrollState
        ) {
            items(reposList, key = {
                it.repoId ?: 0
            }) { item ->
                RepoItem(item, onItemClickListener)
            }
            item {
                if (loadingMore) {
                    PagingLoadingScreen()
                }
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RepoItem(item: RepoResponse, onItemClickListener: (item: RepoResponse) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .padding(dimensionResource(id = R.dimen.size_16)),
        elevation = dimensionResource(id = R.dimen.size_5),
        onClick = {
            onItemClickListener.invoke((item))
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.size_16)),
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
                        color = colorResource(id = R.color.black),
                        overflow = TextOverflow.Ellipsis,
                        minLines = LINE_1,
                        maxLines = LINE_1,

                        )
                    Text(
                        text = item.owner?.login.alternate(),
                        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.size_16)),
                        color = colorResource(id = R.color.gray),
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis,
                        minLines = LINE_1,
                        maxLines = LINE_1,
                    )
                    Text(
                        text = item.description.alternate(),
                        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.size_16)),
                        color = colorResource(id = R.color.gray),
                        style = MaterialTheme.typography.subtitle1,
                        overflow = TextOverflow.Ellipsis,
                        minLines = LINE_2,
                        maxLines = LINE_2,
                    )
                }
            }
        }
    }
}