package com.example.githubbrowser.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import com.example.githubbrowser.notification.Notification;

import java.util.List;

public interface GitHubApiService {
    @GET("notifications")
    Call<List<Notification>> getNotifications(@Header("Authorization") String authHeader);
}
