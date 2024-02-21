package com.RohitSharma.myapplication;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class News implements Comparable<News>, Serializable {
    private final String channel_id;
    private final String category;
    private final String channels;
    private final String author;
    private final String title;
    private final String description;
    private final String url;
    private final String urlToImage;
    private final String publishedAt;


    News(String category,String channel_id, String channels,String author,String title,String description, String url,String urlToImage,String publishedAt) {
        this.channel_id=channel_id;
        this.category = category;
        this.channels = channels;
        this.author=author;
        this.title=title;
        this.description=description;
        this.url=url;
        this.urlToImage=urlToImage;
        this.publishedAt=publishedAt;
    }
    String getChannel_id()
    {
        return channel_id;
    }
    String getCategory() {
        String capitalizeWord="";
        String first=category.substring(0,1);
        String afterfirst=category.substring(1);
        capitalizeWord+=first.toUpperCase()+afterfirst+" ";

        return capitalizeWord;
    }

    String getChannels() {
        return channels;
    }
    String getAuthor()
    {
        return author;
    }
    String getTitle()
    {
        return  title;
    }
    String getDescription()
    {
        return description;
    }
    String getUrl()
    {
        return url;
    }
    String getUrlToImage()
    {
        return urlToImage;
    }
    String getPublishedAt()
    {
        LocalDateTime temp=parseDate(publishedAt);
        String time=formatDateTime(temp);
        return(time);

    }


    @NonNull
    public String toString() {
        return channels;
    }

    @Override
    public int compareTo(News news) {
        return channels.compareTo(news.channels);
    }

    public static LocalDateTime parseDate(String date) {
        if (date != null) {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    // date/time
                    .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    // offset (hh:mm - "+00:00" when it's zero)
                    .optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd()
                    // offset (hhmm - "+0000" when it's zero)
                    .optionalStart().appendOffset("+HHMM", "+0000").optionalEnd()
                    // offset (hh - "Z" when it's zero)
                    .optionalStart().appendOffset("+HH", "Z").optionalEnd()
                    // create formatter
                    .toFormatter();

            return !date.equals("null") ? LocalDateTime.parse(date, formatter) : null;
        }

        return null;
    }


    public static String formatDateTime(LocalDateTime ldt) {
        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm", Locale.getDefault());
        return ldt.format(dtf);
    }
}
