package com.ps.mohamed.travelmantics.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ps.mohamed.travelmantics.Model.TravelDeal;
import com.ps.mohamed.travelmantics.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TravelDeal> mDeals;
    public TravelAdapter(Context ctx, ArrayList<TravelDeal> deals) {
        context = ctx;
        mDeals = deals;

    }

    @Override
    public TravelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(TravelAdapter.ViewHolder holder, int position) {
        TravelDeal currentDeal = mDeals.get(position);
        String imageUrl = null;
        holder.title.setText(currentDeal.getTitle());
        holder.desc.setText(currentDeal.getDesc());
         holder.value.setText(currentDeal.getValue());
        imageUrl = currentDeal.getImage();
        //we need the picasso lib to get the Image
        Picasso.with(context)
                .load(imageUrl)
                .into(holder.postImage)
        ;


    }

    @Override
    public int getItemCount() {
        return mDeals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,desc,value;
        public ImageView postImage;
        String userid;
        public ViewHolder(View itemView,Context ctx) {
            super(itemView);
            context = ctx;
            title = (TextView) itemView.findViewById(R.id.ListItemHNameTvID);
            desc = (TextView) itemView.findViewById(R.id.ListItemHDiscriptionTvID);
            value  = (TextView) itemView.findViewById(R.id.ListItemHValueTvID);
            postImage = (ImageView) itemView.findViewById(R.id.ListItemIvID);
            userid = null;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //we can go to the next activity
                }
            });







        }
    }
}
