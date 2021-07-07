package mk.webfactory.template.feature.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import mk.webfactory.template.databinding.ViewMovieItemBinding
import mk.webfactory.template.model.movie.Movie
import mk.webfactory.template.model.movie.Show
import mk.webfactory.template.network.api.getImageUrl
import mk.webfactory.viewbinderadapter.BinderViewHolder
import mk.webfactory.viewbinderadapter.SupportedTypes
import mk.webfactory.viewbinderadapter.TypeViewBinder

class MovieTypeBinder(val onItemClicked: (Show) -> Unit) :
    TypeViewBinder<MovieTypeBinder.MovieViewHolder, Movie> {

    override fun getSupportedTypes(): SupportedTypes<out Movie> = SupportedTypes.newBuilder<Movie>()
        .add(Movie::class.java)
        .build()

    override fun createViewHolder(parent: ViewGroup, inflater: LayoutInflater) =
        MovieViewHolder(ViewMovieItemBinding.inflate(inflater, parent, false))

    override fun bindViewHolder(holder: MovieTypeBinder.MovieViewHolder, item: Movie) {
        holder.bind(item)
    }

    inner class MovieViewHolder(val binding: ViewMovieItemBinding) :
        BinderViewHolder(binding.root) {

        fun bind(item: Movie) {
            binding.root.setOnClickListener { onItemClicked(item) }
            binding.txtMovieTitle.text = item.title
            binding.txtMovieDescription.text = item.overview
            binding.txtMovieAvgScore.text = item.voteAverage.toString()
            if (item.image != null) {
                Picasso.get().load(getImageUrl(item.image)).into(binding.imgMovie)
            } else {
                binding.imgMovie.setImageDrawable(null)
            }
        }
    }
}
