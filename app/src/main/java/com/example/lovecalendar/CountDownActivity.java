package com.example.lovecalendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.lovecalendar.adapter.CountDownRecyclerViewAdapter;
import com.example.lovecalendar.model.Note;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CountDownActivity extends AppCompatActivity implements CountDownRecyclerViewAdapter.OnItemClickListener {

    @Bind(R.id.count_down_toolbar)
    Toolbar toolbar;
    @Bind(R.id.rv_count_down)
    RecyclerView mRecyclerView;

    private static final int REQUEST = 4;
    private CountDownRecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String[] operationStrings = {"删除"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        QueryBuilder query = new QueryBuilder(Note.class)
                .appendOrderDescBy("year")
                .appendOrderDescBy("month")
                .appendOrderDescBy("day")
                .where("type = ?", new String[]{"2"});

        ArrayList<Note> temp = App.sDb.query(query);
        query = new QueryBuilder(Note.class)
                .appendOrderDescBy("year")
                .appendOrderDescBy("month")
                .appendOrderDescBy("day")
                .where("type = ?", new String[]{"4"});
        ArrayList<Note> temp2 = App.sDb.query(query);
        temp.addAll(temp2);
        System.out.println("有" + temp.size() + "条数据");

        mLayoutManager = new LinearLayoutManager(CountDownActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewAdapter = new CountDownRecyclerViewAdapter(CountDownActivity.this);
        mRecyclerViewAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerViewAdapter.mDatas.addAll(temp);
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position, Note note) {
        String sendDate = note.getYear() + "-" + note.getMonth() + "-" + note.getDay();
        Intent intent = new Intent(CountDownActivity.this, CreateActivity.class);
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
        if (requestCode == CountDownActivity.REQUEST && resultCode == RESULT_OK) {
            System.out.println("onActivityResult");
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
                        new AlertDialog.Builder(CountDownActivity.this).setTitle("系统提示")

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
        QueryBuilder query = new QueryBuilder(Note.class)
                .appendOrderDescBy("year")
                .appendOrderDescBy("month")
                .appendOrderDescBy("day")
                .where("type = ?", new String[]{"2"});
        ArrayList<Note> temp = App.sDb.query(query);
        query = new QueryBuilder(Note.class)
                .appendOrderDescBy("year")
                .appendOrderDescBy("month")
                .appendOrderDescBy("day")
                .where("type = ?", new String[]{"4"});
        ArrayList<Note> temp2 = App.sDb.query(query);
        temp.addAll(temp2);
        System.out.println("有" + temp.size() + "条数据");
        mRecyclerViewAdapter.mDatas.clear();
        mRecyclerViewAdapter.mDatas.addAll(temp);
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.count_down_back)
    void CountBack() {
        Intent intent=new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
