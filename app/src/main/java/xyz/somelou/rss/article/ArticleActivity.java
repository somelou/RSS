package xyz.somelou.rss.article;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import xyz.somelou.rss.R;
import xyz.somelou.rss.bean.FavorRSSItem;
import xyz.somelou.rss.bean.RSSItemBean;
import xyz.somelou.rss.db.impl.FavorRSSItemDALImpl;
import xyz.somelou.rss.utils.IntentUtil;
import xyz.somelou.rss.utils.RSSUtil;

public class ArticleActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RSSItemBean mItemBean;
    private FavorRSSItemDALImpl favorRSSItemDAL;
    protected static final float FLIP_DISTANCE = 50;
    SmsManager smsManager=SmsManager.getDefault();

    TextView textView_title;
    TextView textView_author;
    TextView textView_date;
    WebView textView_content;

    private int articlePosition;
    RSSUtil rssUtil;
    GestureDetector mDetector;
    ArrayList<RSSItemBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        setContentView(R.layout.activity_article);
        rssUtil = new RSSUtil(getIntent().getStringExtra("url"));
        articlePosition=getIntent().getIntExtra("position",0);
        list = (ArrayList<RSSItemBean>) rssUtil.getRssItemBeans();
        ArrayList<FavorRSSItem> dblist = new FavorRSSItemDALImpl(this).getAllData(new ArrayList<FavorRSSItem>());
        if (dblist.size() < 1)
            System.out.println("收藏数据库为空！");
        else {
            for (int i = 0; i < dblist.size(); i++)
                System.out.println("当前为收藏列表第" + (i + 1) + "个，url为" + dblist.get(i).getItemUrl() + "收藏时间为" + dblist.get(i).getFavorTime());
        }
        getSupportActionBar().hide();
        initViews();

    }






    /**
     public boolean onKeyDown(int keyCode, KeyEvent event) {
     if (keyCode == KeyEvent.KEYCODE_SEARCH) {
     String urlstr = list.get(0).getUri();
     textView_content.loadUrl(urlstr);
     return true;
     }
     return false;
     }
     **/

    private void initViews() {
        textView_title = findViewById(R.id.article_title);
        textView_date = findViewById(R.id.article_date);
        textView_content = findViewById(R.id.article_content);
        textView_author = findViewById(R.id.subscription_name);

        textView_author.setText(list.get(articlePosition).getAuthor().toString());
        //textView_content.setText(list.get(0).getDescription());
        textView_date.setText(list.get(articlePosition).getPubDate().toString());
        textView_title.setText(list.get(articlePosition).getTitle().toString());


        //支持javascript
        textView_content.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        textView_content.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        textView_content.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        textView_content.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        textView_content.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        textView_content.getSettings().setLoadWithOverviewMode(true);


        //如果不设置WebViewClient，请求会跳转系统浏览器
        textView_content.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242

                if (url.contains("sina.cn")){
                    view.loadUrl("http://ask.csdn.net/questions/178242");
                    return true;
                }

                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
            {
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (request.getUrl().toString().contains("sina.cn")){
                        view.loadUrl("http://ask.csdn.net/questions/178242");
                        return true;
                    }
                }

                return false;
            }

        });

        textView_content.loadUrl(list.get(articlePosition).getUri());
        //textView_content.loadDataWithBaseURL(null, list.get(0).getUri().toString(), "text/html", "utf-8", null);

        mToolbar =  findViewById(R.id.toolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleActivity.this.finish();//返回
            }
        });




        favorRSSItemDAL = new FavorRSSItemDALImpl(this);
        //设置action menu
        //填充menu
        mToolbar.inflateMenu(R.menu.menu_article);
        //设置点击事件
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_link:
                        Toast.makeText(ArticleActivity.this, "已跳转", Toast.LENGTH_SHORT).show();
                        IntentUtil.openUrl(ArticleActivity.this, list.get(articlePosition).getLink());
                        //System.out.println("测试:  url--" + list.get(0).getUri() + " title--" + list.get(0).getTitle() + "  discription--" + list.get(0).getDescription());
                        break;
                    case R.id.action_fav:
                        if (favorRSSItemDAL.isFavor(list.get(articlePosition).getUri()))
                            {
                                item.setIcon(R.drawable.ic_star_border_white_24dp);
                                favorRSSItemDAL.deleteOneData(list.get(articlePosition).getUri());
                                Toast.makeText(ArticleActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                            } else {
                            favorRSSItemDAL.insertOneData(list.get(articlePosition).getUri(), list.get(articlePosition).getTitle(), list.get(articlePosition).getDescription());
                            item.setIcon(R.drawable.ic_star_white_24dp);
                            Toast.makeText(ArticleActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.action_share:
                        //"smsto:xxx" xxx是可以指定联系人的
                        Uri smsToUri = Uri.parse("smsto:");

                        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

                        String smsBody="我看到一篇好文章，推荐你也来看"+list.get(articlePosition).getUri();
//"sms_body"必须一样，smsbody是发送短信内容content
                        intent.putExtra("sms_body", smsBody);

                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }



}



