package com.jiwoolee.android_login_mongodb;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IMyService {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("student_id") String email, @Field("student_name") String name, @Field("student_password") String password);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("student_id") String email, @Field("student_password") String password);
}
