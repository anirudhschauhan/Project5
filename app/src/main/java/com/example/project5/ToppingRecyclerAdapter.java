package com.example.project5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * class that adapts the topping recycler to android
 * @author Anirudh Chauhan, Matthew Calora
 */

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


    /**
     * retruns the topiing view holder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ToppingViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.toppingsrecycler, parent, false);
        ToppingViewHolder tvh = new ToppingViewHolder(v,mListener);
        return tvh;
    }

    /**
     * binds item and text
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ToppingViewHolder holder, int position) {
        ToppingRecycler currentItem =  mToppingList.get(position);
        holder.mTextView.setText(currentItem.getText());
    }

    /**
     * returns the number of toppings on the pizza
     * @return
     */
    @Override
    public int getItemCount() {
        return mToppingList.size();
    }
}
