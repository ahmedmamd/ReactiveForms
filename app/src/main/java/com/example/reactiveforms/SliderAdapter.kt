package com.example.reactiveforms

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter: SliderViewAdapter<SliderAdapter.SliderAdapterVh>() {
    var context: Context? = null
    var images = arrayOf(
        "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png",
        "https://besthqwallpapers.com/Uploads/20-11-2020/145812/thumb2-mount-fuji-japan-fujisan-autumn-red-trees.jpg",
        "https://fiddymentfarm.rcsdk8.org/sites/main/files/main-images/camera_lense_0.jpeg"
    )

    class SliderAdapterVh(itemView: View) : ViewHolder(itemView) {
        var imageslider: ImageView
        init {
            imageslider = itemView.findViewById(R.id.image_slider)
        }
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterVh {
        return SliderAdapterVh(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.item_sliderview,
                null
            )
        )
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVh?, position: Int) {
       Glide.with(viewHolder!!.itemView).load(images[position]).into(viewHolder.imageslider)
    }


}