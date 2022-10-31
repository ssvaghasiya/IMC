package com.example.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.LayoutDataBinding
import com.example.myapplication.pojo.MusicData
import com.example.myapplication.ui.listener.EventListener

class CustomAdapter(
    var data: ArrayList<MusicData.MusicResult>,
    var mEventListener: EventListener<MusicData.MusicResult>
) :
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = DataBindingUtil.inflate<LayoutDataBinding>(
            inflater,
            R.layout.layout_data, parent, false
        )
        return MyViewHolder(itemBinding)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    private fun getItem(p: Int): MusicData.MusicResult {
        return data[p]

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = with(holder.itemBinding) {
        val item = getItem(position)
        item.apply {
            textViewAlbumName.text = name
            textViewUserName.text = artistName
        }

        root.setOnClickListener {
            mEventListener.onItemClick(position, item, it)
        }
    }

    inner class MyViewHolder(internal var itemBinding: LayoutDataBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

}