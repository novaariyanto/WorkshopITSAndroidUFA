package id.sch.smktiufa.workshopitsandroid.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import id.sch.smktiufa.workshopitsandroid.R;
import id.sch.smktiufa.workshopitsandroid.adapter.NewsAdapter;
import id.sch.smktiufa.workshopitsandroid.content.database.adapter.NewsDatabaseAdapter;
import id.sch.smktiufa.workshopitsandroid.entity.News;

/**
 * Created by smktiufa on 15/12/16.
 */

public class ActivityNews extends AppCompatActivity {
    private RecyclerView recycleNews;
    private NewsAdapter newsAdapter;
    private NewsDatabaseAdapter newsDatabaseAdapter;

    private EditText editTitle;
    private EditText editContent;
    private News news = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        recycleNews = (RecyclerView) findViewById(R.id.recyclenews);
        editTitle = (EditText) findViewById(R.id.edt_title);
        editContent = (EditText) findViewById(R.id.edt_content);

        newsAdapter = new NewsAdapter(this);
        newsDatabaseAdapter = new NewsDatabaseAdapter(this);


        recycleNews.setLayoutManager(new LinearLayoutManager(this));
        recycleNews.setItemAnimator(new DefaultItemAnimator());
        recycleNews.setAdapter(newsAdapter);

        newsAdapter.addNewses(newsDatabaseAdapter.findNewsAll());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                newsAdapter.clearNews();
                newsAdapter.addNewses(newsDatabaseAdapter.findNewsByTitle(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d(getClass().getSimpleName(), "ppppppp");

                newsAdapter.clearNews();
                newsAdapter.addNewses(newsDatabaseAdapter.findNewsAll());
                return false;

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Toast.makeText(this, R.string.search, Toast.LENGTH_LONG).show();
        } else if (id == R.id.action_save) {

            saveNews();

            newsAdapter.clearNews();
            newsAdapter.addNewses(newsDatabaseAdapter.findNewsAll());
        } else if (id == R.id.action_refresh) {
           newsAdapter.clearNews();
            newsAdapter.addNewses(newsDatabaseAdapter.findNewsAll());

        }
        return true;
    }

    public void saveNews() {

        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();

        if (!title.equals("") && !content.equals("")) {

            if (news == null){

            news = new News();

            news.setTitle(editTitle.getText().toString());
            news.setContent(editContent.getText().toString());
            news.setCreateDate(new Date().getTime());
            newsDatabaseAdapter.save(news);
            clearForm();
        }else {
                news.setTitle(editTitle.getText().toString());
                news.setContent(editContent.getText().toString());
                news.setCreateDate(new Date().getTime());
                newsDatabaseAdapter.save(news);
                news  = null;
                clearForm();
            }
        }}
    public void editNews (News news){

        this.news = news;

        editTitle.setText(news.getTitle());
        editContent.setText(news.getContent());

    }

    private void clearForm() {
        editContent.getText().clear();
        editTitle.getText().clear();
    }
    public void deletenews(News news){

        newsDatabaseAdapter.delete(news);

        newsAdapter.clearNews();
        newsAdapter.addNewses(newsDatabaseAdapter.findNewsAll());
    }
}

