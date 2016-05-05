package com.nlaz.infinitekitty;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ListView listview;
    private KittenAdapter adapter;
    private ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.listview);
        data = new ArrayList<String>();
        for (int i=0; i < 10;++i)
             data.add("Kitten " + i);

        adapter = new KittenAdapter(this, R.layout.kitten_item, data);
        listview.setAdapter(adapter);

        listview.setOnScrollListener(new InfiniteScroller(5) {
            @Override
            public void loadMore(int page, int totalItemsCount) {
                for (int i = 0; i < 5;++i){
                    data.add("Kitten "+ i);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public abstract class InfiniteScroller implements AbsListView.OnScrollListener{
        private int bufferItemCount = 10;
        private int currentPage = 0;
        private int itemCount = 0;
        private boolean isLoading = true;

        public InfiniteScroller(int bufferCount){
            this.bufferItemCount = bufferCount;
        }

        public abstract void loadMore(int page, int totalItemsCount);

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount < itemCount) {
                this.itemCount = totalItemCount;
                if (totalItemCount == 0) {
                    this.isLoading = true; }
            }

            if (isLoading && (totalItemCount > itemCount)) {
                isLoading = false;
                itemCount = totalItemCount;
                currentPage++;
            }

            if (!isLoading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + bufferItemCount)) {
                loadMore(currentPage + 1, totalItemCount);
                isLoading = true;
            }
        }
    }

    public class KittenAdapter extends ArrayAdapter<String>{

        private LayoutInflater mInflater;
        private Context context;

        public KittenAdapter(Context context, int resId, ArrayList<String> objects){
            super(context,resId, objects);
            this.mInflater = LayoutInflater.from(context);
            this.context = context;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder viewHolder;

            if (view == null){
                view = mInflater.inflate(R.layout.kitten_item, null);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) view.getTag();
            }

            Picasso.with(context).load("http://placekitten.com/g/500/200").into(viewHolder.imageView);

            return view;
        }

        public class ViewHolder{

            public ImageView imageView;

            public ViewHolder(View view){
                imageView = (ImageView) view.findViewById(R.id.kitten_image);
            }
        }

    }
}