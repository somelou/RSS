package xyz.somelou.rss.db.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import xyz.somelou.rss.R;
import xyz.somelou.rss.db.DatabaseHelper;
import xyz.somelou.rss.utils.RSSUtil;

/**
 * @author somelou
 * @description
 * @date 2019-06-26
 */
public class BaseDALImpl {

    // 数据库默认为空
    private boolean isEmpty;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    String TABLE_NAME;
    private Context context;

    public BaseDALImpl(Context context) {
        databaseHelper = new DatabaseHelper(context);
        this.context = context;
        // System.out.println("new in basedalimpl");
    }

    /**
     * 执行sql，返回Cursor
     *
     * @param sql String
     * @return Cursor
     */
    Cursor getData(String sql) throws SQLException {
        return databaseHelper.getReadableDatabase().rawQuery(sql, null);
    }

    /**
     * 预设置一些rss源
     * url从xml中获取
     */
    public void preInsertRssUrl() {
        if (isEmptyPreInsert()) {
            System.out.println(isEmpty);
            String[] RSS_URL_ARRAY = context.getResources().getStringArray(R.array.rss_url_array);
            RSSUtil rssUtil = new RSSUtil(RSS_URL_ARRAY[0]);

            // 插入一些rss源
            // 问题1：必须要和表的column数量一致，不能插入不饱和数据
            // 2：string的拼接
            String INSERT_RSS_URL =
                    "INSERT INTO RSS_URL SELECT 1 AS 'url_id','" + rssUtil.getTitleName() + "' AS 'name', '"
                            + RSS_URL_ARRAY[0] + "' AS 'url' ,'默认' AS 'GROUP_NAME','NO_SUBSCRIBE' AS 'status',' "+Integer.toString(rssUtil.getFeedSize())+"' AS 'count'";
            for (int i = 1; i < RSS_URL_ARRAY.length; i++) {
                rssUtil.setRssUrl(RSS_URL_ARRAY[i]);
                //Log.i("测试预设RSS",rssUtil.getValidate().toString());
                INSERT_RSS_URL += "UNION SELECT " + (i + 1) + ",'" + rssUtil.getTitleName() + "','"
                        + RSS_URL_ARRAY[i] + "','默认','NO_SUBSCRIBE',' "+Integer.toString(rssUtil.getFeedSize())+"'";
                //Log.i("title=",(i + 1)+ "  "+ rssUtil.getTitleName());
            }
            db = databaseHelper.getWritableDatabase();
            db.execSQL(INSERT_RSS_URL);
        }
    }

    private boolean isEmptyPreInsert() {
        String num = "select count(*) from " + context.getResources().getString(R.string.table_rss_url);
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(num, null);
        while (cursor.moveToNext()) {
            int result = cursor.getInt(cursor.getColumnIndex("count(*)"));
            isEmpty = ( result== 0);
        }
        cursor.close();
        return isEmpty;
    }
}
