package hospitality.sas.gifary.absenkaryawan.net;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;
import hospitality.sas.gifary.absenkaryawan.Constants;

/**
 * Created by gifary on 25/09/17.
 */

public class RestClient {

    public static final String API_SERVER_ADDR = "http://hrms.grandtjokro.com/api/v1/"; //wifibandungjuara.id
    public static final String ACTION_URL_LOGIN = "user/login";


    private static final String TAG = RestClient.class.getSimpleName();
    private static final String PHP_SESSION_HEADER_NAME = "Cookie";
    private static final String KEY ="Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjQxZTc1MjRhYjMwMGM4YWU1MDlhNTQ4NzRlZDkwY2Q1OGNiYjRmZTE2NTRjYmNjZmQ0OWEzZTAyMzhiMTUwM2M4ZTExZTRhNGZmZjg5OTdiIn0.eyJhdWQiOiIyNyIsImp0aSI6IjQxZTc1MjRhYjMwMGM4YWU1MDlhNTQ4NzRlZDkwY2Q1OGNiYjRmZTE2NTRjYmNjZmQ0OWEzZTAyMzhiMTUwM2M4ZTExZTRhNGZmZjg5OTdiIiwiaWF0IjoxNTAxMDMwNDgzLCJuYmYiOjE1MDEwMzA0ODMsImV4cCI6MTUzMjU2NjQ4Mywic3ViIjoiNCIsInNjb3BlcyI6W119.Bq9mQ0SvErhftX9rP8NfDRbje2bzBwCq6wO-0kysq6rRa-FChJ3-9KHu-WX7R50sCJwov1TNb4J_gLlS39aowZuPp6Aw-zU51iUQY-VB0hoLVwVlxpXLvwgQ2J6c_Sh3fEC0QEHx_mB9qwfW50Lsn-6IGFw69xvuloj_zBB4kZzibOO_UqUu1IHaSuqY2kpKxSwzJhf35oBnLkHbxapLbfqnYzxbr-f8xW711GrZfxXSb1Ik_1ZyTnTAYe5j-tWufgBHwZ2csUKdtzkrKCiEMJ9dsnVboCuAh37_jgHyRwBLtmL9aAW1M9fiU0etEmLRcw0x2v7yJ3ESb-H5cvG6ch9v1JCMClUEO0zpcHAhIkzLzUjdabKvqprJ_xidvlPE6lvSH8HnuMHb-sXU9ATSZuFOt4r8DEfd2pOdRZSGrZhhajdZuB3uCAoO_9KN75T0igNkcL2GLgzZxxt-C4FHc4L_mIBGwq1k8-KfZEuV_Pd8icErFWaoApvepl95gySHF1FXxGHBINZnUNaGADdccS33R16h8f1TJGnATakpWJnjR8rpzeYYMhk8KOZSS6B1pC3tuFQ8Yw_gNalHAiSU5xeOhmoF7dBZalfAy2Yl6r7I7pK7KIwhNmVA0N_UXJV7u5ofkCHVpsNnLq69aB-XAb-ZglpcQUKthWasyigKhj8";
    private static final String PHP_JSON_HEADER_NAME = "Accept";
    private static RestClient instance = null;
    private static PersistentCookieStore cookieStore;
    private AsyncHttpClient asyncHttpClient;
    private SyncHttpClient syncHttpClient;
    private AsyncHttpResponseHandler responseHandler;

    public static RestClient getInstance(Context context,
                                         AsyncHttpResponseHandler responseHandler) {
        if (instance == null) {
            instance = new RestClient();
            cookieStore = new PersistentCookieStore(context);

            instance.asyncHttpClient = new AsyncHttpClient();
            instance.asyncHttpClient.setCookieStore(cookieStore);

            instance.syncHttpClient = new SyncHttpClient();
            instance.syncHttpClient.setCookieStore(cookieStore);
        }

        instance.responseHandler = responseHandler;

        return instance;
    }

    public static void cancelRequests(Context context) {
        if (instance != null) {
            instance.asyncHttpClient.cancelRequests(context, true);
            instance.syncHttpClient.cancelRequests(context, true);
        }
    }

    public void addCookie(String name, String value) {
        cookieStore.addCookie(new BasicClientCookie(name, value));
    }

    private void get(String api_server_addr,boolean async, String action, RequestParams params) {
        String uri = api_server_addr + action;

        if (Constants.DEBUG) Log.d(TAG, "get request to: " + uri);

        List<Cookie> cookies = cookieStore.getCookies();
        String session = "";
        if (cookies.isEmpty()) {
            if (Constants.DEBUG) Log.d(TAG, "empty cookie");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                session += cookies.get(i).getName() + "=" + cookies.get(i).getValue() + ";";
            }

            if (Constants.DEBUG) Log.d(TAG, "session cookie: " + session);
        }

        if (!session.isEmpty()) {
            if (async) {
                asyncHttpClient.addHeader(PHP_SESSION_HEADER_NAME, session);
            } else {
                syncHttpClient.addHeader(PHP_SESSION_HEADER_NAME, session);
            }
        }

        if (async) {
            asyncHttpClient.addHeader(PHP_JSON_HEADER_NAME,"application/json");
            asyncHttpClient.addHeader("Authorization",KEY);
        } else {
            syncHttpClient.addHeader(PHP_JSON_HEADER_NAME,"application/json");
            syncHttpClient.addHeader("Authorization",KEY);
        }

        if (params == null) {
            if (Constants.DEBUG) Log.d(TAG, "no param");

            if (async) {
                asyncHttpClient.get(uri, responseHandler);
            } else {
                syncHttpClient.get(uri, responseHandler);
            }
        } else {
            if (Constants.DEBUG) Log.d(TAG, "param: " + params.toString());
            if (async) {
                asyncHttpClient.cancelAllRequests(true);
                asyncHttpClient.get(uri, params, responseHandler);
            } else {
                syncHttpClient.get(uri, params, responseHandler);
            }
        }
    }

    public void post(String api_server_addr,boolean async, String action, RequestParams params) {
        String uri = api_server_addr + action;

        if (Constants.DEBUG) Log.d(TAG, "post request to: " + uri);

        List<Cookie> cookies = cookieStore.getCookies();
        String session = "";
        if (cookies.isEmpty()) {
            Log.d(TAG, "empty cookie");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                session += cookies.get(i).getName() + "=" + cookies.get(i).getValue() + ";";
            }

            if (Constants.DEBUG) Log.d(TAG, "session: " + session);
        }

        if (!session.isEmpty()) {
            if (async) {
                asyncHttpClient.addHeader(PHP_SESSION_HEADER_NAME, session);
            } else {
                syncHttpClient.addHeader(PHP_SESSION_HEADER_NAME, session);
            }
        }

        if (async) {
            asyncHttpClient.addHeader(PHP_JSON_HEADER_NAME,"application/json");
            asyncHttpClient.addHeader("Authorization",KEY);
        } else {
            syncHttpClient.addHeader(PHP_JSON_HEADER_NAME,"application/json");
            syncHttpClient.addHeader("Authorization",KEY);
        }

        if (params == null) {
            if (Constants.DEBUG) Log.d(TAG, "no param");
            if (async) {
                asyncHttpClient.post(uri, responseHandler);
            } else {
                syncHttpClient.post(uri, responseHandler);
            }
        } else {
            if (Constants.DEBUG) Log.d(TAG, "param: " + params.toString());
            if (async) {
                asyncHttpClient.cancelAllRequests(true);
                asyncHttpClient.post(uri, params, responseHandler);
            } else {
                syncHttpClient.post(uri, params, responseHandler);
            }
        }
    }

    public void postLogin(String username, String password, boolean async) {
        RequestParams params = new RequestParams();
        params.put("email", username);
        params.put("password", password);

        post(API_SERVER_ADDR,async, ACTION_URL_LOGIN, params);

    }

}
