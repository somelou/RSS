package xyz.somelou.rss.article;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import xyz.somelou.rss.R;
import xyz.somelou.rss.bean.FavorRSSItem;
import xyz.somelou.rss.bean.RSSItemBean;
import xyz.somelou.rss.db.impl.FavorRSSItemDALImpl;
import xyz.somelou.rss.utils.IntentUtil;
import xyz.somelou.rss.utils.RSSUtil;

public class ArticleActivity extends AppCompatActivity implements View.OnClickListener {
    protected static final float FLIP_DISTANCE = 50;
    SmsManager smsManager=SmsManager.getDefault();
    TextView textView_title;
    TextView textView_author;
    TextView textView_date;
    WebView textView_content;
    RSSUtil rssUtil;
    ArrayList<RSSItemBean> list;
    private Toolbar mToolbar;
    private RSSItemBean mItemBean;
    private FavorRSSItemDALImpl favorRSSItemDAL;
    private ImageButton mPreviousBtn;
    private ImageButton mNextBtn;
    private int articlePosition=0;
    private Boolean isFavorite=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        setContentView(R.layout.activity_article);
        rssUtil = new RSSUtil();
        isFavorite=getIntent().getBooleanExtra("isFavorite",false);
        if (!isFavorite){
            articlePosition=getIntent().getIntExtra("position",0);
            rssUtil.setRssUrl(getIntent().getStringExtra("url"));
            list = (ArrayList<RSSItemBean>) rssUtil.getRssItemBeans();
            ArrayList<FavorRSSItem> dblist = new FavorRSSItemDALImpl(this).getAllData(new ArrayList<FavorRSSItem>());
            if (dblist.size() < 1)
                System.out.println("收藏数据库为空！");
            else {
                for (int i = 0; i < dblist.size(); i++)
                    System.out.println("当前为收藏列表第" + (i + 1) + "个，url为" + dblist.get(i).getItemUrl() + "收藏时间为" + dblist.get(i).getFavorTime());
            }
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
        favorRSSItemDAL = new FavorRSSItemDALImpl(this);
        //绑定控件
        textView_title = findViewById(R.id.article_title);
        textView_date = findViewById(R.id.article_date);
        textView_content = findViewById(R.id.article_content);
        textView_author = findViewById(R.id.subscription_name);
        mPreviousBtn = findViewById(R.id.previous_btn);
        mNextBtn = findViewById(R.id.next_btn);
        //设置action menu
        //填充menu
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_article);

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

        /*如果从收藏页面跳转进来，则从intent获取数据；否则从dblist获取数据*/
        if (!isFavorite) {
            mPreviousBtn.setOnClickListener(this);
            mNextBtn.setOnClickListener(this);
            textView_author.setText(list.get(articlePosition).getAuthor().toString());
            //textView_content.setText(list.get(0).getDescription());
            textView_date.setText(list.get(articlePosition).getPubDate().toString());
            textView_title.setText(list.get(articlePosition).getTitle().toString());
            //如果从文章列表进入，则判断是否在收藏数据表中
            if (favorRSSItemDAL.isFavor(list.get(articlePosition).getUri())) {
                mToolbar.getMenu().findItem(R.id.action_fav).setIcon(R.drawable.ic_star_white_24dp);
            }
            textView_content.loadUrl(list.get(articlePosition).getUri());
        } else {
            textView_author.setText("");
            textView_date.setText(getIntent().getStringExtra("addTime"));
            textView_title.setText(getIntent().getStringExtra("title"));
            textView_content.loadUrl(getIntent().getStringExtra("url"));
            mToolbar.getMenu().findItem(R.id.action_fav).setIcon(R.drawable.ic_star_white_24dp);
            //favor.setIcon(R.drawable.ic_star_white_24dp);
        }

        //设置点击事件
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_link:
                        Toast.makeText(ArticleActivity.this, "已跳转", Toast.LENGTH_SHORT).show();
                        if (!isFavorite)
                            IntentUtil.openUrl(ArticleActivity.this, list.get(articlePosition).getLink());
                        else
                            IntentUtil.openUrl(ArticleActivity.this, getIntent().getStringExtra("url"));
                        //System.out.println("测试:  url--" + list.get(0).getUri() + " title--" + list.get(0).getTitle() + "  discription--" + list.get(0).getDescription());
                        break;
                    case R.id.action_fav:
                        if (!isFavorite) {
                            if (favorRSSItemDAL.isFavor(list.get(articlePosition).getUri())) {
                                item.setIcon(R.drawable.ic_star_border_white_24dp);
                                favorRSSItemDAL.deleteOneData(list.get(articlePosition).getUri());
                                Toast.makeText(ArticleActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                            } else {
                                favorRSSItemDAL.insertOneData(list.get(articlePosition).getUri(), list.get(articlePosition).getTitle(), list.get(articlePosition).getDescription());
                                item.setIcon(R.drawable.ic_star_white_24dp);
                                Toast.makeText(ArticleActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            item.setIcon(R.drawable.ic_star_border_white_24dp);
                            favorRSSItemDAL.deleteOneData(getIntent().getStringExtra("url"));
                            Toast.makeText(ArticleActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.action_share:
                        //"smsto:xxx" xxx是可以指定联系人的
                        Uri smsToUri = Uri.parse("smsto:");
                        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                        String smsBody = "我看到一篇好文章，推荐你也来看";
                        if (!isFavorite)
                            smsBody += list.get(articlePosition).getUri();
                        else
                            smsBody += getIntent().getStringExtra("url");
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


    @Override
    public void onClick(View v) {
        if (!isFavorite) {
            if (v == mPreviousBtn) {
                if (articlePosition < 1) {
                    Toast.makeText(ArticleActivity.this, "已经是第一篇", Toast.LENGTH_SHORT).show();
                } else {
                    articlePosition--;
                    textView_author.setText(list.get(articlePosition).getAuthor().toString());
                    textView_date.setText(list.get(articlePosition).getPubDate().toString());
                    textView_title.setText(list.get(articlePosition).getTitle().toString());
                    textView_content.loadUrl(list.get(articlePosition).getUri());
                }

            } else if (v == mNextBtn) {
                if (articlePosition >= list.size() - 1) {
                    Toast.makeText(ArticleActivity.this, "已经是最后一篇", Toast.LENGTH_SHORT).show();
                } else {
                    articlePosition++;
                    textView_author.setText(list.get(articlePosition).getAuthor().toString());
                    textView_date.setText(list.get(articlePosition).getPubDate().toString());
                    textView_title.setText(list.get(articlePosition).getTitle().toString());
                    textView_content.loadUrl(list.get(articlePosition).getUri());
                }
            }
        }
        //从收藏列表进入后隐藏上下切换按钮
        else {
            mPreviousBtn.setVisibility(View.INVISIBLE);
            mNextBtn.setVisibility(View.INVISIBLE);
        }
    }
}



