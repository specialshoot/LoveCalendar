package com.example.lovecalendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WriteNoteActivity extends AppCompatActivity {

    private static final String TAG = "WriteNoteActivity";
    public static final String WRITENOTE = "note";
    @Bind(R.id.write_toolbar)
    Toolbar write_toolbar;
    @Bind(R.id.write_note_edittext)
    EditText note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);
        ButterKnife.bind(this);
        setSupportActionBar(write_toolbar);
        Bundle bundle = getIntent().getExtras();
        note.setText(bundle.getString("note"));
    }

    @OnClick(R.id.write_back)
    void CancleNote() {
        new AlertDialog.Builder(WriteNoteActivity.this).setTitle("系统提示")//设置对话框标题

                .setMessage("确认放弃记事？")//设置显示的内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        // TODO Auto-generated method stub
                        finish();
                    }

                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮

            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件
                // TODO Auto-generated method stub
                Log.i("alertdialog", " 请保存数据！");
            }
        }).show();//在按键响应事件中显示此对话框
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_writenote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_write_save) {
            Intent intent = new Intent();
            intent.putExtra(WRITENOTE, note.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(WriteNoteActivity.this).setTitle("系统提示")//设置对话框标题

                    .setMessage("确认放弃记事？")//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            // TODO Auto-generated method stub
                            finish();
                        }

                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮

                @Override
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    // TODO Auto-generated method stub
                    Log.i("alertdialog", " 请保存数据！");
                }
            }).show();//在按键响应事件中显示此对话框
        }
        return false;
    }

}
