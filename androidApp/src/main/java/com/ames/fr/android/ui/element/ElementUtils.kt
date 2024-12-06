package com.ames.fr.android.ui.element

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun drawableResourceId(name: String): Int {
    return LocalContext.current.resources.getIdentifier(
        name,
        "drawable",
        LocalContext.current.packageName
    )
}
