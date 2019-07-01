package xyz.somelou.rss.find;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
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
import xyz.somelou.rss.adapter.RSSFindAdapter;
import xyz.somelou.rss.bean.RSSUrl;
import xyz.somelou.rss.db.impl.RSSUrlDALImpl;
import xyz.somelou.rss.enums.SubscribeStatus;
import xyz.somelou.rss.utils.SwitchGroupUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FindFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindFragment extends Fragment implements RSSFindAdapter.SubClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText key_word;//检索关键词
    EditText group_name;//输入分组名
    private ArrayList<RSSUrl> RSSfinds;
    private RSSFindAdapter RSSadapter;
    private RSSUrlDALImpl RSSdal;
    private ListView lv;
    private int MID;//数据库ID
    private SwitchGroupUtil switchGroupUtil;
    View contentView;

    public FindFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindFragment newInstance(String param1, String param2) {
        FindFragment fragment = new FindFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//设置有菜单
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView=inflater.inflate(R.layout.fragment_find, container, false);
        //界面初始化
        RSSdal=new RSSUrlDALImpl(getContext());//数据库操作类

        RSSfinds=RSSdal.getAllData(new ArrayList<RSSUrl>());//RSS源不为空
        /*for (int i=0;i<RSSfinds.size();i++)
        System.out.println(RSSfinds.get(i).getUrl()+" , "+RSSfinds.get(i).getName());*/

        RSSadapter=new RSSFindAdapter(getContext(),RSSfinds,this);
        lv=contentView.findViewById(R.id.findlist);
        lv.setAdapter(RSSadapter);
        registerForContextMenu(lv);
        switchGroupUtil=new SwitchGroupUtil(getContext());//工具类初始化
        return contentView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    //重写两个菜单有关的函数
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.find_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.find_menu_refresh:
                Toast.makeText(getActivity(),"--",Toast.LENGTH_SHORT).show();
                //清除ListView的过滤
                lv.clearTextFilter();
                //RSSadapter.getFilter().filter("");
                RSSfinds.clear();
                RSSfinds.addAll(RSSadapter.getOriginalData());
                RSSadapter.notifyDataSetChanged();
                break;
            case R.id.find_menu_search:
                input();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //输入检索关键字
    public void input(){
       key_word = new EditText(getContext());
       AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
       //设置标题，图标，视图
       dialog.setTitle("请输入检索关键词")
               .setIcon(android.R.drawable.sym_def_app_icon)
               .setView(key_word)
               .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       if (key_word.getText().toString() != null) {
                           RSSadapter.getFilter().filter(key_word.getText().toString());
                       }

                   }
               }).setNegativeButton("取消", null);
       //先显示对话框
       dialog.show();
        key_word.setFocusable(true);
        key_word.setFocusableInTouchMode(true);
        //请求获得焦点
        key_word.requestFocus();

    }

    //重写订阅按钮点击方法
    @Override
    public void subClick(View v) {
        //点击后订阅图标要变，是通过RSSUrl的订阅属性变带动适配器的数据源变
        if (RSSfinds.get((int)v.getTag()).getStatus()==SubscribeStatus.NO_SUBSCRIBE){
            RSSfinds.get((int)v.getTag()).setStatus(SubscribeStatus.SUBSCRIBED);
            Toast.makeText(getContext(),RSSfinds.get((int)v.getTag()).getName()+"  已订阅！", Toast.LENGTH_SHORT).show();
        }

        else {
            RSSfinds.get((int) v.getTag()).setStatus(SubscribeStatus.NO_SUBSCRIBE);
            Toast.makeText(getContext(), RSSfinds.get((int)v.getTag()).getName()+"  取消订阅！", Toast.LENGTH_SHORT).show();
        }
        //同步更新数据库和listview
        RSSdal.updateSubscribeStatus(RSSfinds.get((int)v.getTag()).getId(),RSSfinds.get((int)v.getTag()).getStatus());
        RSSadapter.notifyDataSetChanged();

    }

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
                        int id=am.position+1;
                        //获取item名字

                        TextView itemName = (TextView) item.findViewById(R.id.RSSTitle);

                        menu.setHeaderTitle(itemName.getText());
                        menu.add(0, 0, 0, "分组“"+RSSdal.getOneData(id).getGroupName()+"”");
                        menu.add(0, 1, 0, "更改分组");
                        menu.add(0, 2, 0, "添加至新分组");


                    }
                });
    }
    //选择菜单动作
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        MID = (int) info.position+1;// 这里的MId对应的就是数据库中_id的值

        switch (item.getItemId()) {
            case 0:
                // 显示分组
                Toast toast = Toast.makeText(getContext(),RSSdal.getOneData(MID).getGroupName(), Toast.LENGTH_LONG);
                toast.show();
                break;

            case 1:
                // 更改分组
                Toast.makeText(getContext(), "更改分组",
                    Toast.LENGTH_SHORT).show();
                showChoise();
                break;
            case 2:
                // 更改分组
                Toast.makeText(getContext(), "添加至新分组",
                        Toast.LENGTH_SHORT).show();
               createGroup();
                break;

            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    //选择分组动作函数
    private void showChoise()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),android.R.style.Theme_Holo_Light_Dialog);
        //builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("选择一个分组");
        //    指定下拉列表的显示数据
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
            }
        });
        builder.show();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*Log.i("点击发现项",RSSfinds.get(i).getName()+RSSfinds.get(i).getId()
                        +RSSfinds.get(i).getStatus()+RSSfinds.get(i).getGroupName()+RSSfinds.get(i).getUrl()+RSSfinds.get(i).getCount());*/
                Toast.makeText(getActivity(), "第" + (i+1) + "行", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //
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

                    }
                }).setNegativeButton("取消", null);
        //先显示对话框
        dialog.show();
        group_name.setFocusable(true);
        group_name.setFocusableInTouchMode(true);
        //请求获得焦点
        group_name.requestFocus();

    }

}
