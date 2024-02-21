package com.RohitSharma.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Locale;

public class NewsAdapter extends
        RecyclerView.Adapter<NewsHolder> {
    private final MainActivity mainActivity;
    private  final ArrayList<News> newsList;

    public NewsAdapter(MainActivity mainActivity, ArrayList<News> newsList) {
        this.mainActivity = mainActivity;
        this.newsList = newsList;
    }
    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.activity_news_channel, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
            News n= newsList.get(position);
            if(!n.getAuthor().equals("null"))
            { holder.author.setText(n.getAuthor());}
            else
            {
                holder.author.setVisibility(View.GONE);
            }
           holder.headline.setText(n.getTitle());
        holder.article.setMovementMethod(new ScrollingMovementMethod());
           holder.article.setText(n.getDescription());
           holder.date.setText(n.getPublishedAt());
           if(!n.getUrlToImage().equals("null"))
           {mainActivity.loadRemoteImage(n.getUrlToImage()).into(holder.image);}
        holder.count.setText(String.format(
                Locale.getDefault(),"%d of %d", (position+1), newsList.size()));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        Intent viewIntent =
                new Intent("android.intent.action.VIEW",
                        Uri.parse(n.getUrl()));
        mainActivity.startActivity(viewIntent);
            }
        });
        holder.headline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(n.getUrl()));
                mainActivity.startActivity(viewIntent);
            }
        });
        holder.article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(n.getUrl()));
                mainActivity.startActivity(viewIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


//    public void webNews(View view)
//    {
//        Intent viewIntent =
//                new Intent("android.intent.action.VIEW",
//                        Uri.parse(newsList.getUrl()));
//        mainActivity.startActivity(viewIntent);
//    }

}
