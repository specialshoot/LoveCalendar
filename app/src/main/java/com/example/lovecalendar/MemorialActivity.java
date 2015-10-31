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

import com.example.lovecalendar.adapter.MemorialRecyclerViewAdapter;
import com.example.lovecalendar.adapter.MyRecyclerViewAdapter;
import com.example.lovecalendar.model.Note;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MemorialActivity extends AppCompatActivity implements MemorialRecyclerViewAdapter.OnItemClickListener {

    @Bind(R.id.memorial_toolbar)
    Toolbar toolbar;
    @Bind(R.id.rv_memorial)
    RecyclerView mRecyclerView;

    private static final int REQUEST=3;
    private MemorialRecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String[] operationStrings={"删除"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorial);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        QueryBuilder query = new QueryBuilder(Note.class)
                .appendOrderDescBy("year")
                .appendOrderDescBy("month")
                .appendOrderDescBy("day")
                .where("type = ?",new String[]{"3"});

        ArrayList<Note> temp = App.sDb.query(query);
//        System.out.println("有" + temp.size() + "条数据");

        mLayoutManager = new LinearLayoutManager(MemorialActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewAdapter = new MemorialRecyclerViewAdapter(MemorialActivity.this);
        mRecyclerViewAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerViewAdapter.mDatas.addAll(temp);
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position, Note note) {
        String sendDate = note.getYear() + "-" + note.getMonth() + "-" + note.getDay();
        Intent intent=new Intent(MemorialActivity.this,CreateActivity.class);
        Bundle bundle=new Bundle();
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
        if (requestCode == MemorialActivity.REQUEST && resultCode == RESULT_OK) {
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
                        new AlertDialog.Builder(MemorialActivity.this).setTitle("系统提示")

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
                .where("type = ?",new String[]{"3"});
        ArrayList<Note> temp = App.sDb.query(query);
//        System.out.println("有" + temp.size() + "条数据");
        mRecyclerViewAdapter.mDatas.clear();
        mRecyclerViewAdapter.mDatas.addAll(temp);
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.memorial_back)
    void MemorialBack() {
        Intent intent=new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
