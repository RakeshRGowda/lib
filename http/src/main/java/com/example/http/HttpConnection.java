package com.example.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


/*
USER        Date            Version             Changes
Rakesh      13-06-2018     Initial Draft       No changes.
*/


/**
 * The type Http connection.
 */
public class HttpConnection {

    private String sUrlResult = "";
    private Context mContext;
    private static HttpConnection httpInstance;
    private int iHTTPStatus;
    private String sStatusMsg;
    private static final String TAG = HttpConnection.class.getSimpleName();
    private static int ERROR_DUE_TO_EXCEPTION = 0;
    private static int SUCCESS = 1;
    private static int VOLLEY_ERROR = 2;
    private static final String PROGRESS_DIALOG_ERROR_MSG = "Error occured before HTTP connection";
    private static String VOLLEY_SUCCESS_MSG = "Data received from server";
    private String TIME_OUT_ERROR = "Time out error on connection";
    private String NETWORK_ERROR = "Network error on connection";
    private String NO_CONNECTION_ERROR = "Connection error while connecting";
    private String AUTH_FAILUTE_ERROR = "Authentication failure error";
    private String SERVER_ERROR = "Server error while connecting";
    private String PARSE_ERROR = "Parse error while connecting";
    private static final int DEFAULT_RETRY = 2;
    private static final int iMaxRetries = 5;
    /*HttpTaskCompleteListener interface*/
    private HttpTaskCompleteListener<String> httpTaskCompleteListener;


    /**
     * Sets retries to default. <br>
     *
     * @return the retries to default
     */
    /*
    MethodId : setRetriesToDefault
    Input: Nothing
    Output: int
    scope: project
    Description: To set http retries to default
    Version: 1.0
    */
    public int setRetriesToDefault() {
        return DEFAULT_RETRY;
    }


    /**
     * Sets to max retries. <br>
     *
     * @return the to max retries
     */
    /*
    MethodId : setToMaxRetries
    Input: Nothing
    Output: int
    scope: project
    Description: To set http retries to max, i.e 5
    Version: 1.0
    */
    public int setToMaxRetries() {
        return iMaxRetries;
    }


    /**
     * Gets http status. <br>
     *
     * @return the http status
     */
    /*
    MethodId : getiHTTPStatus
    Input: Nothing
    Output: int
    scope: project
    Description: To get http status. 0 = ProgressDialog Error status, 1= volley success status, 2 = volley error status
    Version: 1.0
    */
    public int getiHTTPStatus() {
        return iHTTPStatus;
    }


    /**
     * Sets http status. <br>
     *
     * @param iHTTPStatus the http status
     */
    /*
    MethodId : setiHTTPStatus
    Input: int
    Output: Nothing
    scope: project
    Description: To set http status. 0 = ProgressDialog Error status, 1= volley success status, 2 = volley error status
    Version: 1.0
    */
    public void setiHTTPStatus(int iHTTPStatus) {
        this.iHTTPStatus = iHTTPStatus;
    }

    /**
     * Gets status msg. <br>
     *
     * @return the status msg
     */
    /*
    MethodId : getsStatusMsg
    Input: Nothing
    Output: String
    scope: project
    Description: To get brief detail of http status mesage
    Version: 1.0
    */
    public String getsStatusMsg() {
        return sStatusMsg;
    }


    /**
     * Sets status msg. <br>
     *
     * @param sStatusMsg the s status msg
     */
    /*
    MethodId : setsStatusMsg
    Input: string
    Output: Nothing
    scope: project
    Description: To set brief detail of http status mesage
    Version: 1.0
    */
    public void setsStatusMsg(String sStatusMsg) {
        this.sStatusMsg = sStatusMsg;
    }

    /**
     * Gets instance. <br>
     *
     * @param context                  the context
     * @param httpTaskCompleteListener the http task complete listener
     * @return the instance
     */
    /*
    MethodId : getInstance
    Input: context,HttpTaskCompleteListener
    Output: HttpConnection instance
    scope: project
    Description: To get instance of the class
    Version: 1.0
    */
    public static HttpConnection getInstance(Context context, HttpTaskCompleteListener<String> httpTaskCompleteListener) {
        if (httpInstance == null) {
            httpInstance = new HttpConnection(context, httpTaskCompleteListener);
        }
        return httpInstance;
    }


    private HttpConnection(Context mContext, HttpTaskCompleteListener<String> httpTaskCompleteListener) {
        this.mContext = mContext;
        this.httpTaskCompleteListener = httpTaskCompleteListener;
    }


    /**
     * Http get. <br>
     *
     * @param sUrl                 the s url
     * @param timeOutInMilliSecond the time out in milli second
     * @param retries              the retries
     */
    /*
    MethodId : httpGet
    Input: string,int,int
    Output: Nothing
    scope: project
    Description: fetching server data using GET method
    Version: 1.0
    */
    public synchronized void httpGet(final String sUrl, final int timeOutInMilliSecond, final int retries) {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        try {
            pDialog.setMessage("Initializing...");
            pDialog.show();
        } catch (Exception e) {
            Log.e(TAG, e.toString() + "," + e.getMessage());
            setiHTTPStatus(ERROR_DUE_TO_EXCEPTION);
            setsStatusMsg(PROGRESS_DIALOG_ERROR_MSG);
        }

        StringRequest strReq = new StringRequest(Request.Method.GET,
                sUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                pDialog.dismiss();
                sUrlResult = response;
                setiHTTPStatus(SUCCESS);
                setsStatusMsg(VOLLEY_SUCCESS_MSG);
                httpTaskCompleteListener.onSuccess(sUrlResult);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.dismiss();

                if (error instanceof TimeoutError) {
                    sUrlResult = TIME_OUT_ERROR;
                } else if (error instanceof NoConnectionError) {
                    sUrlResult = NO_CONNECTION_ERROR;
                } else if (error instanceof AuthFailureError) {
                    sUrlResult = AUTH_FAILUTE_ERROR;
                } else if (error instanceof ServerError) {
                    sUrlResult = SERVER_ERROR;
                } else if (error instanceof NetworkError) {
                    sUrlResult = NETWORK_ERROR;
                } else if (error instanceof ParseError) {
                    sUrlResult = PARSE_ERROR;
                }
                setiHTTPStatus(ERROR_DUE_TO_EXCEPTION);
                setsStatusMsg(error.getMessage());
                httpTaskCompleteListener.onFailure(sUrlResult);
            }
        });

        strReq.setRetryPolicy(new DefaultRetryPolicy(timeOutInMilliSecond, retries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        HttpSingleton.getInstance(mContext).addToRequestQueue(strReq);
    }


    /**
     * Http post.  <br>
     *
     * @param url      the url
     * @param keyValue the key value
     */
    /*
    MethodId : httpPost
    Input: string,hashmap
    Output: Nothing
    scope: project
    Description: fetching server data using POST method
    Version: 1.0
    */
    public synchronized void httpPost(final String url, final HashMap<String, String> keyValue) {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        try {
            pDialog.setMessage("Initializing...");
            pDialog.show();
        } catch (Exception e) {
            Log.e(TAG, e.toString() + "," + e.getMessage());
            setiHTTPStatus(ERROR_DUE_TO_EXCEPTION);
            setsStatusMsg(PROGRESS_DIALOG_ERROR_MSG);
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                pDialog.hide();
                sUrlResult = response;
                setiHTTPStatus(SUCCESS);
                setsStatusMsg(VOLLEY_SUCCESS_MSG);
                httpTaskCompleteListener.onSuccess(sUrlResult);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
                if (error instanceof TimeoutError) {
                    sUrlResult = TIME_OUT_ERROR;
                } else if (error instanceof NoConnectionError) {
                    sUrlResult = NO_CONNECTION_ERROR;
                } else if (error instanceof AuthFailureError) {
                    sUrlResult = AUTH_FAILUTE_ERROR;
                } else if (error instanceof ServerError) {
                    sUrlResult = SERVER_ERROR;
                } else if (error instanceof NetworkError) {
                    sUrlResult = NETWORK_ERROR;
                } else if (error instanceof ParseError) {
                    sUrlResult = PARSE_ERROR;
                }
                setiHTTPStatus(ERROR_DUE_TO_EXCEPTION);
                setsStatusMsg(error.getMessage());
                httpTaskCompleteListener.onFailure(sUrlResult);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        // Adding request to request queue
        HttpSingleton.getInstance(mContext).addToRequestQueue(strReq);
    }

}
