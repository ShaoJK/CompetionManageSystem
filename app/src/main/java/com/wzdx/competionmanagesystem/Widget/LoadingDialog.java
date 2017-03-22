/*
 * Copyright (C) 2009 Teleca Poland Sp. z o.o. <android@teleca.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wzdx.competionmanagesystem.Widget;

import android.app.Activity;
import android.app.ProgressDialog;

import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Wrapper around UserTask & ProgressDialog
 *
 * @author Lukasz Wisniewski
 */
public abstract class LoadingDialog<Input,Progress,Result> extends AsyncTask<Input, Progress, Result> {

    private ProgressDialog mProgressDialog;
    protected Activity mActivity;
    private int mLoadingMsg;
    private int mFailMsg;

    public LoadingDialog(Activity activity, int loadingMsg, int failMsg) {
        this.mActivity = activity;
        this.mLoadingMsg = loadingMsg;
        this.mFailMsg = failMsg;
    }

    @Override
    public void onCancelled() {
        failMsg();
        super.onCancelled();
    }

    @Override
    public void onPreExecute() {
        mProgressDialog = new ProgressDialog(mActivity);

        mProgressDialog.setMessage(mActivity.getResources().getString(mLoadingMsg));
        mProgressDialog.setCancelable(false);
       // mProgressDialog.show();
        super.onPreExecute();
    }

    @Override
    public abstract Result doInBackground(Input... params);

    @Override
    public void onPostExecute(Result result) {
        super.onPostExecute(result);
        mProgressDialog.dismiss();
        if (result != null) {
            doStuffWithResult(result);
        } else {
            failMsg();
        }

    }

    protected void failMsg() {
        Toast.makeText(mActivity, mFailMsg, Toast.LENGTH_LONG).show();
    }

    /**
     * Very abstract function hopefully very meaningful name, executed when
     * result is other than null
     *
     * @param result
     * @return
     */
    public abstract void doStuffWithResult(Result result);

    @Override
    protected void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
    }

}
