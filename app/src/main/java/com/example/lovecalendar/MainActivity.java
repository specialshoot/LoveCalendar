package com.example.lovecalendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.lovecalendar.Utils.ToastUtils;
import com.example.lovecalendar.model.Note;
import com.example.lovecalendar.view.KCalendar;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.androidbucket.utils.imageprocess.ABShape;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    @Bind(R.id.window_calendar)
    KCalendar window_calendar;
    @Bind(R.id.main_toolbar)
    Toolbar toolbar;
    @Bind(R.id.activity_main_rfal)
    RapidFloatingActionLayout rfaLayout;
    @Bind(R.id.activity_main_rfab)
    RapidFloatingActionButton rfaButton;

    private RapidFloatingActionHelper rfabHelper;
    private static Boolean isExit = false;    //判断是否第一次点击退出
    private int year, month, day;
    String date = null;// 设置默认选中的日期  格式为 “2014-04-05” 标准DATE格式
    public static final int REQUSET = 1;
    QueryBuilder query=null;
    private boolean haveEvent=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        if (month < 10 && day < 10) {
            date = year + "-0" + month + "-0" + day;
        } else if (month < 10 && day >= 10) {
            date = year + "-0" + month + "-" + day;
        } else if (month >= 10 && day < 10) {
            date = year + "-" + month + "-0" + day;
        } else {
            date = year + "-" + month + "-" + day;
        }
        if (null != date) {

            int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
            int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
            toolbar.setTitle(years + " 年 " + month + " 月");
            setSupportActionBar(toolbar);
            window_calendar.showCalendar(years, month);
            window_calendar.setCalendarDayBgColor(date, R.drawable.calendar_date_focused);
            queryHaveEvent();
        }

        //监听所选中的日期
        window_calendar.setOnCalendarClickListener(new KCalendar.OnCalendarClickListener() {

            public void onCalendarClick(int row, int col, String dateFormat) {
                year = Integer.parseInt(dateFormat.substring(0,
                        dateFormat.indexOf("-")));
                month = Integer.parseInt(dateFormat.substring(
                        dateFormat.indexOf("-") + 1,
                        dateFormat.lastIndexOf("-")));
                day = Integer.parseInt(dateFormat.substring(
                        dateFormat.lastIndexOf("-") + 1, dateFormat.length()));

                if (window_calendar.getCalendarMonth() - month == 1//跨年跳转
                        || window_calendar.getCalendarMonth() - month == -11) {
                    window_calendar.lastMonth();

                } else if (month - window_calendar.getCalendarMonth() == 1 //跨年跳转
                        || month - window_calendar.getCalendarMonth() == -11) {
                    window_calendar.nextMonth();
                } else {
                    window_calendar.removeAllBgColor();
                    window_calendar.setCalendarDayBgColor(dateFormat,
                            R.drawable.calendar_date_focused);
                    date = dateFormat;//最后返回给全局 date
                }
                queryHaveEvent();
            }
        });

        //监听当前月份
        window_calendar.setOnCalendarDateChangedListener(new KCalendar.OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                toolbar.setTitle(year + " 年 " + month + " 月");
            }
        });

        setRfa();
    }

    private void queryHaveEvent(){
        query = new QueryBuilder(Note.class)
                .whereOr("year = ? and month=? and day=?",
                        new String[]{year + "", month + "", day + ""})
                .whereOr("type = ? and month=? and day=?",
                        new String[]{3 + "", month + "", day + ""})
                .whereOr("type = ? and month=? and day=?",
                        new String[]{4 + "", month + "", day + ""});
        long number = App.sDb.queryCount(query);
        if(number>0){
            haveEvent=true;
        }else{
            haveEvent=false;
        }
        invalidateOptionsMenu();
    }

    private void setRfa() {

        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(MainActivity.this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("普通记事")
                        .setResId(R.mipmap.ic_github)
                        .setIconNormalColor(0xffd84315)
                        .setIconPressedColor(0xffbf360c)
                        .setLabelColor(Color.WHITE)
                        .setLabelBackgroundDrawable(ABShape.generateCornerShapeDrawable(0xaad84315, ABTextUtil.dip2px(MainActivity.this, 4)))
                        .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("倒数日/生日")
                        .setResId(R.mipmap.mode)
                        .setIconNormalColor(0xff4e342e)
                        .setIconPressedColor(0xff3e2723)
                        .setLabelColor(Color.WHITE)
                        .setLabelSizeSp(14)
                        .setLabelBackgroundDrawable(ABShape.generateCornerShapeDrawable(0xaa000000, ABTextUtil.dip2px(MainActivity.this, 4)))
                        .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("纪念日")
                        .setResId(R.mipmap.jinian)
                        .setIconNormalColor(0xff2e0444)
                        .setIconPressedColor(0xff3e2723)
                        .setLabelColor(Color.WHITE)
                        .setLabelSizeSp(14)
                        .setLabelBackgroundDrawable(ABShape.generateCornerShapeDrawable(0xaa8b94a5, ABTextUtil.dip2px(MainActivity.this, 4)))
                        .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("所有事项")
                        .setResId(R.mipmap.all)
                        .setIconNormalColor(0xff3e2723)
                        .setIconPressedColor(0xffd84315)
                        .setLabelColor(Color.WHITE)
                        .setLabelSizeSp(14)
                        .setLabelBackgroundDrawable(ABShape.generateCornerShapeDrawable(0xaa3e2723, ABTextUtil.dip2px(MainActivity.this, 4)))
                        .setWrapper(3)
        );
        rfaContent.setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(MainActivity.this, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(MainActivity.this, 5));

        rfabHelper = new RapidFloatingActionHelper(MainActivity.this, rfaLayout, rfaButton, rfaContent).build();
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        rfabHelper.toggleContent();
        switch (position) {
            case 0:
                Intent intent = new Intent(MainActivity.this, NormalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("na", "normal");
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUSET);
                break;
            case 1:
                Intent intentCountDown = new Intent(MainActivity.this, CountDownActivity.class);
                startActivityForResult(intentCountDown, REQUSET);
                break;
            case 2:
                Intent intentMemorial = new Intent(MainActivity.this, MemorialActivity.class);
                startActivityForResult(intentMemorial, REQUSET);
                break;
            case 3:
                Intent intentAll = new Intent(MainActivity.this, NormalActivity.class);
                Bundle bundleAll = new Bundle();
                bundleAll.putString("na", "all");
                intentAll.putExtras(bundleAll);
                startActivityForResult(intentAll, REQUSET);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        rfabHelper.toggleContent();
        switch (position) {
            case 0:
                Intent intent = new Intent(MainActivity.this, NormalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("na", "normal");
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUSET);
                break;
            case 1:
                Intent intentCountDown = new Intent(MainActivity.this, CountDownActivity.class);
                startActivityForResult(intentCountDown, REQUSET);
                break;
            case 2:
                Intent intentMemorial = new Intent(MainActivity.this, MemorialActivity.class);
                startActivityForResult(intentMemorial, REQUSET);
                break;
            case 3:
                Intent intentAll = new Intent(MainActivity.this, NormalActivity.class);
                Bundle bundleAll = new Bundle();
                bundleAll.putString("na", "all");
                intentAll.putExtras(bundleAll);
                startActivityForResult(intentAll, REQUSET);
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.createNote)
    void createNote() {
        String sendDate = year + "-" + month + "-" + day;
        Intent intent = new Intent(MainActivity.this, CreateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("date", sendDate);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUSET);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_event);
        if(haveEvent==false){
            item.setIcon(android.R.drawable.star_big_off);
        }else {
            item.setIcon(android.R.drawable.star_big_on);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_today) {
            window_calendar.removeAllBgColor();
            year = Calendar.getInstance().get(Calendar.YEAR);
            month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (month < 10 && day < 10) {
                date = year + "-0" + month + "-0" + day;
            } else if (month < 10 && day >= 10) {
                date = year + "-0" + month + "-" + day;
            } else if (month >= 10 && day < 10) {
                date = year + "-" + month + "-0" + day;
            } else {
                date = year + "-" + month + "-" + day;
            }
            if (null != date) {

                int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
                int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
                toolbar.setTitle(years + " 年 " + month + " 月");
                setSupportActionBar(toolbar);
                window_calendar.showCalendar(years, month);
                window_calendar.setCalendarDayBgColor(date, R.drawable.calendar_date_focused);
                queryHaveEvent();
            }
            return true;
        }

        if (id == R.id.action_date) {
            DatePickerDialog dlg = new DatePickerDialog(this, this, year, month - 1, day);
            dlg.show();
            return true;
        }

        if (id == R.id.action_event) {
            if(haveEvent==true) {
                Intent intentToday = new Intent(MainActivity.this, NormalActivity.class);
                Bundle bundleToday = new Bundle();
                boolean specialDay=false;
                query = new QueryBuilder(Note.class)
                        .whereOr("type = ? and month=? and day=?",
                                new String[]{3 + "", month + "", day + ""})
                        .whereOr("type = ? and month=? and day=?",
                                new String[]{4 + "", month + "", day + ""});
                long number = App.sDb.queryCount(query);
                if(number>0){
                    specialDay=true;
                }
                bundleToday.putString("na", "event");
                bundleToday.putInt("year", year);
                bundleToday.putInt("month", month);
                bundleToday.putInt("day", day);
                bundleToday.putBoolean("special",specialDay);
                intentToday.putExtras(bundleToday);
                startActivityForResult(intentToday, REQUSET);
            }else {
                ToastUtils.showShort(MainActivity.this,"所选日期没有事件");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (requestCode == REQUSET && resultCode == RESULT_OK) {
            System.out.println("enter onActivityResult");
            window_calendar.removeAllBgColor();
            System.out.println("finish RemoveAllBgColor");
            if (null != date) {
                int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
                int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
                toolbar.setTitle(years + " 年 " + month + " 月");
                System.out.println("time : " + year + "/" + month + "/" + day);
                setSupportActionBar(toolbar);
                window_calendar.showCalendar(years, month);
                window_calendar.setCalendarDayBgColor(date, R.drawable.calendar_date_focused);
                queryHaveEvent();
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int yearOfToday, int monthOfYear, int dayOfMonth) {

        if (year == yearOfToday && month == monthOfYear + 1 && day == dayOfMonth) {
//            Toast.makeText(TodayActivity.this, "时间选择没有改变", Toast.LENGTH_SHORT).show();
//            System.out.println("时间选择没有改变");
        } else {
            window_calendar.removeAllBgColor();
            year = yearOfToday;
            month = monthOfYear + 1;
            day = dayOfMonth;
            if (month < 10 && day < 10) {
                date = year + "-0" + month + "-0" + day;
            } else if (month < 10 && day >= 10) {
                date = year + "-0" + month + "-" + day;
            } else if (month >= 10 && day < 10) {
                date = year + "-" + month + "-0" + day;
            } else {
                date = year + "-" + month + "-" + day;
            }
            if (null != date) {
                int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
                int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
                toolbar.setTitle(years + " 年 " + month + " 月");
                setSupportActionBar(toolbar);
                window_calendar.showCalendar(years, month);
                window_calendar.setCalendarDayBgColor(date, R.drawable.calendar_date_focused);
                queryHaveEvent();
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true;
            ToastUtils.showShort(MainActivity.this, "再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;   //取消退出
                }
            }, 2000);    //等待2秒钟
        } else {
            finish();
        }
    }
}
