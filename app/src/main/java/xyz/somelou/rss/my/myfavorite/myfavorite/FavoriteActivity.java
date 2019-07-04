package xyz.somelou.rss.my.myfavorite.myfavorite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import xyz.somelou.rss.R;
import xyz.somelou.rss.adapter.FavoriteListAdapter;
import xyz.somelou.rss.article.ArticleActivity;
import xyz.somelou.rss.bean.FavorRSSItem;
import xyz.somelou.rss.db.impl.FavorRSSItemDALImpl;

public class FavoriteActivity extends AppCompatActivity {

    private ListView lv;
    private ArrayList<FavorRSSItem> favorites;
    private FavoriteListAdapter adapter;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        context=getApplicationContext();
        lv=findViewById(R.id.Starlist);
        favorites=new FavorRSSItemDALImpl(context).getAllData(new ArrayList<FavorRSSItem>());
        adapter=new FavoriteListAdapter(context,favorites);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent goToArticle=new Intent(FavoriteActivity.this, ArticleActivity.class);
                goToArticle.putExtra("url",favorites.get(position).getItemUrl());
                goToArticle.putExtra("isFavorite",true);
                goToArticle.putExtra("position",position);
                goToArticle.putExtra("title",favorites.get(position).getTitleName());
                goToArticle.putExtra("discription",favorites.get(position).getDescription());
                startActivity(goToArticle);
                Log.i("--FavoriteActivity", "点击了第" + (position + 1) + "篇收藏");
            }
        });
    }
}
