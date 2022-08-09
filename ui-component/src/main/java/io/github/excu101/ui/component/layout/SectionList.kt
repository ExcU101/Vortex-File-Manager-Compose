package io.github.excu101.ui.component.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.ui.component.icon.Unspecified
import io.github.excu101.ui.component.icon.isNotUnspecified
import io.github.excu101.ui.component.layout.SectionListDefaults.SectionListColors
import io.github.excu101.ui.component.layout.SectionListDefaults.colors
import io.github.excu101.ui.component.text.Subtitle
import io.github.excu101.ui.component.text.SubtitleTextStyle

@Composable
fun SelectableSectionList(
    modifier: Modifier = Modifier,
    colors: SectionListColors = colors(),
    sections: Collection<GroupAction>,
    onActionClick: (Action) -> Unit,
    selectedActions: List<Action>,
) {
    LazyColumn(
        modifier = modifier,
        content = {
            sections.forEach { (title, icon, actions) ->
                item {
                    if (title.isEmpty()) {
                        Divider()
                    } else {
                        Column {
                            Divider()
                            Row(modifier = Modifier.padding(16.dp)) {
                                if (icon != null && icon != Unspecified) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = title
                                    )
                                    RowSpacer(size = 32.dp)
                                }
                                Subtitle(
                                    text = title,
                                    style = SubtitleTextStyle.copy(
                                        colors.title().value
                                    )
                                )
                            }
                        }
                    }
                }

                items(actions.toList()) { item ->
                    val isSelected = selectedActions.contains(item)

                    Surface(
                        modifier = Modifier.padding(
                            vertical = 4.dp,
                            horizontal = 8.dp
                        ),
                        color = colors.itemBackground(isSelected = isSelected).value,
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .clickable(onClick = { onActionClick(item) })
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = CenterVertically
                        ) {
                            if (item.icon.isNotUnspecified()) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = colors.itemIcon(isSelected = isSelected).value
                                )
                                RowSpacer(size = 32.dp)
                            }

                            Text(
                                text = item.title,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = colors.itemTitle(isSelected = isSelected).value
                                )
                            )
                        }
                    }
                }
            }
        }
    )
}

object SectionListDefaults {

    interface SectionListColors {
        @Composable
        fun title(): State<Color>

        @Composable
        fun itemBackground(isSelected: Boolean): State<Color>

        @Composable
        fun itemTitle(isSelected: Boolean): State<Color>

        @Composable
        fun itemIcon(isSelected: Boolean): State<Color>
    }

    internal class DefaultSectionListColors(
        private val title: Color,
        private val itemBackground: Color,
        private val itemBackgroundSelected: Color,
        private val itemTitle: Color,
        private val itemTitleSelected: Color,
        private val itemIcon: Color,
        private val itemIconSelected: Color
    ) : SectionListColors {
        @Composable
        override fun title(): State<Color> {
            return rememberUpdatedState(newValue = title)
        }

        @Composable
        override fun itemBackground(isSelected: Boolean): State<Color> {
            return rememberUpdatedState(newValue = if (isSelected) itemBackgroundSelected else itemBackground)
        }

        @Composable
        override fun itemTitle(isSelected: Boolean): State<Color> {
            return rememberUpdatedState(newValue = if (isSelected) itemTitleSelected else itemTitle)
        }

        @Composable
        override fun itemIcon(isSelected: Boolean): State<Color> {
            return rememberUpdatedState(newValue = if (isSelected) itemIconSelected else itemIcon)
        }

    }

    @Composable
    fun colors(
        title: Color = MaterialTheme.colors.primary,
        itemBackground: Color = MaterialTheme.colors.surface,
        itemBackgroundSelected: Color = MaterialTheme.colors.onSurface,
        itemTitle: Color = MaterialTheme.colors.primary,
        itemTitleSelected: Color = MaterialTheme.colors.secondary,
        itemIcon: Color = MaterialTheme.colors.primary,
        itemIconSelected: Color = MaterialTheme.colors.secondary
    ): SectionListColors {
        return DefaultSectionListColors(
            title = title,
            itemBackground = itemBackground,
            itemBackgroundSelected = itemBackgroundSelected,
            itemTitle = itemTitle,
            itemTitleSelected = itemTitleSelected,
            itemIcon = itemIcon,
            itemIconSelected = itemIconSelected
        )
    }

}