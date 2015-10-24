package com.example.lovecalendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovecalendar.R;
import com.example.lovecalendar.Utils.ToastUtils;
import com.example.lovecalendar.model.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Monkey on 2015/6/29.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View view, int position,Note note);

        void onItemLongClick(View view, int position,Note note);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public Context mContext;
    public List<Note> mDatas;
    public LayoutInflater mLayoutInflater;

    public MyRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatas=new ArrayList<>();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.normal_item, parent, false);
        return new ViewHolder(mView);
    }

    /**
     * 绑定ViewHoler，给item中的控件设置数据
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position,holder.viewNote);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position,holder.viewNote);
                    return true;
                }
            });

        }
        try {
            holder.DateTextView.setText(mDatas.get(position).getYear() + "/" + mDatas.get(position).getMonth()+"/"+mDatas.get(position).getDay());
            holder.TitleTextView.setText(mDatas.get(position).getTitle());
            holder.viewNote=mDatas.get(position);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView DateTextView;
        TextView TitleTextView;
        Note viewNote;

        public ViewHolder(View itemView) {
            super(itemView);
            DateTextView=(TextView)itemView.findViewById(R.id.normal_item_date);
            TitleTextView=(TextView)itemView.findViewById(R.id.normal_item_title);
            viewNote=new Note();
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Note note = mDatas.get(getLayoutPosition());
//                    ToastUtils.showShort(mContext,note.toString());
//                    Intent intent = new Intent(v.getContext(), WebActivity.class);
//                    intent.putExtra("Desc", gank.getDesc());
//                    intent.putExtra("Url", gank.getUrl());
//                    v.getContext().startActivity(intent);
//                }
//            });
        }
    }
}
