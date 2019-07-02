package xyz.somelou.rss.db.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import xyz.somelou.rss.R;
import xyz.somelou.rss.bean.FavorRSSItem;
import xyz.somelou.rss.db.FavorRSSItemDAL;

/**
 * @author somelou
 * @description
 * @date 2019-06-25
 */
public class FavorRSSItemDALImpl extends BaseDALImpl implements FavorRSSItemDAL {

    public FavorRSSItemDALImpl(Context context) {
        super(context);
        TABLE_NAME = context.getString(R.string.table_favor_rss_item);
        //databaseHelper=new DatabaseHelper(context);
    }

    @Override
    public ArrayList<FavorRSSItem> getAllData(ArrayList<FavorRSSItem> favorRSSItems) {
        Cursor cursor = getData("SELECT * FROM " + TABLE_NAME);
        return getDataFromCursor(favorRSSItems,cursor);
    }

    @Override
    public ArrayList<FavorRSSItem> getQueryData(ArrayList<FavorRSSItem> favorRSSItemArrayList, String query) {
        db = databaseHelper.getReadableDatabase();
        Cursor cursor = getData("select * from " + TABLE_NAME + " where name like '%" + query + "%' or " + "description like '%" + query +
                "%' or url like '%" + query + "%'");
        return getDataFromCursor(favorRSSItemArrayList,cursor);
    }

    @Override
    public FavorRSSItem getOneData(Integer id) {
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE item_id = "+id;
        Cursor cursor=getData(sql);
        return getDataFromCursor(cursor);
    }

    @Override
    public boolean isFavor(String itemUrl) {
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE url = '"+itemUrl+"';";
        Cursor cursor=getData(sql);
        return getDataFromCursor(cursor) != null;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public long insertOneData(String url,String titleName,String description) {
        db=databaseHelper.getWritableDatabase();
        String sql="INSERT INTO "+TABLE_NAME+" VALUES (null,?,?,?,?)";

        SQLiteStatement statement=db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,url);
        statement.bindString(2,titleName);
        statement.bindString(3,description);
        statement.bindString(4,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        long result=statement.executeInsert();
        db.close();
        return result;
    }

    @Override
    public int deleteOneData(Integer id) {
        db=databaseHelper.getWritableDatabase();

        String sql = "DELETE FROM "+TABLE_NAME+" WHERE item_id = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindLong(1, id);

        int result=statement.executeUpdateDelete();
        db.close();
        return result;
    }

    @Override
    public int deleteOneData(String itemUrl) {
        db=databaseHelper.getWritableDatabase();

        String sql = "DELETE FROM "+TABLE_NAME+" WHERE url = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, itemUrl);

        int result=statement.executeUpdateDelete();
        db.close();
        return result;
    }

    private ArrayList<FavorRSSItem> getDataFromCursor(ArrayList<FavorRSSItem> favorRSSItems, Cursor cursor){
        //让游标从表头游到表尾,并把数据存放到list中
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("item_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String favorTime =cursor.getString(cursor.getColumnIndex("favor_time"));

            //System.out.println("get name:" + name + ",id:" + id + "，description:" + description+", time:"+favorTime);
            //byte[] stuPic = cursor.getBlob(cursor.getColumnIndex("pic"));
            favorRSSItems.add(new FavorRSSItem(id,url,name,description,favorTime));
        }
        cursor.close();
        return favorRSSItems;
    }

    private FavorRSSItem getDataFromCursor(Cursor cursor){
        //让游标从表头游到表尾,并把数据存放到list中r
        FavorRSSItem rssUrl=null;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("item_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String favorTime =cursor.getString(cursor.getColumnIndex("favor_time"));

            //System.out.println("get name:" + name + ",id:" + id + "，description:" + description+", time:"+favorTime);

            //byte[] stuPic = cursor.getBlob(cursor.getColumnIndex("pic"));
            rssUrl=new FavorRSSItem(id,url,name,description,favorTime);
        }
        cursor.close();
        return rssUrl;
    }

    //删除所有数据但保留表
    public int deleteAllData() {
        db = databaseHelper.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_NAME;
        SQLiteStatement statement = db.compileStatement(sql);
        int result = statement.executeUpdateDelete();
        db.close();
        return result;
    }
}
