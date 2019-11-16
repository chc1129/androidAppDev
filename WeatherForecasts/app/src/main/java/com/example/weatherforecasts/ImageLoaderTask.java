package com.example.weatherforecasts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoaderTask
        extends AsyncTask<ImageLoaderTask.Request,
        Void, ImageLoaderTask.Result> {

    public static class Request {
        public final ImageView imageView;
        public final String url;

        public Request(ImageView imageView, String url) {
            this.imageView = imageView;
            this.url = url;
        }
    }

    public static class Result {
        public final ImageView imageView;
        public final Bitmap bitmap;
        public final Exception exception;

        public Result(ImageView imageView, Bitmap bitmap) {
            this.imageView = imageView;
            this.bitmap = bitmap;
            this.exception = null;
        }

        public Result(ImageView imageView, Exception exception) {
            this.imageView = imageView;
            this.bitmap = null;
            this.exception = exception;
        }
    }

    @Override
    protected Result doInBackground(Request... params) {
        Request request = params[0];
        Result result = null;

        HttpURLConnection connection = null;

        try {
            URL url = new URL(request.url);
            connection = (HttpURLConnection) url.openConnection();

            Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());

            result = new Result(request.imageView, bitmap);

        } catch (IOException e) {
            result = new Result(request.imageView, e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return result;
    }
    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);

        if (result.bitmap != null) {
            result.imageView.setImageBitmap(result.bitmap);
        }
    }
}