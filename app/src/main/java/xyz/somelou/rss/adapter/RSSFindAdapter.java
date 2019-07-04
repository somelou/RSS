package xyz.somelou.rss.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import xyz.somelou.rss.R;
import xyz.somelou.rss.bean.RSSUrl;
import xyz.somelou.rss.db.impl.RSSUrlDALImpl;
import xyz.somelou.rss.enums.SubscribeStatus;

/**
 * Created by ${Marrrrrco} on 2019/6/26.
 */

public class RSSFindAdapter extends BaseAdapter implements Filterable,View.OnClickListener{
    public Context context;
    private ArrayList<RSSUrl> finds;
    private ArrayList<RSSUrl> bkfinds;//备用列表
    private RSSFilter mfilter;
    private SubClickListener clickListener;//为每个item的按钮设置自定义监听器

    //定义一个订阅按钮的接口，用于回调点击按钮的方法
    public interface  SubClickListener{
        public void subClick(View v);
    }

    RSSFindAdapter(){

    }

    public RSSFindAdapter(Context c,ArrayList<RSSUrl> list){
        context=c;
        finds=list;
        bkfinds=list;
        /*for (int i=0;i<finds.size();i++)
        System.out.println("过滤前:"+finds.get(i).getUrl()+" , "+finds.get(i).getName());*/
    }

    public RSSFindAdapter(Context c,ArrayList<RSSUrl> list,SubClickListener cl){
        context=c;
        finds=list;
        bkfinds=new ArrayList<>();
        bkfinds.addAll(list);
        clickListener=cl;
    }

    public int getCount() {
        return finds.size();
    }
    //刷新时还原
    public ArrayList<RSSUrl> getOriginalData(){
        return bkfinds;
    }

    @Override
    public Object getItem(int position) {
        return finds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            //设置当前布局为item
            convertView = inflater.inflate(R.layout.item_find, null);

            //绑定item的控件到vh上
            vh = new ViewHolder();
            vh.RSS_icon = convertView.findViewById(R.id.RSSIcon);
            vh.RSS_link = convertView.findViewById(R.id.RSSLink);
            vh.RSS_title = convertView.findViewById(R.id.RSSTitle);
            vh.ar=convertView.findViewById(R.id.addORremove);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        //设置好控件的内容
        if (finds.get(position).getStatus()== SubscribeStatus.NO_SUBSCRIBE)
        vh.ar.setBackgroundResource(android.R.drawable.checkbox_off_background);//设置未选中
        else
            vh.ar.setBackgroundResource(android.R.drawable.checkbox_on_background);//设置选中
        vh.RSS_title.setText(finds.get(position).getName());//设置标题
        vh.RSS_link.setText(finds.get(position).getUrl());//设置链接
        vh.RSS_icon.setImageResource(android.R.drawable.ic_menu_compass);//设置默认图标
        vh.ar.setOnClickListener(this);
        vh.ar.setTag(position);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mfilter == null)
            mfilter = new RSSFilter();
        return mfilter;
    }

    @Override
    public void onClick(View v) {
        clickListener.subClick(v);
    }

    //内部类用于保存item各个组件
    class ViewHolder{

        public ImageView RSS_icon;
        public TextView RSS_title;
        public TextView RSS_link;
        public Button ar;
    }
    //过滤器
    class RSSFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //用于定义过滤规则
            FilterResults result = new FilterResults();
            ArrayList<RSSUrl> templist;
            templist=finds;
            if (TextUtils.isEmpty(constraint)) {//当过滤的关键字为空的时候，我们则显示所有的数据


            } else {//否则把符合条件的数据对象添加到集合中
                RSSUrlDALImpl sql=new RSSUrlDALImpl(context);
                //不能给templist赋值，否则会影响指向同一内存的finds的数据
                templist.clear();
                templist.addAll(sql.getQueryData(new ArrayList<RSSUrl>(),constraint.toString()));
                //templist = ;
              /*  for (RSSUrl tempRSS : finds) {
                    //匹配RSS源的标题，组名
                    if (tempRSS.getName().contains(constraint) || tempRSS.getGroupName().
                            contains(constraint)
                            ) {
                        templist.add(tempRSS);
                    }
                }*/
            }
            result.values = templist; //将得到的集合保存到FilterResults的value变量中
            result.count = templist.size();//将集合的大小保存到FilterResults的count变量中
            /*if (result.count>0)
            System.out.println("过滤后:"+templist.get(0).getUrl()+", "+templist.get(0).getName());*/
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            finds = (ArrayList<RSSUrl>) results.values;
            for (int i=0;i<finds.size();i++) {
                System.out.println("过滤后,当前是第"+i +"个,链接为"+ finds.get(i).getUrl() + ", 标题是" + finds.get(i).getName());
            }
            //通知数据改变
            notifyDataSetChanged();
            //检索不到数据则提示空
            if (finds.size()==0)
                Toast.makeText(context,"TAT没有对应数据:)", Toast.LENGTH_SHORT).show();
        }
    }

}
