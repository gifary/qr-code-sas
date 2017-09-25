package hospitality.sas.gifary.absenkaryawan.net;

import android.support.annotation.NonNull;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

/**
 * Created by gifary on 25/09/17.
 */

public class RestResponseHandler extends JsonHttpResponseHandler {

    private String tag;

    public RestResponseHandler(@NonNull String tag) {
        this.tag = tag;
    }

    public static JSONObject getData(JSONObject response) throws JSONException {
        return response.getJSONObject("data");
    }

    public static boolean isSuccess(JSONObject response) throws JSONException {
        Log.d("isuscces",response.getString("code"));
        return response.getString("code").equals("200");
    }

    public static boolean isSuccessUpdateToken(JSONObject response) throws JSONException {
        Log.d("isuscces",response.getJSONObject("meta").getString("code"));
        return response.getJSONObject("meta").getString("code").equals("202");
    }

    public static boolean isSuccessPostToken(JSONObject response) throws JSONException {
        Log.d("isuscces",response.getJSONObject("meta").getString("code"));
        return response.getJSONObject("meta").getString("code").equals("201");
    }

    public static boolean isSuccessDeleteToken(JSONObject response) throws JSONException {
        Log.d("isuscces",response.getJSONObject("meta").getString("code"));
        return response.getJSONObject("meta").getString("code").equals("204");
    }

    public void onFailure(String message, Throwable throwable) {
        Log.e(tag + ".Rest", message, throwable);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                          JSONObject errorResponse) {
        if (errorResponse != null) {
            onFailure(errorResponse.toString(), throwable);
        } else {
            onFailure(throwable.getMessage(), throwable);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                          JSONArray errorResponse) {
        if (errorResponse != null) {
            onFailure(errorResponse.toString(), throwable);
        } else {
            onFailure(throwable.getMessage(), throwable);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString,
                          Throwable throwable) {
        if (!TextUtils.isEmpty(responseString)) {
            onFailure(responseString, throwable);
        } else {
            onFailure(throwable.getMessage(), throwable);
        }
    }
}
