package id.sch.smktiufa.workshopitsandroid.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by smktiufa on 16/12/16.
 */

public class NewsContentProvider extends ContentProvider {

    private NewsDatabase dbHelper;
    public static final String TABLES[] = {
            NewsDatabase.TBL_NEWS,
    };

    public static final String AUTHORITY = NewsContentProvider.class.getName().toLowerCase();
    public static final String CONTENT_PATH = "content://" + AUTHORITY + "/";
    public UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH) {
        {
            int i = 0;
            for (String table : TABLES) {
                addURI(AUTHORITY, table, i);
                i++;
            }
        }
    };


    @Override
    public boolean onCreate() {

        dbHelper = new NewsDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        builder.setTables(TABLES[matcher.match(uri)]);
        Cursor cursor = builder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        long id = database.insert(TABLES[matcher.match(uri)], null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(TABLES[matcher.match(uri)] + "/" + id);

    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {


        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int delete = database.delete(TABLES[matcher.match(uri)], selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return delete;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int update = database.update(TABLES[matcher.match(uri)], contentValues, s, strings);
        getContext().getContentResolver().notifyChange(uri, null);
        return update;
    }
}
