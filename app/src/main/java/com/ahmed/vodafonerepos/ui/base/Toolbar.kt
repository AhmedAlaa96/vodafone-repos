package com.ahmed.vodafonerepos.ui.base

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import com.ahmed.vodafonerepos.R
import com.ahmed.vodafonerepos.ui.theme.Black
import com.ahmed.vodafonerepos.ui.theme.White

@Composable
fun Toolbar(
    title: String,
    hasBackButton: Boolean = false,
    onBackButtonClicked: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.body1.copy(
                    color = White,
                    fontWeight = FontWeight.SemiBold
                )
            )
        },
        navigationIcon = {
            if (hasBackButton) {
                IconButton(onClick = { onBackButtonClicked?.invoke() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        },
        backgroundColor = Black,
        contentColor = White
    )

}