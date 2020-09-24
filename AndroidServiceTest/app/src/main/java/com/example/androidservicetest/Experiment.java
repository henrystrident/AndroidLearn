package com.example.androidservicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Experiment extends AppCompatActivity {

    public interface DownloadListener
    {
        void onProgress(int progress);
        void onSuccess();
        void onFailed();
        void onPaused();
        void onCanceled();
    }

    public class DownloadTask extends AsyncTask<String, Integer ,Integer>
    {
        public static final int SUCCESS=0;
        public static final int FAILED=1;
        public static final int PAUSED=2;
        public static final int CANCELED=3;

        private DownloadListener listener;

        private boolean isCanceled=false;
        private boolean isPaused=false;

        private int lastProgress;

        public DownloadTask (DownloadListener listener)
        {
            this.listener = listener;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            InputStream inputStream = null;
            RandomAccessFile randomAccessFile = null;
            File file = null;
            try {
                // 首先获取已经下载的长度，方便断点下载
                String downloadURL = strings[0];
                long downloadLength = 0;
                String fileName = downloadURL.substring(downloadURL.lastIndexOf("/"));
                String directory = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();
                file = new File(directory+fileName);
                // 如果文件存在，就获取文件长度，作为已经下载的长度
                if (file.exists())
                {
                    downloadLength = file.length();
                }
                // 获取需要下载的文件长度
                long contentLength = getContentLength(downloadURL);
                // 判断下载情况，如果内容长度和文件长度已经相等，则下载完成
                if (contentLength == downloadLength)
                {
                    return SUCCESS;
                }
                if (contentLength == 0)
                {
                    return FAILED;
                }

                //开始断点下载
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();
                Request request = new Request.Builder()
                        .addHeader("RANGE", "bytes="+downloadLength+"-")
                        .url(downloadURL)
                        .build();
                // 这里是同步请求，不知道能不能异步请求
                Response response = client.newCall(request).execute();
                if (response.body() != null)
                {
                    inputStream = response.body().byteStream();
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    // 跳过已经下载过的部分
                    randomAccessFile.seek(downloadLength);
                    byte[] b = new byte[1024];
                    int len;
                    int total = 0;
                    while ((len=inputStream.read(b)) != -1)
                    {
                        if (isCanceled)
                        {
                            return CANCELED;
                        }
                        if (isPaused)
                        {
                            return PAUSED;
                        }
                        total += len;
                        randomAccessFile.write(b, 0, len);
                        int progress = (int) ((downloadLength + total)/contentLength);
                        publishProgress(progress);
                    }
                    response.body().close();
                    return SUCCESS;
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                try {
                    if (inputStream != null)
                    {
                        inputStream.close();
                    }
                    if (randomAccessFile != null)
                    {
                        randomAccessFile.close();
                    }
                    if (isCanceled && file!=null)
                    {
                        file.delete();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            return FAILED;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int progress = values[0];
            if (progress > lastProgress)
            {
                listener.onProgress(progress);
                lastProgress = progress;
            }
        }

        @Override
        protected void onPostExecute(Integer status) {
            super.onPostExecute(status);
            switch (status)
            {
                case SUCCESS:
                    listener.onSuccess();
                    break;
                case FAILED:
                    listener.onFailed();
                    break;
                case PAUSED:
                    listener.onPaused();
                    break;
                case CANCELED:
                    listener.onCanceled();
                    break;
                default:
                    break;
            }
        }

        public void pauseDownload()
        {
            isPaused = true;
        }

        public void cancelDownload()
        {
            isCanceled = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment);
    }


    // 向服务器发送请求查看下载文件的内容长度
    private long getContentLength(String downloadURL) throws IOException
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(downloadURL)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful())
        {
            return response.body().contentLength();
        }
        return 0;
    }
}