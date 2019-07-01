package xyz.somelou.rss.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.somelou.rss.R;
import xyz.somelou.rss.bean.RSSUrl;

/**
 * Created by ${Marrrrrco} on 2019/6/30.
 */

public class RSSSubscribeAdapter extends BaseAdapter {

    private Context mcontext;
    private ArrayList<RSSUrl> subs;

    RSSSubscribeAdapter() {

    }

    public RSSSubscribeAdapter(Context c, ArrayList<RSSUrl> list) {
        mcontext = c;
        subs = list;
    }

    @Override
    public int getCount() {
        return subs.size();
    }

    @Override
    public Object getItem(int position) {
        return subs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mcontext);
            //设置当前布局为item
            convertView = inflater.inflate(R.layout.item_subscribe, null);

            //绑定item的控件到vh上
            vh = new ViewHolder();
            vh.sub_icon = convertView.findViewById(R.id.SubIcon);
            vh.sub_group = convertView.findViewById(R.id.SubGroup);
            vh.sub_title = convertView.findViewById(R.id.SubTitle);
            vh.sub_count = convertView.findViewById(R.id.SubCount);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        //设置好控件的内容
        vh.sub_title.setText(subs.get(position).getName());//设置标题
        vh.sub_group.setText(subs.get(position).getGroupName());//设置组别
        vh.sub_icon.setImageResource(android.R.drawable.ic_input_get);//设置默认图标
        vh.sub_count.setText(Integer.toString(subs.get(position).getCount()));//设置文章数量
        return convertView;
    }

    //内部类用于保存item各个组件
    class ViewHolder {

        public ImageView sub_icon;
        public TextView sub_title;
        public TextView sub_group;
        public TextView sub_count;
    }
}
