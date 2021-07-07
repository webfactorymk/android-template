package mk.webfactory.template.feature.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import mk.webfactory.template.model.movie.Show
import mk.webfactory.viewbinderadapter.BinderViewHolder
import mk.webfactory.viewbinderadapter.ViewBinderAdapter

class ShowItemViewAdapter(itemClickListener: (Show) -> Unit) :
    PagingDataAdapter<Show, BinderViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Show>() {
            override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean =
                oldItem == newItem
        }
    }

    private val binderAdapter: ViewBinderAdapter = ViewBinderAdapter()
        .addViewBinder(MovieTypeBinder(itemClickListener))
        .addViewBinder(TvShowTypeBinder(itemClickListener))


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BinderViewHolder {
        return binderAdapter.createViewHolder(parent, viewType, LayoutInflater.from(parent.context))
    }

    override fun onBindViewHolder(holder: BinderViewHolder, position: Int) {
        binderAdapter.bindViewHolder(holder, getItem(position)!!)
    }

    override fun getItemViewType(position: Int): Int {
        return binderAdapter.getItemViewType(getItem(position))
    }
}