import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.gson.R
import com.bumptech.glide.Glide

class PhotoAdapter(private var imageUrlList: List<String>, private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<PhotoAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewItem: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rview_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(holder.imageViewItem)
            .load(imageUrlList[position])
            .centerCrop()
            .into(holder.imageViewItem)
        holder.imageViewItem.setOnClickListener {
            onClick(imageUrlList[position])
        }
    }

    override fun getItemCount(): Int {
        return imageUrlList.size
    }

    fun updateList(urls: List<String>) {
        imageUrlList = urls
        notifyDataSetChanged()
    }
}