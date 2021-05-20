package easyny.ludashen.com.easyny.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PasswordMag extends SQLiteOpenHelper {

    public PasswordMag(Context context) {
        super(context, "pass.db", null, 1);//可以多加一个name参数代表操作多个数据库
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table pass(name varchar(20) primary key,zhanghao varchar(30),pass varchar(30))");
        String sql="insert into pass(name,zhanghao,pass) values('管理员','root','123456')";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

}