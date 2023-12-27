package com.ahmed.vodafonerepos.ui.base

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

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
                    color = MaterialTheme.colors.secondary,
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
        backgroundColor = Color.Black,
        contentColor = Color.White
    )

}