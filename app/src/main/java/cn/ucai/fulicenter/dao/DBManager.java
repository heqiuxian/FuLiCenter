package cn.ucai.fulicenter.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.ucai.fulicenter.bean.User;

/**
 * Created by Administrator on 2016/10/24.
 */

public class DBManager {
    private static DBManager dbMgr=new DBManager();
    private DBOpenHelper dbHelper;
    void onInit(Context context){
        dbHelper=new DBOpenHelper(context);
    }
    public static synchronized DBManager getInstance(){
        return dbMgr;
    }
    public synchronized void closeDB(){
        if(dbHelper!=null){
            dbHelper.closeDB();
        }
    }
    public synchronized  boolean saveUser(User user){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(UserDao.USER_COLUMN_NAME,user.getMuserName());
        values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
        values.put(UserDao.USER_COLUMN_AVATAR_ID,user.getMavatarId());
        values.put(UserDao.USER_COLUMN_AVATAR_TYPE,user.getMavatarType());
        values.put(UserDao.USER_COLUMN_AVATAR_PATH,user.getMavatarPath());
        values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,user.getMavatarSuffix());
        values.put(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME,user.getMavatarLastUpdateTime());
        if(db.isOpen()){
            return db.replace(UserDao.USER_TABLE_NAME,null,values)!=-1;
        }
        return false;
    }

    public synchronized User getUser(String userName){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql="select * from "+UserDao.USER_TABLE_NAME+" where "+UserDao.USER_COLUMN_NAME +" =?";
        Cursor c = db.rawQuery(sql, new String[]{userName});
        while(c.moveToNext()){
            User user = new User();
            user.setMuserName(userName);
            user.setMuserNick(c.getString(c.getColumnIndex(UserDao.USER_COLUMN_NICK)));
            user.setMavatarId(c.getInt(c.getColumnIndex(UserDao.USER_COLUMN_AVATAR_ID)));
            user.setMavatarType(c.getInt(c.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE)));
            user.setMavatarPath(c.getString(c.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH)));
            user.setMavatarSuffix(c.getString(c.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX)));
            user.setMavatarLastUpdateTime(c.getString(c.getColumnIndex(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME)));
            return user;
            }
            return null;
        }
    public synchronized boolean updateUser(User user){
        int result=-1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String where=UserDao.USER_COLUMN_NAME+" =?";
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
        if(db.isOpen()){
            result=db.update(UserDao.USER_TABLE_NAME,values,where,new String[]{user.getMuserName()});
        }
        return result>0;
    }
}
