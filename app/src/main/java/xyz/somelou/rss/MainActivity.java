package xyz.somelou.rss;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.List;

import xyz.somelou.rss.adapter.ViewPagerAdapter;
import xyz.somelou.rss.bean.RSSItemBean;
import xyz.somelou.rss.find.FindFragment;
import xyz.somelou.rss.my.MyFragment;
import xyz.somelou.rss.subscribe.SubscribeFragment;
import xyz.somelou.rss.utils.RSSUtil;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RSSUtil rssUtil=new RSSUtil("http://www.4sbooks.com/feed");
        List<RSSItemBean> rssItemBeans = rssUtil.getRssItemBeans();
        if (rssItemBeans != null && !rssItemBeans.isEmpty()){
            for (RSSItemBean item : rssItemBeans) {
                System.out.println(item.getTitle());
                System.out.println(item.getLink());
                System.out.println(item.getPubDate());
                System.out.println(item.getDescription());
            }
        }

        viewPager=findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //新版本的sdk好像已经解决了这个问题
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_list:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.item_add:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.item_find:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
//                FragmentManager fm = getSupportFragmentManager();
//
//                // fm.getFragments(),当点了第三页时，再回来，返回值的内容就不同了，ListStuFragment和StudentFragment的顺序发生了改变
//                List<Fragment> fragments = fm.getFragments();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setupViewPager(viewPager);
    }

    private void testRssUtil(){
        RSSUtil rssUtil=new RSSUtil("https://rsshub.app/zhihu/hotlist");
        List<RSSItemBean> rssItemBeans = rssUtil.getRssItemBeans();
        if (rssItemBeans != null && !rssItemBeans.isEmpty()){
            for (RSSItemBean item : rssItemBeans) {
                System.out.println(item.getTitle());
                System.out.println(item.getLink());
                System.out.println(item.getPubDate());
                System.out.println(item.getDescription());
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(SubscribeFragment.newInstance());
        adapter.addFragment(FindFragment.newInstance("1","2"));
        adapter.addFragment(MyFragment.newInstance("1","2"));
        viewPager.setAdapter(adapter);
    }
}
