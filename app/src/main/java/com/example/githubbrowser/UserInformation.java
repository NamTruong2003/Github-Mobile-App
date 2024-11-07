package com.example.githubbrowser;

public class UserInformation {
    private static String ACCESS_TOKEN;

    public static String getAccessToken() {
        return ACCESS_TOKEN;
    }

    public static void setAccessToken(String accessToken){
        ACCESS_TOKEN = accessToken;
    }


}
