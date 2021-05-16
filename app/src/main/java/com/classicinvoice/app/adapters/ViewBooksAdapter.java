package com.classicinvoice.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.classicinvoice.app.MainActivity;
import com.classicinvoice.app.R;
import com.classicinvoice.app.classes.SessionManager;
import com.classicinvoice.app.models.getBooks.GetBooks;
import com.classicinvoice.app.models.getBooks.Values;
import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;

import java.util.ArrayList;


public class ViewBooksAdapter extends RecyclerView.Adapter<ViewBooksAdapter.MyViewHolder> {

    ArrayList<ArrayList<String>> itemData;

    FragmentActivity act;

    SessionManager msessionManager;

    OnItemClickListener onItemClickListener;

    public ViewBooksAdapter(FragmentActivity act, ArrayList<ArrayList<String>> itemsData, OnItemClickListener listener) {

        this.act = act;

        this.itemData = itemsData;

        msessionManager = new SessionManager(act);

        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                          int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_books_row, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.bind(onItemClickListener, position);

        if (itemData != null) {

            holder.tv_bookName.setText(itemData.get(position).get(1).trim());
            holder.tv_availability.setText(itemData.get(position).get(2).trim());
        }

    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.linear_parent)
        LinearLayout linear_parent;

        @BindView(R.id.tv_bookName)
        TextView tv_bookName;

        @BindView(R.id.tv_availability)
        TextView tv_availability;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnifeLite.bind(this, itemView);

            //pass adaptors' main layout id
            MainActivity.setTextFonts(linear_parent);

        }

        public void bind(final OnItemClickListener listener, final int position) {

            tv_availability.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(position);

                }

            });
        }
    }

    public interface OnItemClickListener {

        public void onItemClick(int position);
    }

}