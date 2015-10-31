package com.example.lovecalendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lovecalendar.Utils.SnackbarUtil;
import com.example.lovecalendar.Utils.ToastUtils;
import com.example.lovecalendar.adapter.MyRecyclerViewAdapter;
import com.example.lovecalendar.model.Note;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NormalActivity extends AppCompatActivity implements MyRecyclerViewAdapter.OnItemClickListener {

    @Bind(R.id.normal_toolbar)
    Toolbar toolbar;
    @Bind(R.id.normal_title)
    TextView normal_title;
    @Bind(R.id.rv_normal)
    RecyclerView mRecyclerView;

    private MyRecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String[] operationStrings = {"删除"};
    private static final int REQUEST = 2;
    private String na = "";
    private int nowYear, nowMonth, nowDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        na = bundle.getString("na");

        QueryBuilder query = null;
        if (na.equals("normal")) {
            normal_title.setText("普通记事");
            query = new QueryBuilder(Note.class)
                    .appendOrderDescBy("year")
                    .appendOrderDescBy("month")
                    .appendOrderDescBy("day")
                    .where("type = ?", new String[]{"1"});
        } else if (na.equals("all")) {
            normal_title.setText("全部记事");
            query = new QueryBuilder(Note.class)
                    .appendOrderDescBy("year")
                    .appendOrderDescBy("month")
                    .appendOrderDescBy("day");
        } else if (na.equals("event")) {
            boolean specialDay = bundle.getBoolean("special");

            nowYear = bundle.getInt("year");
            nowMonth = bundle.getInt("month");
            nowDay = bundle.getInt("day");

            if (specialDay == true) {
                query = new QueryBuilder(Note.class)
                        .whereOr("year = ? and month=? and day=?",
                                new String[]{nowYear + "", nowMonth + "", nowDay + ""})
                        .whereOr("type = ? and month=? and day=?",
                                new String[]{3 + "", nowMonth + "", nowDay + ""})
                        .whereOr("type = ? and month=? and day=?",
                                new String[]{4 + "", nowMonth + "", nowDay + ""});
                long number = App.sDb.queryCount(query);
                normal_title.setText("纪念日/生日");
            } else {
                query = new QueryBuilder(Note.class)
                        .appendOrderDescBy("year")
                        .appendOrderDescBy("month")
                        .appendOrderDescBy("day").where("year = ? and month=? and day=?", new String[]{nowYear + "", nowMonth + "", nowDay + ""});
            }
        } else {
            ToastUtils.showShort(NormalActivity.this, "错误");
            finish();
        }
        ArrayList<Note> temp = App.sDb.query(query);
//        System.out.println("有" + temp.size() + "条数据");

        mLayoutManager = new LinearLayoutManager(NormalActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewAdapter = new MyRecyclerViewAdapter(NormalActivity.this);
        mRecyclerViewAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerViewAdapter.mDatas.addAll(temp);
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position, Note note) {
//        ToastUtils.showShort(NormalActivity.this, note.toString());
        String sendDate = note.getYear() + "-" + note.getMonth() + "-" + note.getDay();
        Intent intent = new Intent(NormalActivity.this, CreateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);
        bundle.putString("date", sendDate);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (requestCode == NormalActivity.REQUEST && resultCode == RESULT_OK) {
            resetNote();
        }
    }

    @Override
    public void onItemLongClick(View view, int position, final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(operationStrings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        new AlertDialog.Builder(NormalActivity.this).setTitle("系统提示")

                                .setMessage("确认删除？")//设置显示的内容
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteNote(note);
                                    }

                                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("alertdialog", " 请保存数据！");
                            }
                        }).show();//在按键响应事件中显示此对话框
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    public void deleteNote(Note note) {
        App.sDb.delete(note);
        resetNote();
    }

    public void resetNote() {
        QueryBuilder query = null;
        if (na.equals("normal")) {
            query = new QueryBuilder(Note.class)
                    .appendOrderDescBy("year")
                    .appendOrderDescBy("month")
                    .appendOrderDescBy("day")
                    .where("type = ?", new String[]{"1"});
        } else if (na.equals("all")) {
            query = new QueryBuilder(Note.class)
                    .appendOrderDescBy("year")
                    .appendOrderDescBy("month")
                    .appendOrderDescBy("day");
        } else if (na.equals("event")) {
            query = new QueryBuilder(Note.class)
                    .appendOrderDescBy("year")
                    .appendOrderDescBy("month")
                    .appendOrderDescBy("day").where("year = ? and month=? and day=?", new String[]{nowYear + "", nowMonth + "", nowDay + ""});
        } else {
            ToastUtils.showShort(NormalActivity.this, "错误");
            finish();
        }
        ArrayList<Note> temp = App.sDb.query(query);
//        System.out.println("有" + temp.size() + "条数据");
        mRecyclerViewAdapter.mDatas.clear();
        mRecyclerViewAdapter.mDatas.addAll(temp);
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.normal_back)
    void NormalBack() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
