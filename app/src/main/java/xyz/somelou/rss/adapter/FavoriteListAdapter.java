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
import xyz.somelou.rss.bean.FavorRSSItem;

/**
 * Created by ${Marrrrrco} on 2019/7/3.
 */

public class FavoriteListAdapter extends BaseAdapter {
    private ArrayList<FavorRSSItem> passages;
    private Context context;

    public FavoriteListAdapter(){}

    public FavoriteListAdapter(Context context,ArrayList<FavorRSSItem> list){
        this.context=context;
        passages=list;
    }

    @Override
    public int getCount() {
        return passages.size();
    }

    @Override
    public Object getItem(int position) {
        return passages.get(position);
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
            convertView = inflater.inflate(R.layout.item_channel, null);

            //绑定item的控件到vh上
            vh = new ViewHolder();
            vh.icon = convertView.findViewById(R.id.passage_icon);
            vh.title = convertView.findViewById(R.id.passage_title);
            vh.discription = convertView.findViewById(R.id.passage_discription);
            vh.date = convertView.findViewById(R.id.passage_date);
            vh.author = convertView.findViewById(R.id.passage_author);

            convertView.setTag(vh);
        } else {
            vh =(ViewHolder)convertView.getTag();
        }
        //设置好控件的内容
        vh.icon.setImageResource(R.mipmap.article);
        vh.title.setText(passages.get(position).getTitleName());
        vh.discription.setText(passages.get(position).getDescription());
        vh.date.setText("收藏时间:" + passages.get(position).getFavorTime());
        vh.discription.setTextColor(context.getResources().getColor(R.color.tab_unchecked));
        vh.date.setTextColor(context.getResources().getColor(R.color.tab_unchecked));
        return convertView;
    }

    public class ViewHolder {
        public TextView title;//文章标题
        public TextView discription;//文章简述
        public TextView author;//文章作者
        public TextView date;//文章发行日期
        public ImageView icon;//默认图标
    }
}
