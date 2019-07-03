package xyz.somelou.rss.my;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.somelou.rss.R;
import xyz.somelou.rss.db.DatabaseHelper;
import xyz.somelou.rss.my.myGroup.MyGroupRecyclerActivity;

public class MyFragment extends Fragment {
    private Context mContext;
    DatabaseHelper dateBaseHelper;
    int state;
    private String[]listnames=new String[]
            {"收藏列表","阅读字体","导入OPML"};
    private int[]imageID=new int[]
            {R.drawable.favorite, R.drawable.type,R.drawable.input};
    private String TextSize[]=new String[]{"小","中","大"};

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

        View my=inflater.inflate(R.layout.fragment_my,container,false);
        mContext=getContext();
        my.findViewById(R.id.layout_pinned_header_linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyGroupRecyclerActivity.startUp(mContext);
            }
        });
        List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<listnames.length;i++){
            Map<String,Object>listItem=new HashMap<String, Object>();
            listItem.put("header",imageID[i]);
            listItem.put("listname",listnames[i]);
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity(),listItems,R.layout.simple_my,new String[]{"header","listname"},new int[]{R.id.header,R.id.list});
        ListView listView=my.findViewById(R.id.myInterface);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    dateBaseHelper=new DatabaseHelper(getContext());
                    Cursor cursor=dateBaseHelper.getReadableDatabase().rawQuery("select * from FAVOR_RSS_ITEM",null);
                    Bundle data=new Bundle();
                    data.putSerializable("data",converCursorToList(cursor));
                    Intent intent=new Intent(getActivity(),FavoritesList.class);
                    intent.putExtras(data);
                    startActivity(intent);
                }
                else if(position==1){
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setTitle("字体大小").setSingleChoiceItems(TextSize,1,null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                case 1:
                                case 2:

                            }

                        }
                    });
                    builder.setNegativeButton("取消",null);
                    builder.show();

                }
                else if(position==2){

                }
            }
        });

        return my;
    }
    protected ArrayList<Map<String,String>>converCursorToList(Cursor cursor){
        ArrayList<Map<String,String>> result=new ArrayList<Map<String,String>>();
        while (cursor.moveToNext())
        {
            Map<String,String> map=new HashMap<>();
            map.put("url",cursor.getString(1));
            map.put("titleName",cursor.getString(2));
            map.put("description",cursor.getString(3));
            map.put("favor_time",cursor.getString(4));
            result.add(map);
        }
        return result;
    }
}