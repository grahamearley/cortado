package xyz.egie.cortado

import android.database.DataSetObserver
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import kotlinx.android.synthetic.main.list_item_setting.view.*

class PreferenceSpinnerAdapter<T : Setting>(
    private val settings: List<T>
) : SpinnerAdapter {
    override fun isEmpty() = settings.isNotEmpty()
    override fun getCount() = settings.size
    override fun getItemViewType(position: Int) = 1
    override fun getViewTypeCount() = 1
    override fun getItem(position: Int) = settings[position]
    override fun hasStableIds() = false
    override fun unregisterDataSetObserver(p0: DataSetObserver?) {}
    override fun registerDataSetObserver(p0: DataSetObserver?) {}

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.list_item_setting, parent, false)

        val underlineSpan = UnderlineSpan()
        val spanBuilder = SpannableStringBuilder(getItem(position).getName(view.context))
        spanBuilder.setSpan(underlineSpan, 0, spanBuilder.length, SpannableStringBuilder.SPAN_INCLUSIVE_INCLUSIVE)

        view.settingTextView.text = spanBuilder

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.list_item_setting, parent, false)

        view.settingTextView.text = getItem(position).getName(view.context)

        return view
    }

    override fun getItemId(position: Int): Long {
        val item = settings.getOrNull(position) ?: return -1
        return item.hashCode().toLong()
    }
}