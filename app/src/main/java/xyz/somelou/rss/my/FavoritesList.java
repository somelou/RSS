package xyz.somelou.rss.my;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.*;

import xyz.somelou.rss.R;

public class FavoritesList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favoriteslist_layout);
        final ListView listView=(ListView)findViewById(R.id.myfavoriteslist);
        final Intent intent=getIntent();
        Bundle data=intent.getExtras();
        List<Map<String,String>>list=(List<Map<String,String>>)data.getSerializable("data");
        SimpleAdapter adapter=new SimpleAdapter(FavoritesList.this,list,R.layout.simple_favoriteslist,new String[]{"url","titleName","description","favor_time"},new int[]{R.id.Url,R.id.TitleName,R.id.Description,R.id.favorTime});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String URL;
                HashMap<String,String> map=(HashMap<String,String>)listView.getItemAtPosition(position);
                URL=map.get("url");
                Intent intent1=new Intent();
                intent.setData(Uri.parse(URL));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
            }
        });
    }
}
