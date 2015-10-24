package com.example.lovecalendar.Utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by 轾 on 2015/10/8.
 */
public class SnackbarUtil {

    // android-support-design兼容包中新添加的一个类似Toast的控件。
    // make()中的第一个参数，可以写当前界面中的任意一个view对象。
    // android设计规范，新控件，snackbar
    // Toast提示不能进行其它操作，snackbar可以设置一个按钮进行某些必要操作
    private static Snackbar mSnackbar;

    public static void show(View view, String msg, int flag) {

        if (flag == 0) { // 短时显示
            mSnackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        } else if(flag==1) { // 长时显示
            mSnackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        } else{
            mSnackbar = Snackbar.make(view, msg, flag);
        }

        mSnackbar.show();
        // Snackbar中有一个可点击的文字，这里设置点击所触发的操作。
        mSnackbar.setAction("关了吧", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Snackbar在点击“关闭”后消失
                mSnackbar.dismiss();
            }
        });
    }
}
