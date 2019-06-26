package xyz.somelou.rss.db.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import xyz.somelou.rss.db.DatabaseHelper;

/**
 * @author somelou
 * @description
 * @date 2019-06-26
 */
public class BaseDALImpl {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    String TABLE_NAME;

    BaseDALImpl(Context context){
        databaseHelper=new DatabaseHelper(context);
    }

    /**
     * 执行sql，返回Cursor
     * @param sql String
     * @return Cursor
     */
    Cursor getData(String sql) throws SQLException {
        return databaseHelper.getReadableDatabase().rawQuery(sql, null);
    }
}
