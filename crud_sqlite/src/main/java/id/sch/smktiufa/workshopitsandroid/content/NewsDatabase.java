package id.sch.smktiufa.workshopitsandroid.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import id.sch.smktiufa.workshopitsandroid.content.database.model.NewsDatabaseModel;


/**
 * Created by smktiufa on 16/12/16.
 */

public class NewsDatabase extends SQLiteOpenHelper {
    public static final String DATABASE = "workshop";
    private static final int VERSION = 1;

    public static final String TBL_NEWS = "tbl_news";
    private Context context;

    public NewsDatabase(Context context) {

        super(context, DATABASE, null, VERSION);
    }

    public NewsDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

       sqLiteDatabase.execSQL("CREATE TABLE " + TBL_NEWS + "("
               + NewsDatabaseModel.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
               + NewsDatabaseModel.TITLE + " TEXT, "
               + NewsDatabaseModel.CONTENT + " TEXT, "
               + NewsDatabaseModel.CREATE_DATE + " INTEGER, "
               + NewsDatabaseModel.STATUS + " INTEGER)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i < i1) ;
    }
}
