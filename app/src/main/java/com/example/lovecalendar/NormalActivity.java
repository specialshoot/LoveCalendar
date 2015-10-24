package com.example.lovecalendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.lovecalendar.Utils.SnackbarUtil;
import com.example.lovecalendar.Utils.ToastUtils;
import com.example.lovecalendar.adapter.MyRecyclerViewAdapter;
import com.example.lovecalendar.model.Note;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NormalActivity extends AppCompatActivity implements MyRecyclerViewAdapter.OnItemClickListener {

    @Bind(R.id.normal_toolbar)
    Toolbar toolbar;
    @Bind(R.id.rv_normal)
    RecyclerView mRecyclerView;

    private MyRecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Note> mNoteList = new ArrayList<Note>();
    private static final String[] operationStrings={"删除"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        QueryBuilder query = new QueryBuilder(Note.class).appendOrderDescBy("year").appendOrderDescBy("month").appendOrderDescBy("day");
        ArrayList<Note> temp = App.sDb.query(query);
        System.out.println("有" + temp.size() + "条数据");

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
        ToastUtils.showShort(NormalActivity.this, note.toString());
    }

    @Override
    public void onItemLongClick(View view, int position, final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(operationStrings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
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
        QueryBuilder query = new QueryBuilder(Note.class).appendOrderDescBy("year").appendOrderDescBy("month").appendOrderDescBy("day");
        ArrayList<Note> temp = App.sDb.query(query);
        System.out.println("有" + temp.size() + "条数据");
        mRecyclerViewAdapter.mDatas.clear();
        mRecyclerViewAdapter.mDatas.addAll(temp);
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.normal_back)
    void NormalBack() {
        finish();
    }
}
