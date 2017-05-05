package com.wzdx.competionmanagesystem.Widget;

import com.android.internal.http.multipart.Part;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.wzdx.competionmanagesystem.App.MyApplication;
import com.wzdx.competionmanagesystem.Utils.SPUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * 可以上传文件且携带cookies的request
 */
public class MultipartRequest extends StringRequest {
    private Part[] parts;


    public MultipartRequest(String url, Part[] parts, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);
        this.parts = parts;
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap localHashMap = new HashMap();
        //向请求头部添加Cookie
        String cookies = SPUtil.getString(
                MyApplication.getInstance().getApplicationContext(), MyApplication.COOKIES, "");
        if (!cookies.isEmpty()) {
            localHashMap.put("Cookie", cookies);
        }
        return localHashMap;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + Part.getBoundary();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Part.sendParts(baos, parts);
        } catch (IOException e) {
            VolleyLog.e(e, "error when sending parts to output!");
        }
        return baos.toByteArray();
    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            Map<String, String> responseHeaders = response.headers;
            String rawCookies = responseHeaders.get("Set-Cookie");
            //判断response中是否包含了Cookie
            if (rawCookies != null && !rawCookies.isEmpty()) {
                //保存Cookie到本地
                SPUtil.putString(MyApplication.getInstance().
                        getApplicationContext(), MyApplication.COOKIES, rawCookies);
            }
            System.out.println("cookies:"+rawCookies + "\n");
            String dataString = new String(response.data, "UTF-8");
            System.out.println( "data:"+dataString);
            return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
