package com.ahmed.vodafonerepos.ui.repodetails

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmed.vodafonerepos.R
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsResponse
import com.ahmed.vodafonerepos.ui.reposlist.ReposList
import com.ahmed.vodafonerepos.utils.Utils
import com.ahmed.vodafonerepos.utils.alternate
import com.ahmed.vodafonerepos.utils.utilities.UIUtils


@Composable
fun RepoDetailsContent(
    repoDetailsResponse: RepoDetailsResponse,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
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
                    text = repoDetailsResponse.name.alternate(),
                    color = colorResource(id = R.color.black),
                    fontSize = 18.sp
                )
                Text(
                    text = repoDetailsResponse.owner?.login.alternate(),
                    modifier = Modifier.padding(top = 16.dp),
                    color = colorResource(id = R.color.black),
                    fontSize = 14.sp
                )
                Text(
                    text = repoDetailsResponse.description.alternate(),
                    modifier = Modifier.padding(top = 16.dp),
                    color = colorResource(id = R.color.black),
                    fontSize = 14.sp
                )
                Text(
                    text = repoDetailsResponse.stargazersCount.toString().alternate("0"),
                    modifier = Modifier.padding(top = 16.dp),
                    color = colorResource(id = R.color.black),
                    fontSize = 14.sp
                )
            }
        }

        Button(onClick = {
            UIUtils.showToast(context = context, "Clicked on Issue")
        }) {
            Text(text = "Issues")
        }
    }

}