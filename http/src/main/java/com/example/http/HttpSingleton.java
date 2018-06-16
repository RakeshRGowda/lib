package com.example.http;


import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/*
USER        Date            Version             Changes
Rakesh      13-06-2018      Initial Draft       No changes.
*/


public class HttpSingleton  {


    /*TAG for logcat*/
    public static final String TAG = HttpSingleton.class.getSimpleName();
    /*Instance request queue*/
    private RequestQueue mRequestQueue;
    private static HttpSingleton instance;
    private Context context;


    public HttpSingleton(Context context) {
        this.context = context;
    }

    public static synchronized HttpSingleton getInstance(Context context){
        // If Instance is null then initialize new Instance
        if(instance == null){
            instance = new HttpSingleton(context.getApplicationContext());
        }
        // Return MySingleton new Instance
        return instance;
    }





    /*
   MethodId : getRequestQueue
   Input: Nothing
   Output: RequestQueue
   scope: project
   Description: Instance of request queue
   Version: 1.0
   */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }

    /*
   MethodId : addToRequestQueue
   Input: Request, String
   Output: Nothing
   scope: project
   Description: Adding http request to the queue
   Version: 1.0
   */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }


    /*
   MethodId : addToRequestQueue
   Input: Request
   Output: Nothing
   scope: project
   Description: Adding http request to the queue
   Version: 1.0
   */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


    /*
   MethodId : cancelPendingRequests
   Input: Object
   Output: Nothing
   scope: project
   Description: cancelling http request from the queue
   Version: 1.0
   */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
