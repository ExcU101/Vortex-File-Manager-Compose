package io.github.excu101.vortexfilemanager.ui.screen.list.view

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType
import io.github.excu101.filesystem.fs.utils.properties
import io.github.excu101.vortexfilemanager.data.FileModel
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.*

@Composable
fun StorageItem(
    modifier: Modifier = Modifier,
    model: FileModel,
    isSelected: Boolean = false,
    onItemClick: () -> Unit = { },
    onIconClick: () -> Unit = { },
) {
    val transition =
        updateTransition(targetState = isSelected, label = "fileItemSelectedTransition")

    val surfaceColor by transition.animateColor(
        label = "surfaceColor",
        transitionSpec = { tween(300) }
    ) {
        Theme {
            if (it)
                fileItemSurfaceSelectedColorKey
            else
                fileItemSurfaceColorKey
        }
    }

    val iconBackgroundColor by transition.animateColor(
        label = "iconBackgroundColor",
        transitionSpec = { tween(300) }
    ) {
        Theme {
            if (it)
                fileItemIconBackgroundSelectedColorKey
            else
                fileItemIconBackgroundColorKey
        }
    }

    val iconColor by transition.animateColor(
        label = "iconColor",
        transitionSpec = { tween(300) }
    ) {
        Theme {
            if (it)
                fileItemIconSelectedTintColorKey
            else
                fileItemIconTintColorKey
        }
    }

    val titleColor by transition.animateColor(
        label = "titleColor",
        transitionSpec = { tween(300) }
    ) {
        Theme {
            if (it)
                fileItemTitleSelectedTextColorKey
            else
                fileItemTitleTextColorKey
        }
    }

    val secondaryColor by transition.animateColor(
        label = "secondaryColor",
        transitionSpec = { tween(300) }
    ) {
        Theme {
            if (it)
                fileItemSecondarySelectedTextColorKey
            else
                fileItemSecondaryTextColorKey
        }
    }

    Surface(
        modifier = modifier
            .clickable(onClick = onItemClick),
        color = surfaceColor,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .heightIn(min = 56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .background(
                        color = iconBackgroundColor,
                        shape = RoundedCornerShape(100),
                    )
                    .padding(all = 8.dp)
                    .clickable(
                        onClick = onIconClick,
                        indication = rememberRipple(bounded = false),
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ),
                imageVector = if (model.isDirectory) {
                    Icons.Outlined.Folder
                } else {
                    Icons.Outlined.InsertDriveFile
                },
                contentDescription = null,
                tint = iconColor
            )

            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = model.name,
                    style = TextStyle(
                        color = titleColor,
                        fontSize = 16.sp
                    ),
                )
                Text(
                    text = parseContent(model),
                    style = TextStyle(
                        color = secondaryColor,
                        fontSize = 14.sp
                    ),
                )
            }
            Spacer(modifier = Modifier.weight(1F, true))
        }
    }
}

private fun parseContent(model: FileModel): String {

    val content =
        if (model.isDirectory) with(model.path.properties().count) {
            when (this) {
                0 -> {
                    "Empty"
                }
                1 -> {
                    "Item"
                }
                else -> "Items: $this"
            }
        }
        else MimeType.fromName(model.name).toString()

    val size = model.size.toString()

    return "" + (if (content.isNotEmpty()) "$content | " else "") + size
}