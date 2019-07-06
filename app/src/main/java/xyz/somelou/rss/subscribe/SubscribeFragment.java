package xyz.somelou.rss.subscribe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xyz.somelou.rss.R;
import xyz.somelou.rss.adapter.RSSSubscribeAdapter;
import xyz.somelou.rss.bean.RSSUrl;
import xyz.somelou.rss.db.impl.RSSUrlDALImpl;
import xyz.somelou.rss.enums.SubscribeStatus;
import xyz.somelou.rss.subscribe.channel.ChannelActivity;
import xyz.somelou.rss.utils.RSSUtil;
import xyz.somelou.rss.utils.SwitchGroupUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SubscribeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscribeFragment extends Fragment {

    private ArrayList<RSSUrl> subs;
    private RSSSubscribeAdapter adapter;
    private RSSUrlDALImpl RSSdal;
    private ListView lv;
    private View contentView;
    private  EditText key_word;
    private  EditText group_name;
    private RSSUtil util;
    private Boolean isPrepared = false;//标志是否初始化
    private int MID;//数据库ID
    private SwitchGroupUtil switchGroupUtil;

    public SubscribeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SubscribeFragment.
     */
    public static SubscribeFragment newInstance() {
        SubscribeFragment fragment = new SubscribeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        contentView=inflater.inflate(R.layout.fragment_subscribe, container, false);
        isPrepared = true;
        //初始化页面
        RSSdal=new RSSUrlDALImpl(getContext());
        subs=RSSdal.getSubscribe(new ArrayList<RSSUrl>());//获取已订阅的频道
        adapter=new RSSSubscribeAdapter(getActivity(),subs);
        lv=contentView.findViewById(R.id.SubList);//绑定布局
        lv.setAdapter(adapter);//设置适配器
        util=new RSSUtil();
        registerForContextMenu(lv);
        return contentView;

    }
    //重写两个菜单有关的函数
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.subscribe_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     key_word = new EditText(getActivity());
        switch(item.getItemId()){
            case R.id.subscribe_menu_add:
                key_word.setHint("http://www.reuters.com/tools/rss");
                AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                Log.i("对话框之前","--");
                //设置标题，图标，视图
                dialog.setTitle("添加RSS源")
                        .setIcon(android.R.drawable.ic_input_add)
                        .setView(key_word)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (key_word.getText().toString() != null) {
                                    String rss=key_word.getText().toString();
                                    //如果rss源解析成功
                                    util.setRssUrl(rss);
                                    if (util.getValidate()){
                                        //添加RSS到RSS表,默认订阅
                                        RSSdal.insertOneData(rss,null, SubscribeStatus.SUBSCRIBED);
                                        Log.i("-------订阅界面:","添加成功");
                                    }
                                    else{
                                        Toast.makeText(getContext(),"非法RSS源，无法添加！", Toast.LENGTH_SHORT).show();
                                        Log.i("-------订阅界面:","添加失败");
                                    }
                                }
                            }
                        }).setNegativeButton("取消", null);
                dialog.show();
                Log.i("对话框之后","--");
                break;
            case R.id.subscribe_menu_refresh:
                //手动刷新
                flushSubscribe();
                Toast.makeText(getActivity(),"刷新成功", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void flushSubscribe() {
        subs.clear();
        subs.addAll(RSSdal.getSubscribe(new ArrayList<RSSUrl>()));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUserVisibleHint(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent goToChannel=new Intent(getActivity(), ChannelActivity.class);
                //传频道标题和url
                goToChannel.putExtra("url",subs.get(i).getUrl());
                goToChannel.putExtra("title",subs.get(i).getName());
                startActivity(goToChannel);
                //Toast.makeText(getActivity(), "第" + (i+1) + "行", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //可见且已初始化
        if (isPrepared && isVisibleToUser) {
            flushSubscribe();
        }
        Log.i("setUserVisibleHint", "SubScribeFragment被加载");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //
    //长按回调菜单函数
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        lv .setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                // 返回包含了listView中被选中的Item的信息对象
                AdapterView.AdapterContextMenuInfo am = (AdapterView.AdapterContextMenuInfo) menuInfo;
                //获取点中的item
                View item = am.targetView;
                //获取item名字
                TextView itemName = (TextView) item.findViewById(R.id.SubTitle);
                switchGroupUtil=new SwitchGroupUtil(getContext());//工具类初始化
                switchGroupUtil.setIDbyName(itemName.getText().toString().trim());
                MID=switchGroupUtil.getID();
                menu.setHeaderTitle(itemName.getText());

                menu.add(0, 0, 0, "更改分组");
                menu.add(0, 4, 0, "添加至新分组");
                menu.add(0, 5, 0, "取消订阅");
                menu.add(0, 3, 0, "删除该项");

            }
        });
    }
    //选择菜单动作
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        switch (item.getItemId()) {
            case 0:
                // 更改分组
                Toast.makeText(getContext(), "更改分组", Toast.LENGTH_SHORT).show();
                showChoise();
                break;
            case 4:
                // 添加至新分组
                Toast.makeText(getContext(), "添加至新分组", Toast.LENGTH_SHORT).show();
                createGroup();
                break;
            case 5:
                // 取消订阅
                RSSdal.updateSubscribeStatus(MID,SubscribeStatus.NO_SUBSCRIBE);
                flushSubscribe();
                Toast.makeText(getContext(), "取消成功", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                // 删除该订阅
                RSSdal.deleteOneData(MID);
                flushSubscribe();
                Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    //选择分组动作函数
    private void showChoise() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),android.R.style.Theme_Holo_Light_Dialog);
        //builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("选择一个分组");
        //    指定下拉列表的显示数据
        switchGroupUtil=new SwitchGroupUtil(getContext());//工具类初始化
        List<String> groupNames= switchGroupUtil.getGroupNames();
        final String [] groups=groupNames.toArray(new String[groupNames.size()]);
        //    设置一个下拉的列表选择项
        builder.setItems(groups, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(getContext(), "选择的分组为：" + groups[which], Toast.LENGTH_SHORT).show();
                RSSdal.updateGroupName(MID,"" + groups[which]);
                flushSubscribe();
            }
        });
        builder.show();
    }

    //创建分组
    public void createGroup(){
        group_name = new EditText(getContext());
        AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
        //设置标题，图标，视图
        dialog.setTitle("请输入分组名称")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(group_name)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (group_name.getText().toString() != null) {
                            RSSdal.updateGroupName(MID,group_name.getText().toString().trim());
                        }
                        adapter.notifyDataSetChanged();
                        flushSubscribe();
                    }
                }).setNegativeButton("取消", null);
        //先显示对话框
        dialog.show();
        group_name.setFocusable(true);
        group_name.setFocusableInTouchMode(true);
        //请求获得焦点
        group_name.requestFocus();
        flushSubscribe();
    }

}
