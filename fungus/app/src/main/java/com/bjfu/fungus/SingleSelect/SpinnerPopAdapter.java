package com.bjfu.fungus.SingleSelect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjfu.fungus.R;

import java.util.List;

public class SpinnerPopAdapter extends BaseAdapter {
    private List<String> content;
    private Context context;
    private LayoutInflater mInflater;
    public SpinnerPopAdapter(Context context, List<String> content){
        this.context = context;
        this.content = content;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return content == null ? 0 : content.size();
    }
    @Override
    public Object getItem(int position) {
        return content.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.popuwindow_spinner_item,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_spinner = (TextView) convertView.findViewById(R.id.tv_spinner);
            viewHolder.check = (RelativeLayout) convertView.findViewById(R.id.check);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String spinnerText = content.get(position);
        viewHolder.tv_spinner.setText(spinnerText);
        return convertView;
    }
    public static class ViewHolder{
        TextView tv_spinner;
        public RelativeLayout check;
    }
}
