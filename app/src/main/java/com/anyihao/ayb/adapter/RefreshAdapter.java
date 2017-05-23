package com.anyihao.ayb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.anyihao.ayb.R;

import java.util.List;

/**
 * author: Administrator
 * date: 2017/2/22 002217:07.
 * email:looper@126.com
 */

public class RefreshAdapter extends UltimateAdapter<String> {


    public RefreshAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(UltimateViewHolder holder, String s) {
        if (holder.getLayoutPosition() < getItemCount() && (customHeaderView != null ? holder
                .getLayoutPosition() <= mDatas.size() : holder.getLayoutPosition() < mDatas.size
                ()) && (customHeaderView != null ? holder.getLayoutPosition() > 0 : true)) {
            holder.setText(R.id.textview, mDatas.get(customHeaderView != null ? holder
                    .getLayoutPosition() - 1 : holder.getLayoutPosition()));
            // ((ViewHolder) holder).itemView.setActivated(selectedItems.get(position, false));
            if (mDragStartListener != null) {
//                ((ViewHolder) holder).imageViewSample.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
//                            mDragStartListener.onStartDrag(holder);
//                        }
//                        return false;
//                    }
//                });

                holder.getView(R.id.itemview).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
            }
        }
    }

    @Override
    public UltimateViewHolder newFooterHolder(View view) {
        return new UltimateViewHolder(view);
    }

    @Override
    public UltimateViewHolder newHeaderHolder(View view) {
        return new UltimateViewHolder(view);
    }

    @Override
    public long generateHeaderId(int position) {
        if (getItem(position).length() > 0)
            return getItem(position).charAt(0);
        else return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stick_header_item, parent, false);
        return new UltimateViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
//        TextView textView = (TextView) holder.itemView.findViewById(R.id.stick_text);
//        textView.setText(String.valueOf(getItem(position).charAt(0)));
////        viewHolder.itemView.setBackgroundColor(Color.parseColor("#AA70DB93"));
//        holder.itemView.setBackgroundColor(Color.parseColor("#AAffffff"));
//        ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.stick_img);
//        SecureRandom imgGen = new SecureRandom();
//        switch (imgGen.nextInt(3)) {
//            case 0:
//                imageView.setImageResource(R.drawable.jr1);
//                break;
//            case 1:
//                imageView.setImageResource(R.drawable.jr2);
//                break;
//            case 2:
//                imageView.setImageResource(R.drawable.jr3);
//                break;
//        }
    }

    public void insert(String string, int position) {
        insertInternal(mDatas, string, position);
    }

    public void remove(int position) {
        removeInternal(mDatas, position);
    }

    public void clear() {
        clearInternal(mDatas);
    }


    public void swapPositions(int from, int to) {
        swapPositions(mDatas, from, to);
    }

    @Override
    public void onItemDismiss(int position) {
        if (position > 0) {
            remove(position);
            // notifyItemRemoved(position);
//        notifyDataSetChanged();
            super.onItemDismiss(position);
        }
    }

    public void setOnDragStartListener(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition > 0 && toPosition > 0) {
            swapPositions(fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
            super.onItemMove(fromPosition, toPosition);
        }
    }

    public String getItem(int position) {
        if (customHeaderView != null)
            position--;
        // URLogs.d("position----"+position);
        if (position >= 0 && position < mDatas.size())
            return mDatas.get(position);
        else return "";
    }
}
