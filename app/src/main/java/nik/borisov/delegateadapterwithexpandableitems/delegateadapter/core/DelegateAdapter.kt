package nik.borisov.delegateadapterwithexpandableitems.delegateadapter.core

import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import nik.borisov.delegateadapterwithexpandableitems.animation.getValueAnimator

interface DelegateAdapter<I : DelegateAdapterItem, in VH : ViewHolder> {

    val modelClass: Class<out I>

    fun createViewHolder(parent: ViewGroup): ViewHolder

    fun onViewRecycled(viewHolder: VH) = Unit

    fun onViewDetachedFromWindow(viewHolder: VH) = Unit

    fun onViewAttachedToWindow(viewHolder: VH)
}

abstract class DelegateAdapterSimple<I : DelegateAdapterItem, in VH : ViewHolder>(
    override val modelClass: Class<out I>
) : DelegateAdapter<I, VH> {

    abstract fun bindViewHolder(
        item: I,
        viewHolder: VH,
        payloads: List<DelegateAdapterItem.Payloadable>
    )

    override fun onViewAttachedToWindow(viewHolder: VH) = Unit
}

abstract class DelegateAdapterWithExpandableItem<I : DelegateAdapterItem, in VH : ExpandableItemViewHolder>(
    override val modelClass: Class<out I>
) : DelegateAdapter<I, VH> {

    protected var originalHeight = -1
    protected var expandedHeight = -1

    private val animationSpeed = 0.8f
    protected val listItemExpandDuration: Long = (300L / animationSpeed).toLong()

    open fun bindViewHolder(
        item: I,
        viewHolder: VH,
        payloads: List<DelegateAdapterItem.Payloadable>,
        onExpandItem: (expandableContainer: View) -> Unit
    ) {
        viewHolder.bind(item)
        onExpandItem(
            viewHolder.expandableContainer
        )
    }

    open fun expandItem(viewHolder: VH, expand: Boolean, animate: Boolean) {
        if (animate) {
            val animator = getValueAnimator(
                expand, listItemExpandDuration, AccelerateDecelerateInterpolator()
            ) { progress -> setExpandProgress(viewHolder, progress) }

            if (expand) animator.doOnStart { viewHolder.expandView.isVisible = true }
            else animator.doOnEnd { viewHolder.expandView.isVisible = false }

            animator.start()
        } else {

            // show expandView only if we have expandedHeight (onViewAttached)
            viewHolder.expandView.isVisible = expand && expandedHeight >= 0
            setExpandProgress(viewHolder, if (expand) 1f else 0f)
        }
    }

    override fun onViewAttachedToWindow(viewHolder: VH) {
        // get originalHeight & expandedHeight if not gotten before
        if (expandedHeight < 0) {
            expandedHeight = 0 // so that this block is only called once

            viewHolder.expandableContainer.doOnLayout { view ->
                originalHeight = view.height

                // show expandView and record expandedHeight in next layout pass
                // (doOnPreDraw) and hide it immediately. We use onPreDraw because
                // it's called after layout is done. doOnNextLayout is called during
                // layout phase which causes issues with hiding expandView.
                viewHolder.expandView.isVisible = true
                view.doOnPreDraw {
                    expandedHeight = view.height
                    viewHolder.expandView.isVisible = false
                }
            }
        }
    }

    open fun setExpandProgress(viewHolder: VH, progress: Float) {
        if (expandedHeight > 0 && originalHeight > 0) {
            viewHolder.expandableContainer.layoutParams.height =
                (originalHeight + (expandedHeight - originalHeight) * progress).toInt()
        }
        viewHolder.expandableContainer.requestLayout()
    }
}

abstract class ExpandableItemViewHolder(
    itemView: View
) : ViewHolder(itemView) {

    abstract val expandableContainer: View
    abstract val expandView: View

    abstract fun bind(item: DelegateAdapterItem)


}