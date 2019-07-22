package com.example.cxdd.miniDou.newtork;

import com.example.cxdd.miniDou.bean.FeedResponse;
import com.example.cxdd.miniDou.bean.PostVideoResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * @author Xavier.S
 * @date 2019.01.17 20:38
 */
public interface IMiniDouyinService {
    String HOST = "http://test.androidcamp.bytedance.com/";
    //http://test.androidcamp.bytedance.com/mini_douyin/invoke/video

    @Multipart
    @POST("/mini_douyin/invoke/video")
    Call<PostVideoResponse> createVideo(
            @Query("student_id") String studentId,
            @Query("user_name") String userName,
            @Part MultipartBody.Part image, @Part MultipartBody.Part video);

    @GET("/mini_douyin/invoke/video")
    Call<FeedResponse> fetchFeed();
}
