package com.classicinvoice.app.adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.classicinvoice.app.MainActivity;
import com.classicinvoice.app.R;
import com.classicinvoice.app.models.DummyClass;

import java.util.ArrayList;

public class DummyAdapter extends RecyclerView.Adapter<DummyAdapter.MyViewHolder> {

    ArrayList<DummyClass> itemData;
    FragmentActivity act;
    OnItemClickListener onItemClickListener;

    public DummyAdapter(FragmentActivity a, ArrayList<DummyClass> data, OnItemClickListener onItemClickListener) {
        this.itemData = data;
        this.act = a;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.printer_row, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.bind(onItemClickListener, position);

        if (itemData.get(position) != null) {

            holder.tv_printerTitle.setText(itemData.get(position).getTitle());

            holder.tv_printerDescription.setText(itemData.get(position).getDescription());

        }
    }


    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_printerTitle)
        TextView tv_printerTitle;

        @BindView(R.id.tv_printerDescription)
        TextView tv_printerDescription;

        @BindView(R.id.relative_parent)
        RelativeLayout relative_parent;

        @BindView(R.id.iv_printerImage)
        ImageView iv_printerImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnifeLite.bind(this, itemView);

            MainActivity.setTextFonts(relative_parent);
//            tv_printerTitle.setTypeface(MainActivity.tf, Typeface.BOLD);

        }

        public void bind(final OnItemClickListener listener, final int position) {

            relative_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(position);

                }

            });

        }
    }

    public interface OnItemClickListener {

        void onItemClick(int position);

    }

}
