package xyz.somelou.rss.db.impl;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import xyz.somelou.rss.R;
import xyz.somelou.rss.bean.RSSUrl;
import xyz.somelou.rss.db.RSSUrlDAL;
import xyz.somelou.rss.enums.SubscribeStatus;
import xyz.somelou.rss.utils.RSSUtil;

/**
 * @author somelou
 * @description
 * @date 2019-06-25
 */
public class RSSUrlDALImpl extends BaseDALImpl implements RSSUrlDAL {

    public RSSUrlDALImpl(Context context) {
        super(context);
        TABLE_NAME = context.getString(R.string.table_rss_url);
        //databaseHelper=new DatabaseHelper(context);
    }

    /**
     * getAllData()
     * 查询表rss_url全部内容,需要有个容器存放，以供使用，定义了一个ArrayList类的list
     * 使用游标Cursor从表中查询数据,第一个参数："表名"，中间5个：null，查询出来内容的排序方式："stuid DESC"
     *
     * @param rssUrlArrayList ArrayList<RSSUrl>
     * @return list
     */
    @Override
    public ArrayList<RSSUrl> getAllData(ArrayList<RSSUrl> rssUrlArrayList) {
        //可以改成HashMap
        db = databaseHelper.getReadableDatabase();
        /*
         * 使用 adapter.notifyDataSetChanged() 时，必须保证传进 Adapter 的数据 List 是同一个 List
         * 而不能是其他对象，否则无法更新 listview。
         * 即，你可以调用 List 的 add()， remove()， clear()，addAll() 等方法，
         * 这种情况下，List 指向的始终是你最开始 new 出来的 ArrayList ，
         * 然后调用 adapter.notifyDataSetChanged() 方法，可以更新 ListView；
         * 但是如果你重新 new 了一个 ArrayList（重新申请了堆内存），
         * 那么这时候，List 就指向了另外一个 ArrayLIst，
         * 这时调用 adapter.notifyDataSetChanged() 方法，就无法刷新 listview 了。*/
        //ArrayList<Student> list = new ArrayList<Student>();
        Cursor cursor = getData("SELECT * FROM " + TABLE_NAME);
        return getDataFromCursor(rssUrlArrayList,cursor);
    }

    @Override
    public ArrayList<RSSUrl> getSubscribe(ArrayList<RSSUrl> rssUrls) {
        Cursor cursor = getData("SELECT * FROM " + TABLE_NAME + " where status='SUBSCRIBED'");
        return getDataFromCursor(rssUrls,cursor);
    }

    @Override
    public ArrayList<RSSUrl> getQueryData(ArrayList<RSSUrl> rssUrlArrayList, String query) {
        db = databaseHelper.getReadableDatabase();
        Cursor cursor = getData("select * from " + TABLE_NAME + " where name like '%" + query + "%' or " + "group_name like '%" + query +
                "%' or url like '%" + query + "%'");
        return getDataFromCursor(rssUrlArrayList,cursor);
    }

    @Override
    public RSSUrl getOneData(Integer id) {
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE url_id = "+id;
        Cursor cursor=getData(sql);
        return getDataFromCursor(cursor);
    }

    @Override
    public long insertOneData(String url, String groupName, SubscribeStatus status) {
        db=databaseHelper.getWritableDatabase();
        String sql="INSERT INTO "+TABLE_NAME+" VALUES (null,?,?,?,?,?)";

        RSSUtil u=new RSSUtil(url);
        SQLiteStatement statement=db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,u.getTitleName());
        statement.bindString(2,url);
        if (groupName==null||groupName.trim().equals(""))
            statement.bindString(3,"默认");
        else
            statement.bindString(3,groupName);
        statement.bindString(4,status.toString());
        statement.bindLong(5, u.getFeedSize());

        long result=statement.executeInsert();
        db.close();
        return result;
    }

    @Override
    public int deleteOneData(Integer id) {
        db=databaseHelper.getWritableDatabase();

        String sql = "DELETE FROM "+TABLE_NAME+" WHERE url_id = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindLong(1, id);

        int result=statement.executeUpdateDelete();
        db.close();
        return result;
    }

    @Override
    public int updateName(Integer id, String name) {
        db = databaseHelper.getWritableDatabase();

        String sql = "UPDATE "+TABLE_NAME+" SET name = ? WHERE url_id = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.clearBindings();
        statement.bindString(1,name);
        statement.bindLong(2,id);

        int result=statement.executeUpdateDelete();
        db.close();
        return result;
    }

    @Override
    public int updateGroupName(Integer id, String groupName) {
        db = databaseHelper.getWritableDatabase();

        String sql = "UPDATE "+TABLE_NAME+" SET group_name = ? WHERE url_id = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.clearBindings();
        statement.bindString(1,groupName);
        statement.bindLong(2,id);

        int result=statement.executeUpdateDelete();
        db.close();
        return result;
    }

    @Override
    public int updateSubscribeStatus(Integer id, SubscribeStatus status) {
        db = databaseHelper.getWritableDatabase();

        String sql = "UPDATE "+TABLE_NAME+" SET status = ? WHERE url_id = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.clearBindings();
        statement.bindString(1,status.toString());
        statement.bindLong(2,id);

        int result=statement.executeUpdateDelete();
        db.close();
        return result;
    }

    private ArrayList<RSSUrl> getDataFromCursor(ArrayList<RSSUrl> rssUrlArrayList,Cursor cursor){
        //让游标从表头游到表尾,并把数据存放到list中
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("url_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String groupName = cursor.getString(cursor.getColumnIndex("group_name"));
            String status =cursor.getString(cursor.getColumnIndex("status"));
            int count=cursor.getInt(cursor.getColumnIndex("count"));
            //Log.i("RSSUrlDALImpl.java  ","get name:" + name + ",id:" + id + "，groupName:" + groupName+", status:"+status+",count:"+count);
            //byte[] stuPic = cursor.getBlob(cursor.getColumnIndex("pic"));
            rssUrlArrayList.add(new RSSUrl(id, url, name,groupName,status,count));
        }
        cursor.close();
        return rssUrlArrayList;
    }

    private RSSUrl getDataFromCursor(Cursor cursor){
        //让游标从表头游到表尾,并把数据存放到list中r
        RSSUrl rssUrl=null;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("url_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String groupName = cursor.getString(cursor.getColumnIndex("group_name"));
            String status =cursor.getString(cursor.getColumnIndex("status"));
            int count=cursor.getInt(cursor.getColumnIndex("count"));
            //Log.i("RSSUrlDALImpl当前频道文章数--",Integer.toString(count));
            rssUrl=new RSSUrl(id, url, name ,groupName,status,count);
        }
        cursor.close();
        return rssUrl;
    }
}
