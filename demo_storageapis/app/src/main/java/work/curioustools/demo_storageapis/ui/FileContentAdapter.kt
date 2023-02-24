package work.curioustools.demo_storageapis.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import work.curioustools.demo_storageapis.R

class FileContentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val files: MutableList<FileModel> = mutableListOf()

    var verticalFolder:Boolean =false
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_content,parent,false);
        return object : RecyclerView.ViewHolder(view){}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = files[position]
        with(holder.itemView){
            findViewById<AppCompatImageView>(R.id.ivFileIcon).setImageResource(data.res)
            findViewById<TextView>(R.id.tvName).text = "${position+1}. ${data.title}"

            val tvPath: TextView = if(verticalFolder) findViewById(R.id.tvPathHorizontal) else findViewById(R.id.tvPath)

            tvPath.visibility = View.VISIBLE
            tvPath.text = data.path
            val tvNotPath: TextView = if(!verticalFolder) findViewById(R.id.tvPathHorizontal) else findViewById(R.id.tvPath)
            tvNotPath.visibility = View.GONE


            val content =
                if(data.fileSize.isNotBlank()) "size: ${data.fileSize}"
                else "contains ${data.internalItemsSize} files and/or folders"

            findViewById<TextView>(R.id.tvSizeOrContents).text = content + "| type:"+data.mimeType
        }
    }

    override fun getItemCount()= files.size

}