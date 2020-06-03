package edu.uw.main.io;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import androidx.collection.LruCache;

/**
 * This class will generate and manage a request queue of instances.
 * @author Group 3
 * @version 6/2
 */
public class RequestQueueSingleton {
    private static RequestQueueSingleton instance;
    private static Context context;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    /**
     * This method will load up a new image
     * @param context context of data
     */
    private RequestQueueSingleton(Context context) {
        RequestQueueSingleton.context = context;
        mRequestQueue = getmRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    /**
     * This method will request a new singleton if the instance is null.
     * @param context context of data
     * @return instance with a ndw singleton.
     */
    public static synchronized RequestQueueSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new RequestQueueSingleton(context);
        }
        return instance;
    }

    /**
     * This is a getter for a request queue.
     * @return a new request queue.
     */
    public RequestQueue getmRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Adds a request to the request queue
     * @param req new request
     * @param <T> Generic
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getmRequestQueue().add(req);
    }

    /**
     * A getter for an image loader
     * @return an image loader.
     */
    public ImageLoader getmImageLoader() {
        return mImageLoader;
    }
}
