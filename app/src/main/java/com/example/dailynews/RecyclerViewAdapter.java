package com.example.dailynews;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private Context context;
    private ArrayList<NewsItem> news = new ArrayList<>();

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public RecyclerViewAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
          holder.title.setText(news.get(position).getTitle());
          holder.description.setText(news.get(position).getDesc());
          holder.date.setText(news.get(position).getDate());

        Glide.with(context)
                .asBitmap()
                .load(news.get(position).getImgUrl())
                .into(holder.image);

          holder.parent.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Log.d(TAG, "onClick: Clicked");
                  Intent intent = new Intent(context,WebViewActivity.class);
                  intent.putExtra("url",news.get(position).getLink());
                  context.startActivity(intent);
              }
          });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView parent;
        private ImageView image;
        private TextView title,description,date;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            image = itemView.findViewById(R.id.image);
            parent = itemView.findViewById(R.id.parent);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
        }
    }

    public void setNews(ArrayList<NewsItem> news) {
        this.news = news;
        notifyDataSetChanged();
    }
}
