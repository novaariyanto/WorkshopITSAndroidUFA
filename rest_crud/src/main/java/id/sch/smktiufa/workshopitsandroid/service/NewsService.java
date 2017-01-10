package id.sch.smktiufa.workshopitsandroid.service;

import java.io.CharArrayReader;
import java.util.List;

import id.sch.smktiufa.workshopitsandroid.entity.News;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by smktiufa on 17/12/16.
 */

public interface NewsService {

    /**
     * delete news frm server
     * @return
     */
    @GET("news")
    Call<List<News>> getNews();
    /**
     * delete news frm server
     * @param id
     * @return
     */
    @GET("news/{id}")
    Call<List<News>> getNews(@Path("id") int id);
    /**
     * delete news frm server
     * @return
     */
    @POST("news")
    Call<News> postNews(@Body News news);

    /**
     * delete news frm server
     * @param id
     * @return
     */
    @DELETE("news/{id}")
    Call<News> deleteNews(@Path("id") long id);
    /**
     * delete news frm server
     * @param id
     * @return
     */
    @PUT("news/{id}")
    Call<News> putNews(@Path("id") int id, @Body News news);


}
