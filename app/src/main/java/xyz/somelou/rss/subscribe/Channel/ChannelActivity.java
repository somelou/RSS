package xyz.somelou.rss.subscribe.Channel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xyz.somelou.rss.R;
import xyz.somelou.rss.adapter.RSSChannelAdapter;
import xyz.somelou.rss.bean.RSSItemBean;
import xyz.somelou.rss.bean.RSSUrl;
import xyz.somelou.rss.utils.RSSUtil;

public class ChannelActivity extends AppCompatActivity {
    private ArrayList<RSSItemBean> passages;//频道下文章
    private String RSSchannel;//频道名称
    private String url;//频道地址
    private RSSUtil util;
    private RSSChannelAdapter adapter;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_channel);
        //先接收intent
        setTitle(RSSchannel=getIntent().getStringExtra("title"));//设置activity标题
        this.setTitle(RSSchannel);
        passages=(ArrayList<RSSItemBean>) getIntent().getSerializableExtra("pages");
        //测试
        /*passages=new ArrayList<>();
        for (int i=0;i<passages.size();i++){
            Log.i("测试parcelable",passages.get(i).getTitle()+" "+passages.get(i).getDescription());
        }*/
        //定义变量
        adapter=new RSSChannelAdapter(this,passages);
        //绑定
        lv=this.findViewById(R.id.PassageList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Intent goToArticle=new Intent(this,文章界面);
                goToArticle.putParcelableArrayListExtra("pages",passages);
                startActivity(goToArticle);*/
                Log.i("--ChannelActivity","点击了第"+(position+1)+"篇文章");
            }
        });
        super.onCreate(savedInstanceState);
    }
}
