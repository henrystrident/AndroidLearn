package com.example.uiwidgettest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FruitRecyclerViewAdapter extends RecyclerView.Adapter<FruitRecyclerViewAdapter.ViewHolder> {
    private List<Fruit> FruitList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View fruitView;
        TextView name;
        TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fruitView = itemView;
            name = itemView.findViewById(R.id.fruitNameHorizontal);
            price = itemView.findViewById(R.id.fruitPriceHorizontal);
        }
    }

    FruitRecyclerViewAdapter(List<Fruit> fruitList)
    {
        FruitList = fruitList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item_horizontal, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.fruitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Fruit fruit = FruitList.get(position);
                Toast.makeText(v.getContext(), fruit.getName()+" "+fruit.getPrice(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Fruit fruit = FruitList.get(position);
        holder.name.setText(fruit.getName());
        holder.price.setText(String.valueOf(fruit.getPrice()));
    }

    @Override
    public int getItemCount() {
        return this.FruitList.size();
    }
}
