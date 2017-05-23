package com.anyihao.ayb.adapter;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.anyihao.ayb.R;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;


/**
 * Created by zJJ on 4/27/2016.
 */
public class ItemNodeViewHolder extends UltimateRecyclerviewViewHolder {
    public static final int layout = R.layout.message_item;
    public CardView cvItem;
    public TextView tvId;
    public TextView tvTaskLength;
    public TextView tvServerId;
    public TextView tvDate;

    /**
     * the view
     *
     * @param itemView the view context
     */
    public ItemNodeViewHolder(View itemView) {
        super(itemView);
        cvItem = (CardView) itemView.findViewById(R.id.card_view);
        tvId = (TextView) itemView.findViewById(R.id.tv_id);
        tvServerId = (TextView) itemView.findViewById(R.id.tv_server_id);
        tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        tvTaskLength = (TextView) itemView.findViewById(R.id.tv_task_length);
    }

    /**
     * this is the initialization of the node
     *
     * @param viewTypeLine the type of node to redraw
     */
    public void init(int viewTypeLine) {
    }
}
