
import android.util.Log
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
    private val navController: NavController,
    private val onPostClicked: ((Post) -> Unit)? = null,
    var onItemClick: ((Post) -> Unit)? = null,
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
        Log.d("PostAdapter", "Loading image from: ${currentPost.imageUrl}")
        holder.postImageView.invalidate()
        holder.setIsRecyclable(false)

        Picasso.get()
            .load(currentPost.imageUrl)
            .fit()
            .centerCrop()
            .into(holder.postImageView, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    Log.d("PostAdapter", "Image loaded successfully: ${currentPost.imageUrl}")
                }

                override fun onError(e: Exception?) {
                    Log.e("PostAdapter", "Error loading image: ${currentPost.imageUrl}", e)
                    // כאן אתה יכול להציג תמונת ברירת מחדל או לטפל בשגיאה אחרת
                }
            })

        holder.itemView.setOnClickListener {
            if (onPostClicked != null) {
                onPostClicked.invoke(currentPost)
            } else if (onItemClick != null) {
                onItemClick?.invoke(currentPost)
            } else {
                // Remove or comment out the navigation code to avoid the error
                // val bundle = Bundle()
                // bundle.putString("postId", currentPost.id.toString())
                // navController.navigate(R.id.action_homePageFragment_to_fragmentPost, bundle)
                Log.w("PostAdapter", "No click listener defined. Navigation skipped.")
            }
        }
    }

    override fun getItemCount() = posts.size

    fun updatePosts(newPosts: List<Post>) {
        Log.d("PostAdapter", "updatePosts called, newPosts size: ${newPosts.size}")
        posts.clear()
        posts.addAll(newPosts)
        notifyDataSetChanged()
        Log.d("PostAdapter", "notifyDataSetChanged called")
    }
}