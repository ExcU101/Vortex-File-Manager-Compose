package io.github.excu101.vortexfilemanager.ui.screen.plugin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.excu101.pluginsystem.provider.PluginManager

@Composable
fun PluginScreen() {
    Column(
        modifier = Modifier.fillMaxSize()

    ) {
        PluginManager.plugins.forEach {
            Text(text = it.attributes.name + " | " + it.attributes.version + " | " + it.attributes.packageName)
        }
    }
}