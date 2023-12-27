package com.ahmed.vodafonerepos.ui.repodetails

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ahmed.vodafonerepos.R
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsResponse
import com.ahmed.vodafonerepos.ui.base.NetworkImage
import com.ahmed.vodafonerepos.ui.base.Toolbar
import com.ahmed.vodafonerepos.utils.alternate
import com.ahmed.vodafonerepos.utils.utilities.UIUtils


@Composable
fun RepoDetailsContent(
    repoDetailsResponse: RepoDetailsResponse,
    onIssuesClicked: (repoDetailsResponse: RepoDetailsResponse) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(dimensionResource(id = R.dimen.size_16)),
            elevation = dimensionResource(id = R.dimen.size_5),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.size_16)),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    NetworkImage(imageUrl = repoDetailsResponse.owner?.avatarUrl.alternate())
                    Text(
                        text = stringResource(
                            id = R.string.author,
                            repoDetailsResponse.owner?.login.alternate()
                        ),
                        modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16)),
                        color = colorResource(id = R.color.black),
                        style = MaterialTheme.typography.h5,
                    )
                }

                Text(
                    text = repoDetailsResponse.name.alternate(),
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.size_16)),
                    style = MaterialTheme.typography.h4,
                    color = colorResource(id = R.color.black)
                )

                Text(
                    text = repoDetailsResponse.description.alternate(),
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.size_16)),
                    color = colorResource(id = R.color.gray),
                    style = MaterialTheme.typography.subtitle1,
                )
                NumberIconTitle(
                    R.drawable.ic_star,
                    R.color.yellow,
                    R.string.stars_count,
                    repoDetailsResponse.stargazersCount.toString().alternate("0")
                )

                NumberIconTitle(
                    R.drawable.ic_watching,
                    R.color.gray,
                    R.string.watching_count,
                    repoDetailsResponse.watchersCount.toString().alternate("0")
                )

                NumberIconTitle(
                    R.drawable.ic_fork,
                    R.color.gray,
                    R.string.forks_count,
                    repoDetailsResponse.forksCount.toString().alternate("0")
                )

                NumberIconTitle(
                    R.drawable.ic_oppend_issues,
                    R.color.gray,
                    R.string.issues_count,
                    repoDetailsResponse.openIssuesCount.toString().alternate("0")
                )
            }
        }

        Button(
            onClick = {
                onIssuesClicked.invoke(repoDetailsResponse)
            }, modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_80))
                .padding(dimensionResource(id = R.dimen.size_16)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.black),
                contentColor = colorResource(id = R.color.white)
            )
        ) {
            Text(
                text = stringResource(id = R.string.issues),
                color = colorResource(id = R.color.white),
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.Medium
                ),
            )
        }

    }
}


@Composable
fun NumberIconTitle(
    @DrawableRes icon: Int,
    @ColorRes iconColor: Int,
    @StringRes label: Int,
    numberCount: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.size_8)),
    ) {
        Icon(
            painter = painterResource(id = icon), contentDescription = "Icon", tint = colorResource(
                id = iconColor
            )
        )

        Text(
            text = numberCount,
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_4)),
            style = MaterialTheme.typography.body2.copy(
                color = colorResource(id = R.color.black),
                fontWeight = FontWeight.Medium
            ),
        )
        Text(
            text = stringResource(
                id = label,
            ),
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_4)),
            style = MaterialTheme.typography.body2.copy(
                color = colorResource(id = R.color.black),
                fontWeight = FontWeight.Bold
            ),
        )
    }
}