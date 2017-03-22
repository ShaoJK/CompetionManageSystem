package com.wzdx.competionmanagesystem.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wzdx.competionmanagesystem.JavaBean.DepartmentList;
import com.wzdx.competionmanagesystem.R;
import com.wzdx.competionmanagesystem.Utils.BaseViewHolder;

/**
 * Created by sjk on 2017/1/24.
 * 专业的ListView的Adapter
 */

public class DepartmentAdapter extends BaseAdapter {

    private Context mContext;
    private DepartmentList mList;

    public DepartmentAdapter(DepartmentList mList, Context context) {
        this.mList = mList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return mList.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.view_item_choose_teacher, null);
        }
        TextView tv_content = BaseViewHolder.get(convertView, R.id.tv_content);
        tv_content.setText(mList.getData().get(position).getD_name());
        return convertView;
    }
}
