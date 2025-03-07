import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.adoptadog.R
import com.example.adoptadog.database.Post
import com.squareup.picasso.Picasso

class PostAdapter(
    private var posts: MutableList<Post>,
    private val onItemClick: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postImageView: ImageView = itemView.findViewById(R.id.postImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)

        // חישוב גודל הפריט
        val displayMetrics = parent.context.resources.displayMetrics
        val itemWidth = displayMetrics.widthPixels / 2 // 2 טורים
        val layoutParams = itemView.layoutParams
        layoutParams.width = itemWidth
        layoutParams.height = itemWidth
        itemView.layoutParams = layoutParams

        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentPost = posts[position]

        // טעינת תמונה עם Picasso
        Picasso.get()
            .load(currentPost.imageUrl)
            .fit() // התאמת התמונה למסגרת
            .centerCrop() // חיתוך התמונה למילוי המסגרת
            .into(holder.postImageView)

        holder.itemView.setOnClickListener {
            onItemClick(currentPost)
        }
    }

    override fun getItemCount() = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts.clear()
        posts.addAll(newPosts)
        notifyDataSetChanged()
    }
}