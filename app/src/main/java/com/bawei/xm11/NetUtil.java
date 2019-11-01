package com.bawei.xm11;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * author: 斛万珺
 * data: 2019/11/1 19:19:45
 * function:
 */
public class NetUtil {

    private NetUtil() {
    }
    public static NetUtil getInstance(){
        return net.netUtil;
    }
    private static class net{
        private static NetUtil netUtil=new NetUtil();
    }

    public String io2String(InputStream inputStream) {
        byte[] bytes = new byte[1024];
        int len = -1;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String json = "";
        try {
            while (((len = inputStream.read(bytes)) != -1)) {
                byteArrayOutputStream.write(bytes, 0, len);
            }
            byte[] bytes1 = byteArrayOutputStream.toByteArray();
            json = new String(bytes1);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return json;
    }

    public Bitmap io2Bitmap(InputStream inputStream){
        return BitmapFactory.decodeStream(inputStream);
    }


    @SuppressLint("StaticFieldLeak")
    public void doGet(final String httpUrl, final MyCallBack myCallBack){

        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPostExecute(String string) {
                myCallBack.onDoGetSuccess(string);
            }

            @Override
            protected String doInBackground(String... strings) {
                String json="";
                HttpURLConnection httpURLConnection=null;
                InputStream inputStream=null;
                try {
                    URL url = new URL(httpUrl);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.connect();
                    if (httpURLConnection.getResponseCode()==200){
                        inputStream= httpURLConnection.getInputStream();
                        json= io2String(inputStream);
                    }else {
                        Log.i("a", "连接失败");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {

                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return json;
            }
        }.execute();
    }
    public void doGetPhoto(final String httpUrl, final MyCallBack myCallBack){
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                myCallBack.onDoGetPhotoSuccess(bitmap);
            }

            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bitmap=null;
                HttpURLConnection httpURLConnection=null;
                InputStream inputStream=null;
                try {
                    URL url = new URL(httpUrl);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.connect();
                    if (httpURLConnection.getResponseCode()==200){
                        inputStream= httpURLConnection.getInputStream();
                        bitmap= io2Bitmap(inputStream);
                    }else {
                        Log.i("a", "连接失败");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {

                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }

                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return bitmap;
            }
        }.execute();
    }

    public interface MyCallBack {
        void onDoGetSuccess(String string);

        void onDoGetPhotoSuccess(Bitmap bitmap);
    }
}
