package com.bjfu.fungus.MultiSelect;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjfu.fungus.R;


public class SearchPopupWindowsAdapter extends CommonBaseAdapter<Search> {

    public SearchPopupWindowsAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.multi_item_list_selector, viewGroup, false);
        if (view != null) {
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout_search);
            TextView textView = (TextView) view.findViewById(R.id.textView_listView_selector);
            final ImageView imageView = (ImageView) view.findViewById(R.id.image_search_check);
            imageView.setImageResource(itemList.get(i).isChecked() ? R.drawable.ic_check_circle : R.drawable.ic_circle);
            textView.setText(itemList.get(i).getKeyWord());
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemList.get(i).setChecked(itemList.get(i).isChecked() ? false : true);
                    imageView.setImageResource(itemList.get(i).isChecked() ? R.drawable.ic_check_circle :R.drawable.ic_circle);
                }
            });
        }
        return view;
    }

}
