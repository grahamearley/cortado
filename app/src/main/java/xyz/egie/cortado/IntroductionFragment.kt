package xyz.egie.cortado

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_introduction.*

class IntroductionFragment : Fragment(R.layout.fragment_introduction) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val instructionText = getString(R.string.instruction_text)
        val iconSpan = ImageSpan(getTextSizedIcon())

        val indexOfIcon = instructionText.indexOf('*')
        val spannableStringBuilder = SpannableStringBuilder(instructionText)
        spannableStringBuilder.setSpan(iconSpan, indexOfIcon, indexOfIcon + 1, SpannableStringBuilder.SPAN_INCLUSIVE_INCLUSIVE)

        instructionTextView.text = spannableStringBuilder
    }

    private fun getTextSizedIcon(): Drawable {
        val lineHeight = instructionTextView.lineHeight
        val iconDrawable = resources.getDrawable(R.drawable.ic_cortado, null).mutate()

        val iconAspectRatio = iconDrawable.intrinsicHeight / iconDrawable.intrinsicWidth.toFloat()

        val iconHeight = (lineHeight * 0.9f).toInt()
        val iconWidth = (iconHeight / iconAspectRatio).toInt()

        iconDrawable.setBounds(0, 0, iconWidth, iconHeight)

        return iconDrawable
    }
}