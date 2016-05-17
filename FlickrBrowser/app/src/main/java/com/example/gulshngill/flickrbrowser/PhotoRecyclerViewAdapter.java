package com.example.gulshngill.flickrbrowser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by gulshngill on 08/05/2016.
 */
public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter {
    //data of photoList is passed from GetJsonData through the main class
    private List<Photo> photoList;
    private Context context;


    public PhotoRecyclerViewAdapter(List<Photo> photoList, Context context) {
        this.photoList = photoList;
        this.context = context;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        protected ImageView thumbnail;
        protected TextView title;

        public PhotoViewHolder(View v) {
            super(v);
            this.thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
            this.title = (TextView) v.findViewById(R.id.title);
        }

    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //initialize viewholder (create view from browse.xml file by inflating it into a view object)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, null);

        //create new photoViewHolder object using the inflated browse.xml
        PhotoViewHolder photoViewHolder = new PhotoViewHolder(view);
        return photoViewHolder;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        //get viewholder
        PhotoViewHolder photoViewHolder = (PhotoViewHolder) viewHolder;

        //Populate each view with data (position = position of viewholder)
        Photo photoItem = photoList.get(position);

        Picasso.with(context).load(photoItem.getPhotoUrl())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(photoViewHolder.thumbnail);

        photoViewHolder.title.setText(photoItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return (null != photoList ? photoList.size() : 0);
    }

    public void loadNewData(List<Photo> photoList) {
        this.photoList = photoList;
        notifyDataSetChanged();
    }
}
