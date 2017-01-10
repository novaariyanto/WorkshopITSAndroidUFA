package id.sch.smktiufa.workshopitsandroid.job;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import id.sch.smktiufa.workshopitsandroid.AppAplication;
import id.sch.smktiufa.workshopitsandroid.entity.Category;
import id.sch.smktiufa.workshopitsandroid.entity.News;
import id.sch.smktiufa.workshopitsandroid.service.NewsService;
import retrofit2.Response;

/**
 * Created by smktiufa on 17/12/16.
 */

public class NewsJob extends Job {
    private int id;
    public static final int GET_ALL = 100;
    public static final int GET_BY_TITLE = 200;
    public static final int POST = 300;
    public static final int PUT = 400;
    public static final int DELETE = 500;

    private int status;
    private News news;


    public NewsJob(int id, int status, News news) {
        super(new Params(1));
        this.id = id;
        this.status = status;
        this.news = news;
    }

    @Override
    public void onAdded() {

        Log.d(getClass().getSimpleName(), "news job added");
        EventBus.getDefault().post(new EventMessage(id, EventMessage.ADD, null));
    }

    @Override
    public void onRun() throws Throwable {

        Log.d(getClass().getSimpleName(), "news job running");
        NewsService service = AppAplication
                .getInstance()
                .getRetrofit()
                .create(NewsService.class);
        Log.d(getClass().getSimpleName(), "job status" + status);
        switch (status) {
            case GET_ALL:

                Response<List<News>> responseGetAll = service.getNews().execute();
                Log.d(getClass().getSimpleName(), "job get all");

                if (responseGetAll.isSuccessful()) {

                    Log.d(getClass().getSimpleName(), "response news size" + responseGetAll.body().size());
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCES, responseGetAll.body()));
                } else {

                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responseGetAll.body()));

                }
                break;
            case POST:
                Response<News> responsePost = service.postNews(news).execute();

                if (responsePost.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "posting news success" + news.getId());
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCES, responsePost.body()));
                } else {

                    Log.d(getClass().getSimpleName(), "posting news failed" + news.getId());
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responsePost.body()));
                }
                break;

            case PUT:
                Response<News> responsePut = service.putNews(news.getId(), news).execute();

                if (responsePut.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "put news success" + news.getId());
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCES, responsePut.body()));
                } else {

                    Log.d(getClass().getSimpleName(), "put news failed" + news.getId());
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responsePut.body()));
                }
                break;
            case DELETE:
                Response<News> responsedelete = service.deleteNews(news.getId()).execute();

                if (responsedelete.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "Delete news success" + news.getId());
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCES, responsedelete.body()));
                } else {

                    Log.d(getClass().getSimpleName(), "delete news failed" + news.getId());
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responsedelete.body()));
                }
                break;

        }
    }

    @Override
    protected void onCancel() {
        Log.d(getClass().getSimpleName(), "news job Cancelled");
        EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, null));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
