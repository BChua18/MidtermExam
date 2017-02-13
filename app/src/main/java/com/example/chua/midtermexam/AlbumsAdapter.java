package com.example.chua.midtermexam;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Chua on 2/13/2017.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Album> albums;

    public AlbumsAdapter(Context mContext, ArrayList<Album> albums){
        this.mContext = mContext;
        this.albums = albums;
    }

    @Override
    public AlbumsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AlbumsAdapter.ViewHolder holder, int position) {
        if(!albums.get(position).getImage().isEmpty()){
            Glide.with(mContext).load(albums.get(position).getImage()).into(holder.album_image);
        }
        holder.album_title.setText(albums.get(position).getName());
        holder.album_artist.setText(albums.get(position).getArtist());
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView album_image;
        private TextView album_title;
        private TextView album_artist;

        public ViewHolder(final View itemView){
            super(itemView);
            album_image = (ImageView) itemView.findViewById(R.id.album_image);
            album_title = (TextView) itemView.findViewById(R.id.album_title);
            album_artist = (TextView) itemView.findViewById(R.id.album_artist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri albumUri = Uri.parse(albums.get(getAdapterPosition()).getUrl());
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, albumUri);
                    mContext.startActivity(websiteIntent);
                }
            });

        }
    }
}
