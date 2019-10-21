package com.example.ftpmanage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ftpmanage.utils.ConstantUtil;

public class DBHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String SWORD="SWORD";

    //三个不同参数的构造函数
    //带全部参数的构造函数，此构造函数必不可少
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //带两个参数的构造函数，调用的其实是带三个参数的构造函数
    public DBHelper(Context context){
        this(context, ConstantUtil.DATABASE_NAME,VERSION);
    }

    //带两个参数的构造函数，调用的其实是带三个参数的构造函数
    public DBHelper(Context context,String name){
        this(context,name,VERSION);
    }

    //带三个参数的构造函数，调用的是带所有参数的构造函数
    public DBHelper(Context context,String name,int version){
        this(context, name,null,version);
    }

    //创建数据库
    public void onCreate(SQLiteDatabase db) {
        //创建数据库sql语句
        StringBuilder sql = new StringBuilder();
        sql.append("create table FtpList(");
        sql.append("fid INTEGER PRIMARY KEY AUTOINCREMENT");
        sql.append(",ftpName NVARCHAR(50)");
        sql.append(",ftpHost NVARCHAR(200)");
        sql.append(",ftpPort INTEGER");
        sql.append(",ftpHost1 NVARCHAR(200)");
        sql.append(",ftpPort1 INTEGER");
        sql.append(",ftpUserName NVARCHAR(200)");
        sql.append(",ftpUserPwd NVARCHAR(200)");
        sql.append(",anonymousLogin INTEGER");
        sql.append(",domainToIp INTEGER");
        sql.append(",ftpMode INTEGER");
        sql.append(",ftpEncoded NVARCHAR(200)");
        sql.append(",creatorDate DATETIME");
        sql.append(",updateDate DATETIME");
        sql.append(")");
        //执行创建数据库操作
        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}