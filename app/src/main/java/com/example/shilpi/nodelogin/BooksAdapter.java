package com.example.shilpi.nodelogin;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 */

public class BooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<Books> data= Collections.emptyList();
    Books current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public BooksAdapter(Context context, List<Books> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_books, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        Books current=data.get(position);
        myHolder.id.setText("ID : " + current.id);
        myHolder.idedit.setText("ID : " + current.id);
        myHolder.Name.setText("Book: " + current.Name);
        myHolder.Author.setText("Author: " + current.Author);
        myHolder.Review.setText("Review: " + current.Review);
        myHolder.Buy_here.setText("Buy_here " + current.Buy_here);
        myHolder.Buy_here.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

        // load image into imageview using glide
       /* Glide.with(context).load("http://192.168.1.6/test/images/" + current.fishImage)
                .placeholder(R.drawable.ic_img_error)
                .error(R.drawable.ic_img_error)
                .into(myHolder.ivFish);*/

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{
        EditText idedit;
        TextView Name;
        ImageView ivFish;
        TextView Author;
        TextView Review;
        TextView Buy_here;
        TextView id;
        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.id);
            idedit = (EditText) itemView.findViewById(R.id.idedit);
            Name= (TextView) itemView.findViewById(R.id.Name);
            // ivFish= (ImageView) itemView.findViewById(R.id.ivFish);
            Author = (TextView) itemView.findViewById(R.id.Author);
            Review = (TextView) itemView.findViewById(R.id.Review);
            Buy_here = (TextView) itemView.findViewById(R.id.Buy_here);
        }

    }


}