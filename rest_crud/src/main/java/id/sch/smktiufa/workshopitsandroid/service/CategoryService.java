package id.sch.smktiufa.workshopitsandroid.service;

import java.util.List;

import id.sch.smktiufa.workshopitsandroid.entity.Category;
import id.sch.smktiufa.workshopitsandroid.entity.News;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by smktiufa on 18/12/16.
 */

public interface CategoryService {
    @GET("category")
    Call<List<Category>> getCategories();


    @GET("category/{id}")
    Call<List<Category>> getCategories(@Path("id") int id);

    @POST("category")
    Call<Category> postCategories(@Body Category category);

    @DELETE("category/{id}")
    Call<Category> deleteCategories(@Path("id") int id);

    @PUT("category/{id}")
    Call<Category> putCategories(@Path("id") int id, @Body News news);

}
