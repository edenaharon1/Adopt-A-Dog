import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.adoptadog.R
import com.example.adoptadog.database.Post
import com.squareup.picasso.Picasso

class PostAdapter(
    private var posts: MutableList<Post>,
    private val navController: NavController // הוספנו NavController כפרמטר
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postImageView: ImageView = itemView.findViewById(R.id.postImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)

        val displayMetrics = parent.context.resources.displayMetrics
        val itemWidth = displayMetrics.widthPixels / 2
        val layoutParams = itemView.layoutParams
        layoutParams.width = itemWidth
        layoutParams.height = itemWidth
        itemView.layoutParams = layoutParams

        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentPost = posts[position]

        Picasso.get()
            .load(currentPost.imageUrl)
            .fit()
            .centerCrop()
            .into(holder.postImageView)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putLong("postId", currentPost.id)
            navController.navigate(R.id.action_homePageFragment_to_fragmentPost, bundle) // ניווט ל FragmentPost
        }
    }

    override fun getItemCount() = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts.clear()
        posts.addAll(newPosts)
        notifyDataSetChanged()
    }
}