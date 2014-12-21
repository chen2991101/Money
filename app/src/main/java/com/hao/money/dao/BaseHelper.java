package com.hao.money.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hao.money.entity.History;
import com.hao.money.entity.Record;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

/**
 * Created by hao on 2014/12/21.
 */
public class BaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something
    // appropriate for your app
    private static final String DATABASE_NAME = "demo.db";
    // any time you make changes to your database objects, you may have to
    // increase the database version
    private static final int DATABASE_VERSION = 1;
    // the DAO object we use to access the tables
    // 添加事物
    public DatabaseConnection conn;

    public BaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should
     * call createTable statements here to create the tables that will store
     * your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            conn = connectionSource.getSpecialConnection();
            TableUtils.createTable(connectionSource, History.class);
            TableUtils.createTable(connectionSource, Record.class);
            // TableUtils.createTable(connectionSource, ArticleResponse.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher
     * version number. This allows you to adjust the various data to match the
     * new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, History.class, true);
            TableUtils.dropTable(connectionSource, Record.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

}
