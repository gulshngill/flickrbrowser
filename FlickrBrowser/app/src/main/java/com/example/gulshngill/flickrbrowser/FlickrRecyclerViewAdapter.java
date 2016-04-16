package com.example.gulshngill.flickrbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by gulshngill on 16/4/2016.
 */
public class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrImgViewHolder> {
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
    public int getItemCount() {
        return (null != photoList ? photoList.size() : 0);
    }
}
