package io.github.excu101.vortexfilemanager.ui.screen.main.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.ui.component.button.IconButtonContainer
import io.github.excu101.ui.component.layout.RowSpacer
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.*
import io.github.excu101.vortexfilemanager.util.item
import io.github.excu101.vortexfilemanager.util.listBuilder

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainNavigationMenu(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        bottom = 64.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    ),
    onActionClick: (Action) -> Unit,
    currentSelected: Int = 0,
    navigationMenu: List<Action> = listBuilder {
        item(title = "Storage", icon = Icons.Outlined.Folder)
        item(title = "Plugin manager", icon = Icons.Outlined.AddCircleOutline)
        item(title = "Settings", icon = Icons.Outlined.Settings)
    },
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        stickyHeader {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .background(
                            color = Theme[accentColorKey],
                            shape = RoundedCornerShape(100),
                        )
                        .padding(all = 8.dp),
                    imageVector = Icons.Outlined.Folder,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {

                }
                Spacer(modifier = Modifier.weight(1F))
                IconButtonContainer(
                    modifier = Modifier,
                    vector = if (Theme.isDark) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                    onClick = {
                        Theme.isDark = !Theme.isDark
                    }
                )
            }
        }

        itemsIndexed(navigationMenu) { index, item ->
            val isSelected by remember(currentSelected) { derivedStateOf { index == currentSelected } }

            Surface(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                color = Theme {
                    if (isSelected)
                        mainDrawerItemSelectedBackgroundColorKey
                    else
                        mainDrawerSurfaceColorKey
                },
                shape = RoundedCornerShape(16.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .clickable(onClick = { onActionClick(item) }),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = Theme {
                            if (isSelected)
                                mainDrawerActionIconSelectedTintColorKey
                            else
                                mainDrawerActionIconTintColorKey
                        }
                    )
                    RowSpacer(size = 32.dp)
                    Text(
                        text = item.title,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Theme {
                                if (isSelected)
                                    mainDrawerActionIconSelectedTintColorKey
                                else
                                    mainDrawerActionTitleTextColorKey
                            }
                        )
                    )
                }
            }
        }
    }
}

