package com.ahmed.vodafonerepos.ui.repoissues

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmed.vodafonerepos.R
import com.ahmed.vodafonerepos.data.models.dto.RepoIssueResponse
import com.ahmed.vodafonerepos.ui.base.PagingLoadingScreen
import com.ahmed.vodafonerepos.utils.DateTimeHelper
import com.ahmed.vodafonerepos.utils.Utils
import com.ahmed.vodafonerepos.utils.alternate
import com.ahmed.vodafonerepos.utils.utilities.UIUtils
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun RepoIssuesList(
    reposList: ArrayList<RepoIssueResponse>, loadingMore: Boolean,
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
            items(reposList, key = { it.id ?: 0 }) { item ->
                IssueItem(item)
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
fun IssueItem(item: RepoIssueResponse) {
    val context = LocalContext.current
    Card(modifier = Modifier
        .fillMaxHeight()
        .padding(dimensionResource(id = R.dimen.size_16)),
        elevation = 5.dp,
        onClick = {
            if (item.htmlUrl.isNullOrEmpty()) {
                UIUtils.showToast(context, context.getString(R.string.invalid_url))
            } else {
                Utils.openUrlInBrowser(context, item.htmlUrl)
            }
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.title.alternate(),
                        modifier = Modifier.padding(end = dimensionResource(id = R.dimen.size_4)),
                        style = MaterialTheme.typography.h4,
                        color = colorResource(id = R.color.black),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                    Text(
                        text = item.user?.login.alternate(),
                        modifier = Modifier.padding(top = 16.dp),
                        color = colorResource(id = R.color.gray),
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = DateTimeHelper.convertDateStringToAnotherFormat(item.createdAt),
                        modifier = Modifier.padding(top = 16.dp),
                        color = colorResource(id = R.color.gray),
                        style = MaterialTheme.typography.subtitle1,
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = item.state.alternate(),

                        style = MaterialTheme.typography.body2
                            .copy(
                                fontWeight = FontWeight.SemiBold,
                                color = colorResource(id = R.color.gray),
                            ),
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_open_url),
                        contentDescription = "open in browser",
                        modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_4)),
                        tint = colorResource(
                            id = R.color.gray
                        )
                    )
                }
            }
        }
    }
}