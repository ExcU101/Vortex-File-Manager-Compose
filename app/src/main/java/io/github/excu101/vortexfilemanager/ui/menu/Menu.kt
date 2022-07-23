package io.github.excu101.vortexfilemanager.ui.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.excu101.pluginsystem.model.Action

interface Menu {
    val title: String
    val icon: ImageVector
    val sections: Set<Section>
    val actions: Set<Menu>
}

interface Section {
    val title: String
    val icon: ImageVector?
    val actions: Set<Menu>
}

@Immutable
internal class MenuImpl(
    override val title: String,
    override val icon: ImageVector,
    override val sections: Set<Section>,
    override val actions: Set<Menu>
) : Menu

@Immutable
internal class SectionImpl(
    override val title: String,
    override val icon: ImageVector?,
    override val actions: Set<Menu>,
) : Section

@DslMarker
annotation class MenuMarker

@MenuMarker
class MenuBuilder constructor(
    var title: String,
    var icon: ImageVector,
) {

    private val _sections: MutableSet<Section> = mutableSetOf()
    val sections: Set<Section>
        get() = _sections

    private val _actions: MutableSet<Menu> = mutableSetOf()
    val actions: Set<Menu>
        get() = _actions

    fun registerSection(section: Section): Boolean = _sections.add(section)

    fun registerAction(action: Menu): Boolean = _actions.add(action)

    fun build(): Menu = MenuImpl(
        title = title,
        icon = icon,
        sections = sections,
        actions = actions
    )
}

@MenuMarker
class SectionBuilder(
    var title: String,
    var icon: ImageVector? = null
) {
    private val _actions: MutableSet<Menu> = mutableSetOf()
    val actions: Set<Menu>
        get() = _actions

    fun registerAction(action: Menu): Boolean = _actions.add(action)

    fun build(): Section {
        return SectionImpl(
            title = title,
            icon = icon,
            actions = actions
        )
    }
}

inline fun menu(block: MenuBuilder.() -> Unit): Menu {
    return MenuBuilder(title = "", icon = Icons.Outlined.Add).apply(block).build()
}

context(MenuBuilder)
fun Action.register() {
    MenuImpl(title = title, icon = icon, sections = setOf(), actions = setOf())
}

inline fun MenuBuilder.section(block: SectionBuilder.() -> Unit): Boolean {
    return registerSection(section = SectionBuilder(title = "Empty section").apply(block).build())
}

inline fun MenuBuilder.action(block: MenuBuilder.() -> Unit): Boolean {
    val action = MenuBuilder(
        title = "Empty action",
        icon = Icons.Outlined.Add
    ).apply(block).build()

    return registerAction(
        action
    )
}

inline fun SectionBuilder.action(block: MenuBuilder.() -> Unit): Boolean {
    val action = MenuBuilder(
        title = "Empty action",
        icon = Icons.Outlined.Add,
    ).apply(block).build()

    return registerAction(
        action
    )
}

context (MenuBuilder)
fun Pair<String, ImageVector>.asAction(): Boolean {
    return registerAction(
        MenuImpl(
            title = first,
            icon = second,
            sections = sections,
            actions = actions
        )
    )
}

data class CovariantMenu(
    val title: String,
    val icon: ImageVector,
    val actions: Set<Menu> = setOf()
)

context(SectionBuilder) infix fun String.withIcon(icon: ImageVector): CovariantMenu {
    return CovariantMenu(
        title = this,
        icon = icon
    )
}

context(SectionBuilder) infix fun CovariantMenu.withActions(actions: Set<Menu>): CovariantMenu {
    return copy(actions = actions)
}

infix fun CovariantMenu.registerAsActionTo(builder: SectionBuilder): Boolean {
    return builder.registerAction(
        MenuImpl(
            title = title,
            icon = icon,
            sections = setOf(),
            actions = actions
        )
    )
}

object ActionConditions {
    val conditions: MutableList<ActionCondition> = mutableListOf()

    fun notifyConditions(action: Action) = notifyConditions(listOf(action))

    fun notifyConditions(actions: List<Action>) {
        actions.forEach { action ->
            conditions.forEach { actionCondition ->
                if (actionCondition.condition.invoke(action)) {
                    actionCondition.onSuccess()
                }
            }
        }
    }

    class ActionCondition(
        internal val condition: (Action) -> Boolean,
        internal val onSuccess: () -> Unit
    )

    class ActionConditionBuilder(
        var condition: (Action) -> Boolean = { false },
        var onSuccess: () -> Unit = {},
    ) {
        fun build(): ActionCondition =
            ActionCondition(condition, onSuccess)
    }
}

context(MenuBuilder)
fun MenuBuilder.registerCondition(condition: (Action) -> Boolean, onSuccess: () -> Unit) {
    ActionConditions.conditions.add(ActionConditions.ActionCondition(condition, onSuccess))
}

context(MenuBuilder)
fun MenuBuilder.registerCondition(block: ActionConditions.ActionConditionBuilder.() -> Unit) {
    val builder = ActionConditions.ActionConditionBuilder().apply(block)
    ActionConditions.conditions.add(builder.build())
}
