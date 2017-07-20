
        package com.example.sadanandk.moviereviews;

        import android.content.Context;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;


        class GridViewAdapter extends BaseAdapter
        {
            private final Context context;
            private final ArrayList<PojoImage> pj;
            GridViewAdapter(Context context, ArrayList<PojoImage> pj)
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
            public View getView(int position, View convertView, ViewGroup parent) {

                View v=convertView;
                ViewHolder holder;


                if(v==null) {

                    LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = li.inflate(R.layout.grid_item, parent, false);

                    holder=new ViewHolder();
                    holder.iv = (ImageView) v.findViewById(R.id.i1);
                    holder.tv = (TextView) v.findViewById(R.id.movie_tital);
                    Log.e("mahessh","ifnull");

                    v.setTag(holder);
                }
                else {

                    holder = (ViewHolder) v.getTag();
                    Log.e("mahessh","ifnotnull");

                }

                Picasso.with(context).load("http://image.tmdb.org/t/p/w500/" + pj.get(position).getUrl()).into(holder.iv);
                holder.tv.setText(pj.get(position).getId());

                return v;
            }
        }

        class ViewHolder
        {
            ImageView iv;
            TextView tv;


        }
