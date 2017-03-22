package com.wzdx.competionmanagesystem.Utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.internal.http.multipart.Part;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wzdx.competionmanagesystem.App.UICommon;
import com.wzdx.competionmanagesystem.Widget.MultipartRequest;
import com.wzdx.competionmanagesystem.Widget.StringWithCookiesRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by sjk on 2016/12/25.
 */

public abstract class MyHttpRequest {
    private Context mContext;
    private String mUrl;
    private Map<String, String> mParams;
    private ProgressDialog mDialog;
    public static final String ROOT_URL = "http://10.0.2.2/CompetionManageSystem/index.php/";

    protected MyHttpRequest(Context context, String url, Map<String, String> params) {
        mContext = context;
        mUrl = ROOT_URL + url;
        mParams = params;
        mDialog = new ProgressDialog(mContext);
    }

    public abstract void sucessListener(String result);

    //public abstract void errorListener();

    /**
     * 一般的Post请求
     */
    public void startRequestByPost() {
        showDialog();
        RequestQueue queue = UICommon.INSTANCE.getRequestQueue(mContext.getApplicationContext());
        StringWithCookiesRequest request = new StringWithCookiesRequest(Request.Method.POST, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                mDialog.dismiss();
                System.out.println("result------->" + s);
                if (s != null && (!s.isEmpty())) {
                    try {
                        sucessListener(s);
                    } catch (Exception e) {
                        TispToastFactory.showTip("系统异常");
                        e.printStackTrace();
                    }
                } else {
                    TispToastFactory.getToast(mContext, "网络异常", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mDialog.dismiss();
                System.out.println("Error------>" + volleyError.getStackTrace());
                //  errorListener();
                TispToastFactory.getToast(mContext, "网络异常", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return mParams;
            }
        };
        queue.add(request);
    }

    /**
     * 带文件的POST请求
     *
     * @param params
     */
    public void startRequestByPostWithFile(List<Part> params) {
        showDialog();
        RequestQueue queue = UICommon.INSTANCE.getRequestQueue(mContext.getApplicationContext());
        //生成请求
        MultipartRequest request = new MultipartRequest(mUrl, params.toArray(new Part[params.size()]), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                mDialog.dismiss();
                System.out.println("result------->" + s);
                if (s != null && (!s.isEmpty())) {
                    try {
                        sucessListener(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                        TispToastFactory.showTip("系统异常");
                    }
                } else {
                    TispToastFactory.getToast(mContext, "网络异常", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mDialog.dismiss();
                System.out.println("Error------>" + volleyError.getStackTrace());
                //  errorListener();
                TispToastFactory.getToast(mContext, "网络异常", Toast.LENGTH_LONG).show();
            }
        });
        //将请求加入队列
        queue.add(request);
    }

    protected void showDialog() {

        mDialog.setMessage("数据加载中...");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    /**
     * Get请求
     */
    public void startRequestByGet() {
        showDialog();
        RequestQueue queue = UICommon.INSTANCE.getRequestQueue(mContext);
        System.out.println("mUrl---------->" + mUrl);
        StringWithCookiesRequest request = new StringWithCookiesRequest(mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                mDialog.dismiss();
                System.out.println("result------->" + s);
                if (s != null && (!s.isEmpty())) {
                    try {
                        sucessListener(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                        TispToastFactory.showTip("系统异常");
                    }
                } else {
                    TispToastFactory.getToast(mContext, "网络异常", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mDialog.dismiss();
                System.out.println("Error------>" + volleyError.getStackTrace());
                //  errorListener();
                TispToastFactory.getToast(mContext, "网络异常", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }
}
