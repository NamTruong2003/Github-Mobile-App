package com.example.githubbrowser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Handler {
    private static final String TAG = Handler.class.getSimpleName();
    public Handler(){

    }
    public String httpServiceCall(String requestUrl,String GITHUB_TOKEN) throws MalformedURLException {
        String result = null;
        try{
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            result = convertResultToString(inputStream);
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        } catch (ProtocolException e){
            e.printStackTrace();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private String convertResultToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}
