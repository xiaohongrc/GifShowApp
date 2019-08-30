//package com.hongenit.gifshowapp.util.imageloader.glide;
//
//import android.util.Log;
//import com.bumptech.glide.Priority;
//import com.bumptech.glide.load.DataSource;
//import com.bumptech.glide.load.HttpException;
//import com.bumptech.glide.load.data.DataFetcher;
//import com.bumptech.glide.load.model.GlideUrl;
//import com.bumptech.glide.util.ContentLengthInputStream;
//import okhttp3.*;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Iterator;
//import java.util.Map;
//
///**
// * Created by Xiaohong on 2019-08-26.
// * desc:
// */
//public class OkHttpStreamFetcher implements DataFetcher<InputStream> {
//    private static final String TAG = "OkHttpFetcher";
//    private final Call.Factory client;
//    private final GlideUrl url;
//    InputStream stream;
//    ResponseBody responseBody;
//    private volatile Call call;
//
//    public OkHttpStreamFetcher(Call.Factory client, GlideUrl url) {
//        this.client = client;
//        this.url = url;
//    }
//
//    public void loadData(Priority priority, final DataCallback<? super InputStream> callback) {
////        Request.Builder requestBuilder = (new Request.Builder()).url(this.url.toStringUrl());
////        Iterator request = this.url.getHeaders().entrySet().iterator();
////
////        while (request.hasNext()) {
////            Map.Entry headerEntry = (Map.Entry) request.next();
////            String key = (String) headerEntry.getKey();
////            requestBuilder.addHeader(key, (String) headerEntry.getValue());
////        }
////
////        Request request1 = requestBuilder.build();
////        this.call = this.client.newCall(request1);
////        this.call.enqueue(new Callback() {
////            public void onFailure(Call call, IOException e) {
////                Log.d("OkHttpFetcher", "OkHttp failed to obtain result", e);
////
////                callback.onLoadFailed(e);
////            }
////
////            public void onResponse(Call call, Response response) {
////                responseBody = response.body();
////                if (response.isSuccessful()) {
////                    long contentLength = responseBody.contentLength();
////                    stream = ContentLengthInputStream.obtain(responseBody.byteStream(), contentLength);
////                    callback.onDataReady(stream);
////                } else {
////                    callback.onLoadFailed(new HttpException(response.message(), response.code()));
////                }
////
////            }
////        });
//
//        Request request = new Request.Builder().url(url.toString()).build();
//        OkHttpClient client = new OkHttpClient();
//        client.interceptors().add(new ProgressInterceptor());
//
//        try {
//            progressCall = client.newCall(request);
//            Response response = progressCall.execute();
//            if (isCancelled) {
//                return ;
//            }
//
//            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//            stream = response.body().byteStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ;
//        }
//        return ;
//
//    }
//
//    public void cleanup() {
//        try {
//            if (this.stream != null) {
//                this.stream.close();
//            }
//        } catch (IOException var2) {
//        }
//
//        if (this.responseBody != null) {
//            this.responseBody.close();
//        }
//
//    }
//
//    public void cancel() {
//        Call local = this.call;
//        if (local != null) {
//            local.cancel();
//        }
//
//    }
//
//    public Class<InputStream> getDataClass() {
//        return InputStream.class;
//    }
//
//    public DataSource getDataSource() {
//        return DataSource.REMOTE;
//    }
//}