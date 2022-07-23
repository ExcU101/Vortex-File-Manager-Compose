package io.github.excu101.vortexfilemanager.ui.view.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.ui.component.text.Subtitle
import io.github.excu101.vortexfilemanager.ui.view.action.ActionItem

@Composable
fun SectionList(
    modifier: Modifier = Modifier,
    sections: Collection<Pair<String, Collection<Action>>>,
    onActionClick: (Action) -> Unit,
//    contentPaddings: PaddingValues,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            bottom = 64.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        ),
        content = {
            sections.forEach { (title, actions) ->
                item {
                    Subtitle(
                        text = title,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                actions.forEach { action ->
                    item {
                        ActionItem(
                            action = action,
                            modifier = Modifier.clickable {
                                onActionClick(action)
                            }
                        )
                    }
                }
            }
        }
    )
}