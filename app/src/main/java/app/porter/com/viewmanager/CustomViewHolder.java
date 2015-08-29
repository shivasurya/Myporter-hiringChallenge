package app.porter.com.viewmanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.porter.com.R;

/**
 * Created by S.Shivasurya on 8/21/2015.
 */
public class CustomViewHolder extends RecyclerView.ViewHolder {
    protected ImageView imageView;
    protected TextView textView;
    protected TextView priceView;
    protected TextView typeView;
    protected RelativeLayout relativeLayout;
    public CustomViewHolder(View view) {
        super(view);
        this.imageView = (ImageView) view.findViewById(R.id.image);
        this.textView = (TextView) view.findViewById(R.id.title);
        this.priceView = (TextView)view.findViewById(R.id.price);
        this.typeView = (TextView)view.findViewById(R.id.type);
        this.relativeLayout = (RelativeLayout)view.findViewById(R.id.layout);

    }
}