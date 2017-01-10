package id.sch.smktiufa.workshopitsandroid.activity;

import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.path.android.jobqueue.JobManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.sch.smktiufa.workshopitsandroid.AppAplication;
import id.sch.smktiufa.workshopitsandroid.R;
import id.sch.smktiufa.workshopitsandroid.adapter.NewsAdapter;

import id.sch.smktiufa.workshopitsandroid.entity.Category;
import id.sch.smktiufa.workshopitsandroid.entity.News;
import id.sch.smktiufa.workshopitsandroid.job.CategoryJob;
import id.sch.smktiufa.workshopitsandroid.job.EventMessage;
import id.sch.smktiufa.workshopitsandroid.job.NewsJob;

/**
 * Created by smktiufa on 15/12/16.
 */

public class ActivityNews extends AppCompatActivity {
    public int PROCESS_ID_GET_ALL_NEWS = 1;
    public int PROCESS_ID_POST_NEWS = 2;
    public int PROCESS_ID_PUT_NEWS = 3;
    public int PROCESS_ID_DELETE_NEWS = 4;

    public int PROCCES_ID_GET_ALL_CATEGORY = 20;


    private RecyclerView recycleNews;
    private Spinner spincategory;
    private NewsAdapter newsAdapter;


    private EditText editTitle;
    private EditText editContent;
    private News news = null;
    private List<Category> categories = new ArrayList<>();

    private JobManager jobManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        spincategory = (Spinner) findViewById(R.id.spin_category);
        recycleNews = (RecyclerView) findViewById(R.id.recyclenews);
        editTitle = (EditText) findViewById(R.id.edt_title);
        editContent = (EditText) findViewById(R.id.edt_content);

        newsAdapter = new NewsAdapter(this);
        jobManager = AppAplication.getInstance().getJobManager();

        recycleNews.setLayoutManager(new LinearLayoutManager(this));
        recycleNews.setItemAnimator(new DefaultItemAnimator());
        recycleNews.setAdapter(newsAdapter);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.app_name));
        progressDialog.setMessage(getString(R.string.text_wait));


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
//               newsAdapter.clearNews();
//                newsAdapter.addNewses(newsDatabaseAdapter.findNewsByTitle(query));
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

//                newsAdapter.clearNews();
//                newsAdapter.addNewses(newsDatabaseAdapter.findNewsAll());
                return false;

            }
        });
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        jobManager.addJobInBackground(new CategoryJob(PROCCES_ID_GET_ALL_CATEGORY, CategoryJob.GET_ALL, null));
        jobManager.addJobInBackground(new NewsJob(PROCESS_ID_GET_ALL_NEWS, NewsJob.GET_ALL, null));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage message) {

        if (message.getStatus() == EventMessage.ADD) {
            if (message.getId() == PROCESS_ID_GET_ALL_NEWS) {
                progressDialog.show();
            }
        } else if (message.getStatus() == EventMessage.SUCCES) {

            if (message.getId() == PROCESS_ID_GET_ALL_NEWS) {
                progressDialog.dismiss();

                List<News> newsList = (List<News>) message.getContent();
                newsAdapter.clearNews();
                newsAdapter.addNewses(newsList);

            } else if (message.getId() == PROCESS_ID_POST_NEWS) {

                Toast.makeText(this, "Posting news succes", Toast.LENGTH_LONG).show();
                jobManager.addJobInBackground(new NewsJob(PROCESS_ID_GET_ALL_NEWS, NewsJob.GET_ALL, null));

            } else if (message.getId() == PROCESS_ID_PUT_NEWS) {
                Toast.makeText(this, "Update news succes", Toast.LENGTH_LONG).show();
                jobManager.addJobInBackground(new NewsJob(PROCESS_ID_GET_ALL_NEWS, NewsJob.GET_ALL, null));
            } else if (message.getId() == PROCESS_ID_DELETE_NEWS) {
                Toast.makeText(this, "Delete news succes", Toast.LENGTH_LONG).show();
                jobManager.addJobInBackground(new NewsJob(PROCESS_ID_GET_ALL_NEWS, NewsJob.GET_ALL, null));
            } else if (message.getId() == PROCCES_ID_GET_ALL_CATEGORY) {
                categories = (List<Category>) message.getContent();

                ArrayAdapter<Category> arrayAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_dropdown_item_1line, categories);
                spincategory.setAdapter(arrayAdapter);

            }

        } else if (message.getStatus() == EventMessage.ERROR) {
            if (message.getId() == PROCESS_ID_GET_ALL_NEWS) {
                progressDialog.dismiss();
            } else if (message.getId() == PROCESS_ID_POST_NEWS) {
                progressDialog.dismiss();
                Toast.makeText(this, "Posting news failed", Toast.LENGTH_LONG).show();
            } else if (message.getId() == PROCESS_ID_PUT_NEWS) {
                progressDialog.dismiss();
                Toast.makeText(this, "Put news failed", Toast.LENGTH_LONG).show();
            } else if (message.getId() == PROCESS_ID_DELETE_NEWS) {
                progressDialog.dismiss();
                Toast.makeText(this, "Delete news failed", Toast.LENGTH_LONG).show();
            }


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {

            Toast.makeText(this, R.string.search, Toast.LENGTH_LONG).show();

        } else if (id == R.id.action_save) {

            saveNews();

        } else if (id == R.id.action_refresh) {

            jobManager.addJobInBackground(new NewsJob(PROCESS_ID_GET_ALL_NEWS, NewsJob.GET_ALL, null));

        }
        return true;
    }

    public void saveNews() {

        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();

        if (!title.equals("") && !content.equals("")) {

            if (news == null) {

                Category category = categories.get(spincategory.getSelectedItemPosition());

                news = new News();
                news.setTitle(editTitle.getText().toString());
                news.setContent(editContent.getText().toString());
                news.setCreateDate(new Date().getTime());

                news.setCategory(category);

                jobManager.addJobInBackground(new NewsJob(PROCESS_ID_POST_NEWS, NewsJob.POST, news));

                news = null;
                clearForm();

            } else {
                news.setTitle(editTitle.getText().toString());
                news.setContent(editContent.getText().toString());
                news.setCreateDate(new Date().getTime());

                jobManager.addJobInBackground(new NewsJob(PROCESS_ID_PUT_NEWS, NewsJob.PUT, news));

                news = null;
                clearForm();
            }
        }
    }


    public void editNews(News news) {

        this.news = news;

        editTitle.setText(news.getTitle());
        editContent.setText(news.getContent());

    }

    private void clearForm() {
        editContent.getText().clear();
        editTitle.getText().clear();
    }

    public void deletenews(News news) {

        jobManager.addJobInBackground(new NewsJob(PROCESS_ID_DELETE_NEWS, NewsJob.DELETE, news));
    }

}

