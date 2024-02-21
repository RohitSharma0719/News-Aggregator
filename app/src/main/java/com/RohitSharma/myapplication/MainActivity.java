package com.RohitSharma.myapplication;

import static java.util.Comparator.naturalOrder;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static java.util.Comparator.comparing;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "";
    private Menu opt_menu;
    //private final String apiKey="4ec5a0ab9967479fa7a9409374b8a9ea";
    private final String apiKey="4fd79640632949dea94f3378d8637bdb";
    private static RequestQueue queue;
    private String newsUrl;
    private static final ArrayList<SpannableString> newsList = new ArrayList<>();
    private static final ArrayList<News> newslist = new ArrayList<>();
    private final HashMap<String, ArrayList<News>> newsData = new HashMap<>();
    private final HashMap<String,String> colordata= new HashMap<>();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayAdapter<News> arrayAdapter;
    private ViewPager2 viewPager;
    private newsDataLoader newsDataloader;
    private String channe_url="https://newsapi.org/v2/top-headlines?sources=";
    private final ArrayList<News> currentNewsList = new ArrayList<>();
    private NewsAdapter newsAdapter;
    private Picasso picasso;
    private static int temp=999;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);
        mDrawerLayout = findViewById(R.id.drawer_layout); // <== Important!
        mDrawerList = findViewById(R.id.left_drawer);
        picasso = Picasso.get();

        mDrawerList.setOnItemClickListener(
                (parent, view, position, id) -> {
//                    News news = newsList.get(position);
//                    Intent intent = new Intent(MainActivity.this, NewsChannel.class);
//                    intent.putExtra(News.class.getName(), news);
//                    startActivity(intent);
//                    mDrawerLayout.closeDrawer(mDrawerList);
                    temp=position;
                    selectNewsItem(position);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
        );

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        newsAdapter = new NewsAdapter(this, currentNewsList);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(newsAdapter);

        if (newsData.isEmpty()) {
            newsDataLoader clr = new newsDataLoader(this);
            new Thread(clr).start();
        }
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState (savedInstanceState);
//      savedInstanceState.getNewsArrayList("currentNewsList");
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState (outState);
        newsAdapter = new NewsAdapter(this, currentNewsList);
        newsAdapter.notifyDataSetChanged();
//        outState.putInt("position",position);
    }

//    protected void onSaveInstanceState(Bundle icicle) {
//        super.onSaveInstanceState(icicle);
////        icicle.putLong("param", value);
//    }


//    @Override
//    protected void onResume() {
//
//        super.onResume();
////       updateData(newsData);
//
//        if(temp!=999)
//        {
////            newsDataLoader clr = new newsDataLoader(this);
////            new Thread(clr).start();
//            selectNewsItem(temp);
//        }
////        if (newsData.isEmpty()) {
////            newsDataLoader clr = new newsDataLoader(this);
////            new Thread(clr).start();
////        }
//    }


    public void updateData(ArrayList<News> listIn) {
        setTitle("News Aggregator ("+listIn.size()+")");
        for (News news : listIn) {

            if (!newsData.containsKey(news.getCategory())) {
                newsData.put(news.getCategory(), new ArrayList<>());
            }
            ArrayList<News> clist = newsData.get(news.getCategory());
            if (clist != null) {
                clist.add(news);
            }
        }
        newsData.put("All", listIn);

        ArrayList<String> tempList = new ArrayList<>(newsData.keySet());
        Collections.sort(tempList);
//       SpannableString spanString = new SpannableString(newsSource.getName());
//            int red = Color.red(color);
//            int green = Color.green(color);
//            int blue = Color.blue(color);
//            spanString.setSpan(new ForegroundColorSpan(Color.rgb(red,green,blue)), 0,     spanString.length(), 0);
        String[] colorString= {"#000000","#FF12FF","#a1cef8","#e2d810","#FFBE33","#F11B65","#106ee2","#1BF123","#f97e42","#d4ac99","#8a7165","#f8bda1","#f8efa1","#a1b9f8","#cfa1f8"};
        int i=-0;
        for (String s : tempList)
        {
            SpannableString spanString = new SpannableString(s);
//            int color = Color.parseColor(ColorMaster.get(categories_ColorIndex.get(newsSource.getCategory())));
            int color = Color.parseColor(colorString[i]);
            colordata.put(s,colorString[i]);
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            spanString.setSpan(new ForegroundColorSpan(Color.rgb(red,green,blue)), 0,spanString.length(), 0);
            opt_menu.add(spanString);
            i++;
        }
//            opt_menu.add(s);
//            opt_menu.add(s.substring(0,1).toUpperCase()+s.substring(1));
//            opt_menu.add(spanString);}

//        ArrayList<String> temp= new ArrayList<String>();
//        for (News news:listIn){
//            temp.add(news.getChannels());
//        }
//        Collections.sort(temp);
//        newsList.addAll(listIn);

        newslist.addAll(listIn);
        ArrayList<SpannableString> temp1= new ArrayList<>();
        for(News s : listIn)
        {
            SpannableString spanString = new SpannableString(s.getChannels());
            int color = Color.parseColor(colordata.get(s.getCategory()));
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            spanString.setSpan(new ForegroundColorSpan(Color.rgb(red,green,blue)), 0,spanString.length(), 0);
           newsList.add(spanString);
        }
        arrayAdapter = new ArrayAdapter(this, R.layout.drawer_items, newsList);
        mDrawerList.setAdapter(arrayAdapter);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    public void downloadFailed() {
        newsList.clear();
        arrayAdapter.notifyDataSetChanged();
    }

    // You need the 2 below to make the drawer-toggle work properly:

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }



    // You need the below to open the drawer when the toggle is clicked
    // Same method is called when an options menu item is selected.

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }
//        setTitle(item.getTitle());
        newsList.clear();
        newslist.clear();
        ArrayList<News> clist = newsData.get(item.getTitle().toString());
        if (clist != null) {
            newslist.addAll(clist);
            for(News s : clist)
            {
                SpannableString spanString = new SpannableString(s.getChannels());
                int color = Color.parseColor(colordata.get(s.getCategory()));
                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);
                spanString.setSpan(new ForegroundColorSpan(Color.rgb(red,green,blue)), 0,spanString.length(), 0);
                newsList.add(spanString);
            }

        }
        if(getTitle().toString().contains("News Aggregator"))
        {
        setTitle("News Aggregator ("+newsList.size()+")");}
        arrayAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    // You need this to set up the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        opt_menu = menu;
        return true;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void selectNewsItem(int position) {
        viewPager.setBackground(null);
//         News channel = newsList.get(position);
        SpannableString p=newsList.get(position);
        News channel = newslist.get(position);
        setTitle(channel.getChannels());
        String DATA_URL=channe_url+channel.getChannel_id();
        queue = Volley.newRequestQueue(this);
        Uri.Builder buildURL = Uri.parse(DATA_URL).buildUpon();
        buildURL.appendQueryParameter("apiKey",apiKey);
        String urlToUse = buildURL.build().toString();
        Response.Listener<JSONObject> listener =
                response ->{
            try {
                String author="null";

                currentNewsList.clear();
                JSONObject jObjMain = new JSONObject(response.toString());
                JSONArray jsonSources = jObjMain.getJSONArray("articles");
                for (int i = 0; i < jsonSources.length(); i++) {
                    author="null";
                    if(jsonSources.getJSONObject(i).has("author"))
                    {
                        author=jsonSources.getJSONObject(i).getString("author");
                    }
                    currentNewsList.add(new News("","", "", author, jsonSources.getJSONObject(i).getString("title"), jsonSources.getJSONObject(i).getString("description")
                            , jsonSources.getJSONObject(i).getString("url"), jsonSources.getJSONObject(i).getString("urlToImage"), jsonSources.getJSONObject(i).getString("publishedAt")));
                }
                newsAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(0);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
                };
        Response.ErrorListener error =
                error1 -> {
                    Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
                };

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "");
                        return headers;
                    }
                };
        queue.add(jsonObjectRequest);
    }


    public RequestCreator loadRemoteImage(String imageURL) {
        // Needs gradle  implementation 'com.squareup.picasso:picasso:2.71828'
        if(imageURL.equals("null"))
            return null;
        return picasso.load(imageURL)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.loading);

    }
    }

