package com.ahmed.vodafonerepos.ui.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.ahmed.vodafonerepos.R

@Composable
fun NetworkImage(
    imageUrl: String, contentDescription: String? = null, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(Color.Transparent)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = Modifier.size(dimensionResource(id = R.dimen.size_50)),
            contentScale = ContentScale.Fit,

        )
    }
}
