package app.porter.com.viewmanager;

/**
 * Created by S.Shivasurya on 8/21/2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.porter.com.R;
import app.porter.com.datastore.parcel;

public class MyRecyclerAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private List<parcel> feedItemList;
    private Context mContext;

    public MyRecyclerAdapter(Context context, ArrayList<parcel> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        parcel feedItem = feedItemList.get(i);

        Picasso.with(mContext).
                load(feedItem.getImage())
                .error(R.mipmap.ic_launcher)
                .noFade()
                .resize(100,100)
        .into(customViewHolder.imageView);

        customViewHolder.textView.setText(feedItem.getName());
        customViewHolder.priceView.setText("Rs." + "" + feedItem.getPrice());
        customViewHolder.typeView.setText(feedItem.getType());

    }
    public void updateList(List<parcel> data) {
        feedItemList = data;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }


}
