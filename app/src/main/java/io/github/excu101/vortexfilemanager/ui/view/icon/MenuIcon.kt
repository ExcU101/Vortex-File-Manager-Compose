package io.github.excu101.vortexfilemanager.ui.view.icon

import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.mainBarNavigationIconTintColorKey

enum class MenuIconState {
    CLOSE,
    MENU,
    ARROW_BACK
}

@Composable
fun NavigationMenuIcon(
    modifier: Modifier = Modifier,
    state: MenuIconState,
    contentDescription: String? = null,
    @FloatRange(from = 0.0, to = 1.0)
    progress: Float = animateFloatAsState(targetValue = if (state != MenuIconState.MENU) 1.0F else 0.0F).value,
    thickness: Int = 2,
    gap: Int = 5,
    color: Color = Theme[mainBarNavigationIconTintColorKey],
) {
    val semantics = if (contentDescription == null) {
        Modifier
    } else {
        Modifier.semantics {
            this.contentDescription = contentDescription
        }
    }

    Canvas(
        modifier = modifier.then(semantics),
        onDraw = {
            if (progress < 0.5) {
                drawMenu(
                    scope = this,
                    progress = progress,
                    thickness = thickness,
                    color = color,
                    gap = gap
                )
            } else {
                drawCross(
                    scope = this,
                    progress = progress,
                    thickness = thickness,
                    color = color,
                    gap = gap
                )
            }
        }
    )
}

private fun drawMenu(
    scope: DrawScope,
    progress: Float,
    thickness: Int,
    gap: Int,
    color: Color,
) = with(scope) {
    val bounds = size.toRect()
    val pixelThickness: Float = thickness.dp.toPx()
    val middlePixelThickness: Float = pixelThickness / 2.0F
    val pixelGap: Float = gap.dp.toPx()
    val offsetY: Float = pixelGap * (2 * (0.5F - progress))
    val rotation = lerp(start = 0F, stop = 180F, fraction = progress)

    val centerY = center.y

    val startX = (0F + 2.dp.toPx() + middlePixelThickness)
    val endX = (size.width - 2.dp.toPx() - middlePixelThickness)

    val startY = centerY + offsetY
    val endY = centerY - offsetY

    rotate(rotation) {
        // Top line
        drawLine(
            color = color,
            start = Offset(x = startX, y = endY),
            end = Offset(x = endX, y = endY),
            strokeWidth = pixelThickness,
            cap = StrokeCap.Butt
        )

        // Center line
        drawLine(
            color = color,
            start = Offset(x = startX, y = centerY),
            end = Offset(x = endX, y = centerY),
            strokeWidth = pixelThickness,
            cap = StrokeCap.Butt
        )

        // Bottom line
        drawLine(
            color = color,
            start = Offset(x = startX, y = startY),
            end = Offset(x = endX, y = startY),
            strokeWidth = pixelThickness,
            cap = StrokeCap.Butt
        )
    }
}

private fun drawCross(
    scope: DrawScope,
    progress: Float,
    thickness: Int,
    gap: Int,
    color: Color,
) = with(scope) {
    val path = Path()

    val pixelThickness = thickness.dp.toPx()
    val pixelGap = gap.dp.toPx()
    val centerX = center.x
    val centerY = center.y
    val height = pixelGap + pixelThickness * 3
    val centerHeight = height / 2

    val distanceY = centerHeight * (2 * (progress - 0.5F))

    val startX: Float = centerX - centerHeight
    val startY: Float = centerY - distanceY
    val endX: Float = centerX + centerHeight
    val endY: Float = centerY + distanceY

    val rotation = lerp(start = 0F, stop = 180F, fraction = progress)

    rotate(rotation) {
        drawLine(
            color = color,
            start = Offset(x = startX, y = startY),
            end = Offset(x = endX, y = endY),
            strokeWidth = pixelThickness,
            cap = StrokeCap.Butt
        )

        drawLine(
            color = color,
            start = Offset(x = startX, y = endY),
            end = Offset(x = endX, y = startY),
            strokeWidth = pixelThickness,
            cap = StrokeCap.Butt
        )
    }
}

private fun drawArrowBack(
    scope: DrawScope,
) = with(scope) {

}