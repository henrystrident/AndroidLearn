package com.example.uiwidgettest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FruitAdapter extends ArrayAdapter<Fruit>
{
    private int resourceId;
    FruitAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Fruit> objects)
    {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
    }

    public static class ViewHolder
    {
        TextView name;
        TextView price;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Fruit fruit = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(this.resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = view.findViewById(R.id.fruitName);
            viewHolder.price = view.findViewById(R.id.fruitPrice);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }


        assert fruit != null;
        viewHolder.name.setText(fruit.getName());
        viewHolder.price.setText(String.valueOf(fruit.getPrice()));
        return view;

    }
}
