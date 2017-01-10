package id.sch.smktiufa.workshopitsandroid.content.database.adapter;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import id.sch.smktiufa.workshopitsandroid.adapter.NewsAdapter;
import id.sch.smktiufa.workshopitsandroid.content.NewsContentProvider;
import id.sch.smktiufa.workshopitsandroid.content.NewsDatabase;
import id.sch.smktiufa.workshopitsandroid.content.database.model.NewsDatabaseModel;
import id.sch.smktiufa.workshopitsandroid.entity.News;

/**
 * Created by smktiufa on 17/12/16.
 */

public class NewsDatabaseAdapter {
    private Uri dbUriNews = Uri.parse(NewsContentProvider.CONTENT_PATH + NewsContentProvider.TABLES[0]);
    ;
    private Context context;

    public NewsDatabaseAdapter(Context context) {
        this.context = context;
    }

    public void save(News news) {
        ContentValues values = new ContentValues();
        if (news.getId() == -1) {
            values.put(NewsDatabaseModel.TITLE, news.getTitle());
            values.put(NewsDatabaseModel.CONTENT, news.getContent());
            values.put(NewsDatabaseModel.STATUS, 1);
            values.put(NewsDatabaseModel.CREATE_DATE, news.getCreateDate());

            context.getContentResolver().insert(dbUriNews, values);
        } else {
            values.put(NewsDatabaseModel.TITLE, news.getTitle());
            values.put(NewsDatabaseModel.CONTENT, news.getContent());
            values.put(NewsDatabaseModel.STATUS, 1);
            values.put(NewsDatabaseModel.CREATE_DATE, news.getCreateDate());

            context.getContentResolver().update(dbUriNews, values, NewsDatabaseModel.ID + "=?", new String[]{news.getId() + ""});
        }
    }

    public List<News> findNewsByTitle(String title) {
        String query = NewsDatabaseModel.TITLE + " like ? AND " + NewsDatabaseModel.STATUS + " =? ";
        String[] parameter = { "%" + title + "%", "1"};

        Cursor cursor = context.getContentResolver().query(dbUriNews, null, query, parameter, NewsDatabaseModel.CREATE_DATE);

        List<News> newses = new ArrayList<News>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    News news = new News();
                    news.setId(cursor.getInt(cursor.getColumnIndex(NewsDatabaseModel.ID)));
                    news.setTitle(cursor.getString(cursor.getColumnIndex(NewsDatabaseModel.TITLE)));
                    news.setContent(cursor.getString(cursor.getColumnIndex(NewsDatabaseModel.CONTENT)));
                    news.setStatus(cursor.getInt(cursor.getColumnIndex(NewsDatabaseModel.STATUS)));
                    news.setCreateDate(cursor.getLong(cursor.getColumnIndex(NewsDatabaseModel.CREATE_DATE)));

                    newses.add(news);
                }
            }
        }
        cursor.close();
        return newses;
    }


    public List<News> findNewsAll() {
        String query = NewsDatabaseModel.STATUS + " = ?";
        String[] parameter = { "1" };
        Cursor cursor = context.getContentResolver().query(dbUriNews, null, query, parameter,
                NewsDatabaseModel.CREATE_DATE);
        List<News> categories = new ArrayList<News>();
        if(cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    News news = new News();
                    news.setId(cursor.getInt(cursor.getColumnIndex(NewsDatabaseModel.ID)));
                    news.setTitle(cursor.getString(cursor.getColumnIndex(NewsDatabaseModel.TITLE)));news.setContent(cursor.getString(cursor.getColumnIndex(NewsDatabaseModel.CONTENT)));
                    news.setStatus(cursor.getInt(cursor.getColumnIndex(NewsDatabaseModel.STATUS)));
                    news.setCreateDate(cursor.getLong(cursor.getColumnIndex(NewsDatabaseModel.CREATE_DATE)));
                    categories.add(news);
                }
            }
        }
        cursor.close();
        return categories;
    }
    public News findNewsById(long id) {
        String query = NewsDatabaseModel.ID + "=?";
        String[] parameter = {id + "1"};

        Cursor cursor = context.getContentResolver().query(dbUriNews, null, query, parameter, null);
        News news = null;

        if (cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                news = new News();
                news.setId(cursor.getInt(cursor.getColumnIndex(NewsDatabaseModel.ID)));
                news.setTitle(cursor.getString(cursor.getColumnIndex(NewsDatabaseModel.TITLE)));
                news.setContent(cursor.getString(cursor.getColumnIndex(NewsDatabaseModel.CONTENT)));
                news.setStatus(cursor.getInt(cursor.getColumnIndex(NewsDatabaseModel.STATUS)));
                news.setCreateDate(cursor.getLong(cursor.getColumnIndex(NewsDatabaseModel.CREATE_DATE)));

            } catch (Exception e) {
                e.printStackTrace();
                news = null; }
        }

    cursor.close();
    return news;
}

    public void delete(News news ){
        ContentValues values = new ContentValues();
        values.put(NewsDatabaseModel.STATUS,0);

        context.getContentResolver().update(dbUriNews,values,NewsDatabaseModel.ID + "=?" ,new String[]{news.getId()+""});
    }

}
