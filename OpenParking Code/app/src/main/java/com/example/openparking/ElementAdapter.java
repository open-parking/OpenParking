package com.example.openparking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ElementViewHolder> {


    private Context mCtx;
    private List<Element> elementList;

    public ElementAdapter(Context mCtx, List<Element> elementList) {
        this.mCtx = mCtx;
        this.elementList = elementList;
    }
    @Override
    public int getItemCount() {
        return elementList.size();
    }

    class ElementViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewTitle, textViewDesc, textViewRating, textViewPrice;
        public ElementViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);

        }
    }
    @NonNull
    @Override
    public ElementViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout, null);
        ElementViewHolder holder = new ElementViewHolder(view);
        return new ElementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ElementViewHolder elementViewHolder, int position) {
        Element element = elementList.get(position);

        elementViewHolder.textViewTitle.setText(element.getTitle());
        elementViewHolder.textViewDesc .setText(element.getShortdesc());
        elementViewHolder.textViewRating.setText(String.valueOf(element.getRating()));
        elementViewHolder.textViewPrice.setText(String.valueOf(element.getPrice()));
        elementViewHolder.imageView.setImageDrawable(mCtx.getResources().getDrawable(element.getImage()));
    }

}
