package xyz.egie.cortado.view

import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.cardview.widget.CardView
import androidx.core.graphics.ColorUtils
import kotlinx.android.synthetic.main.view_cortado_status_toggle.view.*
import xyz.egie.cortado.R
import kotlin.time.*

@ExperimentalTime
class CortadoStatusToggleView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    init {
        View.inflate(context, R.layout.view_cortado_status_toggle, this)
        radius = resources.getDimensionPixelSize(R.dimen.cortado_card_radius).toFloat()

        toggleSwitch.isClickable = false
        setCardBackgroundColor(resources.getColor(R.color.secondary_background, null))
        cardElevation = resources.getDimensionPixelSize(R.dimen.card_elevation_low).toFloat()

        this.layoutTransition = LayoutTransition().apply {
            enableTransitionType(LayoutTransition.CHANGING)
        }
    }

    private var isChecked: Boolean = false
    private var lastTimeoutDuration: Duration? = null
    private var currentAnimator: ValueAnimator? = null

    fun setChecked(isChecked: Boolean, timeout: Duration) {
        if (isChecked == this.isChecked && timeout == lastTimeoutDuration) return

        this.isChecked = isChecked
        this.lastTimeoutDuration = timeout
        animateToStatus(isChecked)

        toggleSwitch.isChecked = isChecked
        headerTextView.setText(
            if (isChecked) {
                R.string.cortado_is_enabled
            } else {
                R.string.cortado_is_disabled
            }
        )

        descriptionTextView.text = if (timeout.inMilliseconds.toInt() == Int.MAX_VALUE) {
            resources.getString(R.string.timeout_max_time)
        } else {
            resources.getString(R.string.timeout_time_format, timeout.toPhrase(context))
        }
    }

    fun setOnToggleListener(listener: (wasChecked: Boolean) -> Unit) {
        setOnClickListener { listener(isChecked) }
    }

    private fun animateToStatus(checked: Boolean) {
        this.currentAnimator?.cancel()

        val animator = ValueAnimator.ofFloat(0f, 1f)

        val startElevation = cardElevation
        val endElevation = resources.getDimensionPixelSize(
            if (checked) R.dimen.card_elevation_high else R.dimen.card_elevation_low
        )
        val startCardColor = cardBackgroundColor.defaultColor
        val endCardColor = resources.getColor(
            if (checked) R.color.accent_background else R.color.secondary_background, null
        )
        val startTextColor = headerTextView.currentTextColor
        val endTextColor = resources.getColor(
            if (checked) R.color.on_accent_background else R.color.on_secondary_background, null
        )

        animator.addUpdateListener { animation ->
            val animationPercent = animation.animatedValue as Float

            val elevation = startElevation + (endElevation - startElevation) * animationPercent
            val cardColor = ColorUtils.blendARGB(startCardColor, endCardColor, animationPercent)
            val textColor = ColorUtils.blendARGB(startTextColor, endTextColor, animationPercent)

            this.cardElevation = elevation
            this.setCardBackgroundColor(cardColor)
            headerTextView.setTextColor(textColor)
            descriptionTextView.setTextColor(textColor)
        }

        animator.duration = 600L
        animator.interpolator = DecelerateInterpolator()
        animator.start()

        this.currentAnimator = animator
    }

    private fun Duration.toPhrase(context: Context): String {
        var milliseconds = this.inMilliseconds.toInt()

        val extractedDays = milliseconds / (1000 * 60 * 60 * 24)
        milliseconds -= extractedDays.days.inMilliseconds.toInt()

        val extractedHours = milliseconds / (1000 * 60 * 60)
        milliseconds -= extractedHours.hours.inMilliseconds.toInt()

        val extractedMinutes = milliseconds / (1000 * 60)
        milliseconds -= extractedMinutes.minutes.inMilliseconds.toInt()

        val extractedSeconds = milliseconds / 1000
        milliseconds -= extractedSeconds.seconds.inMilliseconds.toInt()

        val timeComponents = listOfNotNull(
            extractedDays.takeIf { it > 0 }
                ?.let { context.resources.getQuantityString(R.plurals.time_format_days, it, it) },
            extractedHours.takeIf { it > 0 }
                ?.let { context.resources.getQuantityString(R.plurals.time_format_hours, it, it) },
            extractedMinutes.takeIf { it > 0 }
                ?.let { context.resources.getQuantityString(R.plurals.time_format_minutes, it, it) },
            extractedSeconds.takeIf { it > 0 }
                ?.let { context.resources.getQuantityString(R.plurals.time_format_seconds, it, it) }
        )

        return when (timeComponents.size) {
            0 -> context.resources.getQuantityString(R.plurals.time_format_seconds, 0, 0)
            1 -> timeComponents.first()
            2 -> {
                val separator = context.getString(R.string.time_separator_and)
                return timeComponents.joinToString(separator = " $separator ")
            }
            else -> {
                val commaSeparator = context.getString(R.string.time_separator_list)
                val andSeparator = context.getString(R.string.time_separator_and)
                return timeComponents.joinToString(separator = "$commaSeparator ") { component ->
                    if (component == timeComponents.last()) "$andSeparator $component" else component
                }
            }
        }
    }
}