package xyz.somelou.rss.my.myGroup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xyz.somelou.rss.R;
import xyz.somelou.rss.bean.FavorRSSItem;
import xyz.somelou.rss.bean.RSSUrl;
import xyz.somelou.rss.db.DatabaseHelper;
import xyz.somelou.rss.db.RSSUrlDAL;
import xyz.somelou.rss.db.impl.FavorRSSItemDALImpl;
import xyz.somelou.rss.db.impl.RSSUrlDALImpl;
import xyz.somelou.rss.my.MyFragment;
import xyz.somelou.rss.my.pinnerHeader.PinnedHeaderItemDecoration;
import xyz.somelou.rss.my.pinnerHeader.PinnedHeaderRecyclerView;
import xyz.somelou.rss.my.touchHelper.MyItemTouchHelperCallback;
import xyz.somelou.rss.utils.SwitchGroupUtil;

import static android.widget.Toast.LENGTH_SHORT;

public class MyGroupRecyclerActivity extends AppCompatActivity {
    private RSSUrlDALImpl rssUrlDALImpl;
    private SwitchGroupUtil switchGroupUtil;

    public static void startUp(Context context) {
        context.startActivity(new Intent(context, MyGroupRecyclerActivity.class));
    }

    private PinnedHeaderRecyclerView mRecyclerView;
    private Context                  mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_group_activity_recycler);
        mContext = this;
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recycler_linear);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void initEvent() {
        mRecyclerView.setOnPinnedHeaderClickListener(new PinnedHeaderRecyclerView.OnPinnedHeaderClickListener() {
            @Override
            public void onPinnedHeaderClick(int adapterPosition) {
                Toast.makeText(mContext, "点击了悬浮标题 position = " + adapterPosition, LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        MyGroupRecyclerAdapter adapter = new MyGroupRecyclerAdapter(obtainData());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new PinnedHeaderItemDecoration());
        //////
        MyItemTouchHelperCallback callback = new MyItemTouchHelperCallback(adapter); //此类继承ItemTouchHelper.Callback,这是帮助处理RecylerView拖动侧滑操作的辅助类
        callback.setDatas(obtainData());
        ItemTouchHelper helper = new ItemTouchHelper(callback); //用上面实例化的callback实例化一个ItemTouchHelper对象。
        helper.attachToRecyclerView(mRecyclerView);
    }

    private List<String> obtainData() {
        switchGroupUtil=new SwitchGroupUtil(this);
        return switchGroupUtil.getGroupSortList();

    }

}
