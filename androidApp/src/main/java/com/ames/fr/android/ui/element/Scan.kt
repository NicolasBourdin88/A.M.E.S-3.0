package com.ames.fr.android.ui.element

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun Scan(onClick: () -> Unit) {
    Log.e("nicolas", "Scan")
    val fileName = "scan"

    val painter = painterResource(id = drawableResourceId(fileName))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.BottomEnd)
                .clickable { onClick.invoke() }
        )
    }
}
