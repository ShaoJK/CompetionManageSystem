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
import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.wzdx.competionmanagesystem.R;

/**
 * 自定义的EditText带一个清空的按钮
 */
public class MyEditTextView extends FrameLayout {

    private EditText et_text;
    private ImageButton ib_close;
    private MyTextWatcher mTextWatcher;


    public MyEditTextView(Context context) {
        this(context, null);
    }

    public MyEditTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        View contentView = View.inflate(context, R.layout.view_my_edit_text, this);
        et_text = (EditText) contentView.findViewById(R.id.et_text);
        ib_close = (ImageButton) contentView.findViewById(R.id.ib_close);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MyEditTextView);

        String hint = a.getString(R.styleable.MyEditTextView_hint);
        et_text.setHint(hint);
        if (et_text.getText().toString().isEmpty()) {//如果editText中没有内容，则不显示x按钮
            ib_close.setVisibility(View.GONE);
        }
        //设置清空按钮，点击清空editText中的所有内容
        ib_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                et_text.setText("");
            }
        });

        //为editText设置文字改变监听
        et_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    ib_close.setVisibility(View.VISIBLE);
                }else{
                    ib_close.setVisibility(View.GONE);
                }

                if(mTextWatcher!=null){//执行自己的事件监听
                    mTextWatcher.afterTextChanged(s);
                }
            }

        });
    }

    public void addTextWatcher(MyTextWatcher textWatcher){
        mTextWatcher = textWatcher;
    }

    private interface MyTextWatcher{
        void afterTextChanged(Editable s);
    }

    public String getText(){
       return et_text.getText().toString();
    }

}
