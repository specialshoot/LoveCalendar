package com.example.lovecalendar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lovecalendar.Utils.ToastUtils;
import com.example.lovecalendar.model.Note;
import com.example.lovecalendar.view.CustomDialog;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateActivity extends AppCompatActivity {

    private static final String TAG = "CreateActivity";
    public static final int REQUSET = 1;

    @Bind(R.id.create_toolbar)
    Toolbar create_toolbar;
    @Bind(R.id.create_title_textview)
    TextView create_title_textview;
    @Bind(R.id.create_note_textview)
    TextView create_note_textview;
    @Bind(R.id.create_type_textview)
    TextView create_type_textview;
    @Bind(R.id.create_toolbar_title)
    TextView create_toolbar_title;

    public static final String CREATE = "create";
    private String getDateString;
    private Date getDate;
    private int year, month, day;
    private String[] typeStrings = {"普通记事", "倒数日", "纪念日", "生日"};
    private int typeNum = 0;
    private boolean isModifyMode = false;
    private Note haveNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();

        haveNote = (Note) bundle.getSerializable("note");
        if (haveNote != null) {
            create_title_textview.setText(haveNote.getTitle());
            create_note_textview.setText(haveNote.getContent());
            switch (haveNote.getType()) {
                case 1:
                    create_type_textview.setText("普通记事");
                    typeNum = 1;
                    break;
                case 2:
                    create_type_textview.setText("倒数日");
                    typeNum = 2;
                    break;
                case 3:
                    create_type_textview.setText("纪念日");
                    typeNum = 3;
                    break;
                case 4:
                    create_type_textview.setText("生日");
                    typeNum = 4;
                    break;
                default:
                    break;
            }
            isModifyMode = true;
        }

        getDateString = bundle.getString("date");
        year = Integer.parseInt(getDateString.substring(0, getDateString.indexOf("-")));
        month = Integer.parseInt(getDateString.substring(getDateString.indexOf("-") + 1, getDateString.lastIndexOf("-")));
        day = Integer.parseInt(getDateString.substring(getDateString.lastIndexOf("-") + 1, getDateString.length()));

        create_toolbar_title.setText(year + "-" + month + "-" + day);
        setSupportActionBar(create_toolbar);
    }

    @OnClick(R.id.create_title)
    void createTitle() {
        final CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("标题");
        builder.setInitMessage(create_title_textview.getText().toString());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (builder.message.getText().toString().equals("")) {
                    ToastUtils.showShort(CreateActivity.this, "请输入内容");
                } else {
//                    ToastUtils.showShort(CreateActivity.this, builder.message.getText());
                    create_title_textview.setText(builder.message.getText());
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    @OnClick(R.id.create_note)
    void createNote() {
        Intent intent = new Intent(CreateActivity.this, WriteNoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("note", create_note_textview.getText().toString());
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUSET);
    }

    @OnClick(R.id.create_type)
    void createType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("选择记事类型");

        builder.setItems(typeStrings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                create_type_textview.setText(typeStrings[which]);
                typeNum = which + 1;
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.create_image_back)
    void createBack() {
        if (isModifyMode) {
            new AlertDialog.Builder(CreateActivity.this).setTitle("系统提示")//设置对话框标题

                    .setMessage("确认放弃修改？")//设置显示的内容
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
        } else {
            new AlertDialog.Builder(CreateActivity.this).setTitle("系统提示")//设置对话框标题

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create_save) {

            if (create_title_textview.getText().equals("")) {
                ToastUtils.showShort(CreateActivity.this, "请填写标题");
            } else if (create_note_textview.getText().equals("")) {
                ToastUtils.showShort(CreateActivity.this, "请填写内容");
            } else if (typeNum == 0) {
                ToastUtils.showShort(CreateActivity.this, "请选择笔记类型");
            } else {
                if (typeNum == 2) {
                    try {
                        String nowString = "";
                        String dateString = getDateString;
                        Calendar tempCalendar = Calendar.getInstance();
                        int nowYear = tempCalendar.get(Calendar.YEAR);
                        int nowMonth = tempCalendar.get(Calendar.MONTH) + 1;
                        int nowDay = tempCalendar.get(Calendar.DAY_OF_MONTH);
                        nowString = nowYear + "-" + nowMonth + "-" + nowDay;
                        long interval = getDistanceDays(nowString, dateString);
                        if (interval == 0) {
                            ToastUtils.showShort(CreateActivity.this, "就是今天,赶紧做吧");
                            return true;
                        } else if (interval < 0) {
                            ToastUtils.showShort(CreateActivity.this, "倒数日时间早于今天");
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (typeNum == 3) {
                    try {
                        String nowString = "";
                        String dateString = getDateString;
                        Calendar tempCalendar = Calendar.getInstance();
                        int nowYear = tempCalendar.get(Calendar.YEAR);
                        int nowMonth = tempCalendar.get(Calendar.MONTH) + 1;
                        int nowDay = tempCalendar.get(Calendar.DAY_OF_MONTH);
                        nowString = nowYear + "-" + nowMonth + "-" + nowDay;
                        long interval = getDistanceDays(nowString, dateString);
                        if(interval>0){
                            ToastUtils.showShort(CreateActivity.this,"纪念日不应超过今天");
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                QueryBuilder qb = new QueryBuilder(Note.class)
                        .columns(new String[]{"_id"})
                        .where("title = ?", new String[]{create_title_textview.getText().toString()});
                long count = App.sDb.queryCount(qb);
//                System.out.println("count : " + count);
                if (count > 0 && !isModifyMode) {
                    ToastUtils.showShort(CreateActivity.this, "标题与现存数据重复，请重新命名标题");
                } else {
                    Note note = new Note();
                    if (isModifyMode) {
                        note = haveNote;
                    }
                    note.setTitle(create_title_textview.getText().toString());
                    note.setContent(create_note_textview.getText().toString());
                    note.setYear(year);
                    note.setMonth(month);
                    note.setDay(day);
                    note.setType(typeNum);
                    if(typeNum==2){
                        note.setIsFisish(0);
                    }
                    if (isModifyMode) {
                        App.sDb.update(note, ConflictAlgorithm.Fail);
                        Intent intent = new Intent();
                        intent.putExtra(CREATE, "ok");
                        setResult(RESULT_OK, intent);
                    } else {
                        App.sDb.insert(note);
                    }
                    Intent intent=new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 比较时间差,用于判断选择倒数日一项选择日期是否小于当前日期
     *
     * @param str1 当前日期
     * @param str2 选择日期
     * @return <=0代表不符合倒数日要求
     * @throws Exception
     */

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
            diff = time2 - time1;
            days = diff / (1000 * 60 * 60 * 24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (requestCode == CreateActivity.REQUSET && resultCode == RESULT_OK) {
            String str = data.getStringExtra(WriteNoteActivity.WRITENOTE);
            create_note_textview.setText(str);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isModifyMode) {
                new AlertDialog.Builder(CreateActivity.this).setTitle("系统提示")//设置对话框标题

                        .setMessage("确认放弃修改？")//设置显示的内容
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
            } else {
                new AlertDialog.Builder(CreateActivity.this).setTitle("系统提示")//设置对话框标题

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
        }
        return false;
    }

}
