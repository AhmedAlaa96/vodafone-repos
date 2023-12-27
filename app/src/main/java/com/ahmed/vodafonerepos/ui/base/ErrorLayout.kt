package com.ahmed.vodafonerepos.ui.base

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ahmed.vodafonerepos.R
import com.ahmed.vodafonerepos.ui.theme.Black
import com.ahmed.vodafonerepos.ui.theme.White

@Composable
fun ErrorLayout(
    @DrawableRes icon: Int,
    title: String,
    subTitle: String? = null,
    onRetryBtnClicked: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "error icon",
            tint = MaterialTheme.colors.secondaryVariant,
            modifier = Modifier.size(
                dimensionResource(id = R.dimen.size_100)
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16)))
        Text(
            text = title,
            style = MaterialTheme.typography.h4
                .copy(
                    color = MaterialTheme.colors.primary
                ),
            textAlign = TextAlign.Center,
        )
        if (!subTitle.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16)))
            Text(
                text = subTitle,
                style = MaterialTheme.typography.body2.copy(
                    color = MaterialTheme.colors.secondaryVariant
                ),
                textAlign = TextAlign.Center,
            )
        }

        if (onRetryBtnClicked != null) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16)))
            Button(
                onClick = {
                    onRetryBtnClicked.invoke()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_80))
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.size_50),
                        vertical = dimensionResource(id = R.dimen.size_16)
                    ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Black,
                    contentColor = White
                )
            ) {
                Text(
                    text = stringResource(id = R.string.retry),
                    style = MaterialTheme.typography.body2.copy(
                        fontWeight = FontWeight.Medium,
                        color = White,
                    ),
                )
            }
        }

    }

}