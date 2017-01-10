package id.sch.smktiufa.workshopitsandroid.job;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import id.sch.smktiufa.workshopitsandroid.AppAplication;
import id.sch.smktiufa.workshopitsandroid.entity.Category;
import id.sch.smktiufa.workshopitsandroid.entity.News;
import id.sch.smktiufa.workshopitsandroid.service.CategoryService;
import id.sch.smktiufa.workshopitsandroid.service.NewsService;
import retrofit2.Response;

/**
 * Created by smktiufa on 18/12/16.
 */

public class CategoryJob extends Job {
    private int id;
    public static final int GET_ALL = 100;
    public static final int GET_BY_TITLE = 200;
    public static final int POST = 300;
    public static final int PUT = 400;
    public static final int DELETE = 500;

    private int status;
    private Category category;


    public CategoryJob(int id, int status, Category category) {
        super(new Params(1).requireNetwork().persist());
        this.id = id;
        this.status = status;
        this.category = category;
    }

    @Override
    public void onAdded() {

        Log.d(getClass().getSimpleName(), "news job added");
        EventBus.getDefault().post(new EventMessage(id, EventMessage.ADD, null));
    }

    @Override
    public void onRun() throws Throwable {

        Log.d(getClass().getSimpleName(), "news job running");
        CategoryService service = AppAplication
                .getInstance()
                .getRetrofit()
                .create(CategoryService.class);
        Log.d(getClass().getSimpleName(), "job status" + status);
        switch (status) {
            case GET_ALL:

                Response<List<Category>> responseGetAll = service.getCategories().execute();
                Log.d(getClass().getSimpleName(), "job get all");

                if (responseGetAll.isSuccessful()) {

                    Log.d(getClass().getSimpleName(), "response news size" + responseGetAll.body().size());
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCES, responseGetAll.body()));
                } else {

                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responseGetAll.body()));

                }
                break;
            case POST:

                Response<Category> responsePost = service.postCategories(category).execute();

                if (responsePost.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "posting news success" + category.getId());
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCES, responsePost.body()));
                } else {

                    Log.d(getClass().getSimpleName(), "posting news failed" + category.getId());
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responsePost.body()));
                }
                break;

//            case PUT:
//                Response<Category> responsePut = service.putCategories(category).execute();
//                if (responsePut.isSuccessful()) {
//                    Log.d(getClass().getSimpleName(), "put news success" + category.getId());
//                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCES, responsePut.body()));
//                } else {
//
//                    Log.d(getClass().getSimpleName(), "put news failed" + category.getId());
//                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responsePut.body()));
//                }
//
//            case DELETE:
//
//                Response<List<Category>> responsedelete = service.getCategories().execute();
//                if (responsedelete.isSuccessful()) {
//                    Log.d(getClass().getSimpleName(), "Delete news success" + category.getId());
//                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCES, responsedelete.body()));
//                } else {
//
//                    Log.d(getClass().getSimpleName(), "delete news failed" + category.getId());
//                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responsedelete.body()));
//                }
//                break;

        }
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

}
