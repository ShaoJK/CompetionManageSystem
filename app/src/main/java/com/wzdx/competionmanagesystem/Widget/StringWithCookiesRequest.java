package com.wzdx.competionmanagesystem.Widget;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.wzdx.competionmanagesystem.App.MyApplication;
import com.wzdx.competionmanagesystem.Utils.SPUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * 携带cookies的request
 */
public class StringWithCookiesRequest extends StringRequest {


    public StringWithCookiesRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public StringWithCookiesRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap localHashMap = new HashMap();
        //向请求头部添加Cookie
        String cookies = SPUtil.getString(
                MyApplication.getInstance().getApplicationContext(), MyApplication.COOKIES, "");
        System.out.println("cookie------>" + cookies);
        if (!cookies.isEmpty()) {
            localHashMap.put("Cookie", cookies);
        }
        return localHashMap;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            Map<String, String> responseHeaders = response.headers;
            String rawCookies = responseHeaders.get("Set-Cookie");

            if (rawCookies != null && !rawCookies.isEmpty()) {
                SPUtil.putString(MyApplication.getInstance().
                        getApplicationContext(), MyApplication.COOKIES, rawCookies);//保存Cookie
            }
            System.out.println("cookies:" + rawCookies + "\n");
            String dataString = new String(response.data, "UTF-8");
            System.out.println("data:" + dataString);
            return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
