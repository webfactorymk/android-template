package mk.webfactory.template.feature.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import mk.webfactory.template.databinding.ViewTvShowItemBinding
import mk.webfactory.template.model.movie.Show
import mk.webfactory.template.model.movie.TvShow
import mk.webfactory.template.network.api.getImageUrl
import mk.webfactory.viewbinderadapter.BinderViewHolder
import mk.webfactory.viewbinderadapter.SupportedTypes
import mk.webfactory.viewbinderadapter.TypeViewBinder

class TvShowTypeBinder(val onItemClicked: (Show) -> Unit) :
    TypeViewBinder<TvShowTypeBinder.TvShowViewHolder, TvShow> {

    override fun getSupportedTypes(): SupportedTypes<out TvShow> = SupportedTypes.newBuilder<TvShow>()
        .add(TvShow::class.java)
        .build()

    override fun createViewHolder(parent: ViewGroup, inflater: LayoutInflater) =
        TvShowViewHolder(ViewTvShowItemBinding.inflate(inflater, parent, false))

    override fun bindViewHolder(holder: TvShowTypeBinder.TvShowViewHolder, item: TvShow) {
        holder.bind(item)
    }

    inner class TvShowViewHolder(val binding: ViewTvShowItemBinding) :
        BinderViewHolder(binding.root) {

        fun bind(item: TvShow) {
            binding.root.setOnClickListener { onItemClicked(item) }
            binding.txtTvTitle.text = item.title
            binding.txtTvAvgScore.text = item.voteAverage.toString()
            if (item.image != null) {
                Picasso.get().load(getImageUrl(item.image)).into(binding.imgTv)
            } else {
                binding.imgTv.setImageDrawable(null)
            }
        }
    }
}
