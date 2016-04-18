package com.example.gulshngill.flickrbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by gulshngill on 16/4/2016.
 */
public class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrImgViewHolder> {
    private String LOG_TAG = GetFlickrJsonData.class.getSimpleName();
    private List<Photo> photoList;
    private Context context;

    public FlickrRecyclerViewAdapter(Context context, List<Photo> photoList ) {
        this.photoList = photoList;
        this.context = context;
    }

    @Override
    public FlickrImgViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.browse, null);
        FlickrImgViewHolder flickrImgViewHolder = new FlickrImgViewHolder(view);

        return flickrImgViewHolder;
    }

    @Override
    public void onBindViewHolder(FlickrImgViewHolder holder, int position) {
        Photo photoItem = photoList.get(position);
        Log.d(LOG_TAG,"Processing: " + photoItem.getTitle() + " --> " + Integer.toString(position));

        Picasso.with(context).load(photoItem.getImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail);
        holder.title.setText(photoItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return (null != photoList ? photoList.size() : 0);
    }
}
