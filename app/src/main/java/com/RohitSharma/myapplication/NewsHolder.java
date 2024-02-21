package com.RohitSharma.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsHolder extends RecyclerView.ViewHolder {
    TextView headline;
    TextView date;
    TextView author;
    TextView article;
    ImageView image;
    TextView count;
    public NewsHolder(@NonNull View itemView) {
        super(itemView);
        headline=itemView.findViewById(R.id.news_headline);
        date=itemView.findViewById(R.id.article_date);
        author=itemView.findViewById(R.id.article_author);
        article=itemView.findViewById(R.id.article);
        count=itemView.findViewById(R.id.article_count);
        image=itemView.findViewById(R.id.imageView);
    }
}
