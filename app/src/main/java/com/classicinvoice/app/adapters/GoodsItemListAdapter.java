

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

import com.classicinvoice.app.MainActivity;
import com.classicinvoice.app.R;
import com.classicinvoice.app.models.GoodsItem;
import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GoodsItemListAdapter extends RecyclerView.Adapter<GoodsItemListAdapter.MyViewHolder> {

    ArrayList<GoodsItem> itemData;
    FragmentActivity act;
    OnItemClickListener onItemClickListener;


    public GoodsItemListAdapter(FragmentActivity a, ArrayList<GoodsItem> data, OnItemClickListener onItemClickListener) {
        this.itemData = data;
        this.act = a;
        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goods_row, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.bind(onItemClickListener, position);

        // for fetching images and title
        if (itemData.get(position) != null) {

            holder.tv_srNo.setText(itemData.get(position).getSrNo()+"");

            holder.tv_goodsItem.setText(itemData.get(position).getDescription());

            holder.tv_goodsQty.setText(act.getString(R.string.Quantity) + ": "
                    + itemData.get(position).getQuantity());

            holder.tv_goodsRate.setText(act.getString(R.string.RateLabel) + ": "
                    + itemData.get(position).getRate());
        }
    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.linear_goods_parent)
        LinearLayout linear_goods_parent;

        @BindView(R.id.tv_srNo)
        TextView tv_srNo;

        @BindView(R.id.tv_goodsItem)
        TextView tv_goodsItem;

        @BindView(R.id.tv_goodsQty)
        TextView tv_goodsQty;

        @BindView(R.id.tv_goodsRate)
        TextView tv_goodsRate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnifeLite.bind(this, itemView);

            //pass adaptors' main layout id
            MainActivity.setTextFonts(linear_goods_parent);

        }


        public void bind(final OnItemClickListener listener, final int position) {

            linear_goods_parent.setOnClickListener(new View.OnClickListener() {
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