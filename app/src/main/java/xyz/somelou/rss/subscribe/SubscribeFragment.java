package xyz.somelou.rss.subscribe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import xyz.somelou.rss.R;
import xyz.somelou.rss.adapter.RSSSubscribeAdapter;
import xyz.somelou.rss.bean.RSSItemBean;
import xyz.somelou.rss.bean.RSSUrl;
import xyz.somelou.rss.db.impl.RSSUrlDALImpl;
import xyz.somelou.rss.enums.SubscribeStatus;
import xyz.somelou.rss.subscribe.channel.ChannelActivity;
import xyz.somelou.rss.utils.RSSUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SubscribeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscribeFragment extends Fragment {

    private ArrayList<RSSUrl> subs;
    private ArrayList<RSSItemBean> pages;
    private RSSSubscribeAdapter adapter;
    private RSSUrlDALImpl RSSdal;
    private ListView lv;
    private View contentView;
    private  EditText key_word;
    private RSSUtil util;

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
        //初始化页面
        RSSdal=new RSSUrlDALImpl(getContext());
        subs=RSSdal.getSubscribe(new ArrayList<RSSUrl>());//获取已订阅的频道
        /*if (subs.size()>0) {
            for (int i = 0; i < subs.size(); i++) {
                Log.i("测试组别", subs.get(i).getName()+"组："+subs.get(i).getGroupName());
            }
        }
        else
            Log.i("测试已订阅列表","空");*/
        adapter=new RSSSubscribeAdapter(getActivity(),subs);
        lv=contentView.findViewById(R.id.SubList);//绑定布局
        lv.setAdapter(adapter);//设置适配器
        util=new RSSUtil();
        //Log.i("默认validate",util.getValidate().toString());
        pages=new ArrayList<>();
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
                                        //添加RSS到RSS表,默认不订阅
                                        RSSdal.insertOneData(rss,null, SubscribeStatus.NO_SUBSCRIBE);
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
                /*key_word.setFocusable(true);
                key_word.setFocusableInTouchMode(true);
                //请求获得焦点
                key_word.requestFocus();*/
                Log.i("对话框之后","--");
                break;
            case R.id.subscribe_menu_refresh:
                //手动刷新
                subs.clear();
                subs.addAll(RSSdal.getSubscribe(new ArrayList<RSSUrl>()));
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(),"刷新成功", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent goToChannel=new Intent(getActivity(), ChannelActivity.class);
                //传频道标题和所有文章
                util.setRssUrl(subs.get(i).getUrl());//先设置网址进行解析
                pages= (ArrayList)util.getRssItemBeans();//再保存文章
                goToChannel.putExtra("pages",pages);
                goToChannel.putExtra("url",subs.get(i).getUrl());
                goToChannel.putExtra("title",subs.get(i).getName());
                startActivity(goToChannel);
                //Toast.makeText(getActivity(), "第" + (i+1) + "行", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
