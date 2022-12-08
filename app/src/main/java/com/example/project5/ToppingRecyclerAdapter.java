package com.example.project5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ToppingRecyclerAdapter extends RecyclerView.Adapter<ToppingRecyclerAdapter.ToppingViewHolder> {
    private ArrayList<ToppingRecycler> mToppingList;
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void OnItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public static class ToppingViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ToppingViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!= null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.OnItemClick(position);
                        }
                    }
                }
            });
        }

    }
    public ToppingRecyclerAdapter(ArrayList<ToppingRecycler> toppingList){
        mToppingList = toppingList;
    }


    @Override
    public ToppingViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.toppingsrecycler, parent, false);
        ToppingViewHolder tvh = new ToppingViewHolder(v,mListener);
        return tvh;
    }

    @Override
    public void onBindViewHolder(ToppingViewHolder holder, int position) {
        ToppingRecycler currentItem =  mToppingList.get(position);
        holder.mTextView.setText(currentItem.getText());
    }

    @Override
    public int getItemCount() {
        return mToppingList.size();
    }
}
