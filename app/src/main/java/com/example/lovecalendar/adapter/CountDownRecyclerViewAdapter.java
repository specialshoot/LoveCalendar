package com.example.lovecalendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lovecalendar.App;
import com.example.lovecalendar.R;
import com.example.lovecalendar.model.Note;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Monkey on 2015/6/29.
 */
public class CountDownRecyclerViewAdapter extends RecyclerView.Adapter<CountDownRecyclerViewAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Note note);

        void onItemLongClick(View view, int position, Note note);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public Context mContext;
    public List<Note> mDatas;
    public LayoutInflater mLayoutInflater;

    public CountDownRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatas = new ArrayList<>();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.count_down_item, parent, false);
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
                    mOnItemClickListener.onItemClick(holder.itemView, position, holder.viewNote);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position, holder.viewNote);
                    return true;
                }
            });

        }
        try {
            String dateString = "";
            int year = mDatas.get(position).getYear();
            int month = mDatas.get(position).getMonth();
            int day = mDatas.get(position).getDay();
            Calendar tempCalendar = Calendar.getInstance();
            int nowYear = tempCalendar.get(Calendar.YEAR);
            int nowMonth = tempCalendar.get(Calendar.MONTH) + 1;
            int nowDay = tempCalendar.get(Calendar.DAY_OF_MONTH);
            String nowString=nowYear + "-" + nowMonth + "-" + nowDay;
            if (mDatas.get(position).getType() == 4) { //生日
                if (nowMonth < month) {
                    dateString = nowYear + "-" + month + "-" + day;
                } else if (nowMonth == month) {
                    if (nowDay <= day) {
                        dateString = nowYear + "-" + month + "-" + day;
                    } else {
                        dateString = (nowYear + 1) + "-" + month + "-" + day;
                    }
                } else {
                    dateString = (nowYear + 1) + "-" + month + "-" + day;
                }
            } else if (mDatas.get(position).getType() == 2) {
                dateString = year + "-" + month + "-" + day; //普通倒数日
            }
            holder.TitleTextView.setText("距离" + mDatas.get(position).getTitle());
            switch (mDatas.get(position).getType()) {
                case 2: //普通倒数日
                    holder.TypeTextView.setText("REMAINING");
                    if (mDatas.get(position).getIsFisish() == 1) {
                        holder.NumberTextView.setText("Finish");
                    } else {
                        if (getDistanceDays(dateString, nowString) == 0) {
                            mDatas.get(position).setIsFisish(1);
                            App.sDb.update(mDatas.get(position), ConflictAlgorithm.Fail);
                            holder.NumberTextView.setText("Finish");
                        }else {
                            holder.NumberTextView.setText(getDistanceDays(dateString, nowString) + "");
                        }
                    }
                    break;
                case 4: //生日
                    holder.TypeTextView.setText("BIRTHDAY");
                    if(getDistanceDays(dateString, nowString)==0){
                        holder.NumberTextView.setText("Yes");
                    }else{
                        holder.NumberTextView.setText(getDistanceDays(dateString, nowString) + "");
                    }
                    break;
                default:
                    break;
            }
            holder.viewNote = mDatas.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getDistanceDays(String str1, String str2) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60 * 24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView TitleTextView;
        TextView NumberTextView;
        TextView TypeTextView;
        Note viewNote;

        public ViewHolder(View itemView) {
            super(itemView);
            NumberTextView = (TextView) itemView.findViewById(R.id.countdown_number);
            TitleTextView = (TextView) itemView.findViewById(R.id.countdown_title);
            TypeTextView=(TextView)itemView.findViewById(R.id.count_item_type);
            viewNote = new Note();
        }
    }
}
