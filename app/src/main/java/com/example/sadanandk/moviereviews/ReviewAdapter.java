package com.example.sadanandk.moviereviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by maheshs on 6/28/2017.
 */

class ReviewAdapter extends BaseAdapter
{
    private final Context context;

    private final ArrayList<PojoReview> pj;
    ReviewAdapter(Context context, ArrayList<PojoReview> pj)
    {
        this.context = context;
        this.pj = pj;
    }
    @Override
    public int getCount() {
        return pj.size();
    }

    @Override
    public Object getItem(int position) {
        return pj.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v=convertView;
        ViewHolder1 holder;


        if(v==null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate( R.layout.reviewitem, parent, false);


            //  Toast.makeText(context, ""+pj.size(), Toast.LENGTH_SHORT).show();
            // http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg

            holder=new ViewHolder1();
            holder.review = (TextView) v.findViewById(R.id.t1);
             holder.author = (TextView) v.findViewById(R.id.t2);
            v.setTag(holder);
        }
        else
        {
          holder= (ViewHolder1) v.getTag();
        }

        holder.review.setText(pj.get(position).getReview());
        holder.author.setText(pj.get(position).getAuthor());

        return v;
    }
}

class ViewHolder1
{

    TextView review;
    TextView author;
}
