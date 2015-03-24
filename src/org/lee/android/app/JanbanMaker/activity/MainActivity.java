package org.lee.android.app.JanbanMaker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.lee.android.app.JanbanMaker.AppApplication;
import org.lee.android.app.JanbanMaker.AppFunction;
import org.lee.android.app.JanbanMaker.R;
import org.lee.android.app.JanbanMaker.common.JiabanCalculator;
import org.lee.android.util.Toast;

/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 14-09-05.
 * <p/>
 * 农夫计算器主界面
 */
public class MainActivity extends Activity implements View.OnClickListener
        , View.OnTouchListener, TextView.OnEditorActionListener {

    private TextView mMessageText;
    private TextView mTimeText;
    private TextView mShowText;
    private EditText mStartDateEdit;
    private EditText mStartEdit;
    private EditText mEndEdit;

    private boolean removeOnStartText;
    private boolean removeOnEndText;
    private View mResultLayout;

    private JiabanCalculator iCalculator = new JiabanCalculator();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tracker t = ((AppApplication) getApplication()).getTracker(
                AppApplication.TrackerName.APP_TRACKER);
        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("MainActivity");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());


        init();
    }

    private void init() {
        mMessageText = (TextView) findViewById(R.id.Message);
        mShowText = (TextView) findViewById(R.id.Show);
        mTimeText = (TextView) findViewById(R.id.Time);
        mStartDateEdit = (EditText) findViewById(R.id.StartDate);
        mStartEdit = (EditText) findViewById(R.id.Start);
        mEndEdit = (EditText) findViewById(R.id.End);
        findViewById(R.id.Rollback).setOnClickListener(this);
        findViewById(R.id.Add).setOnClickListener(this);

        mStartEdit.setOnEditorActionListener(this);
        mEndEdit.setOnEditorActionListener(this);
        mStartEdit.addTextChangedListener(iStartTextWatcher);
        mEndEdit.addTextChangedListener(iEndTextWatcher);
        mStartDateEdit.addTextChangedListener(iTextWatcher);

        /** 除编辑框和按钮之外区域，设置Touch事件收起键盘 */
        findViewById(R.id.Content).setOnTouchListener(this);
        mResultLayout = findViewById(R.id.ResultLayout);

    }

    /**
     * 添加应用标题栏菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Advanced://高级设置功能在建设中
                Toast.show("建设中...");
                break;
            case R.id.Share://分享功能增在建设中
                Toast.show("建设中...");
                break;
            case R.id.Clear://清空当前操作记录，便于重新开始一个计算
                mResultLayout.setVisibility(View.GONE);
                iCalculator.clear();
                break;
            case R.id.About://跳转到关于页面
                AboutActivity.launch(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 用于开始时间编辑框快速切换到下一个编辑框
     *
     * @param textView 开始时间编辑框
     * @param actionId Identifier of the action.  This will be either the
     *                 identifier you supplied, or {@link android.view.inputmethod.EditorInfo#IME_NULL
     *                 EditorInfo.IME_NULL} if being called due to the enter key
     *                 being pressed.
     * @param keyEvent
     * @return
     */
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        switch (textView.getId()) {
            case R.id.Start:
                mEndEdit.requestFocus();
                return true;
        }
        return true;
    }

    /**
     * 添加一条时间记录，并进行计算事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Rollback:
                int size = iCalculator.getRecords().size();
                if (size == 0)
                    return;
                iCalculator.remove(size - 1);
                OK();
                break;
            case R.id.Add:
                boolean isAdded = add();
                if (isAdded)
                    OK();
                break;
        }
    }

    /**
     * 将时间记录添加到记录缓存集合
     *
     * @return
     */
    private boolean add() {
        String startDate = mStartDateEdit.getText().toString().trim();
        String start = mStartEdit.getText().toString().trim();
        String end = mEndEdit.getText().toString().trim();
        if (TextUtils.isEmpty(startDate)) {
            mStartDateEdit.requestFocus();
            Toast.show("日期必须填写！");
            return false;
        }
        if (TextUtils.isEmpty(start)) {
            mStartEdit.requestFocus();
            Toast.show("开始时间必须填写！");
            return false;
        }
        if (TextUtils.isEmpty(end)) {
            mEndEdit.requestFocus();
            Toast.show("结束时间必须填写！");
            return false;
        }

        iCalculator.add(startDate, start, end);

        mStartDateEdit.requestFocus();
        mStartEdit.setText(null);
        mEndEdit.setText(null);
        return true;
    }

    /**
     * 遍历时间记录缓存集合，进行计算
     */
    private void OK() {
        iCalculator.calculate();
        float hours = iCalculator.result();
        String result = "有效加班总时间(单位小时):" + hours;

        TextView Show2 = (TextView) findViewById(R.id.Show2);
        Show2.setText(result);
        mTimeText.setText("(总" + hours + "小时)");

        String show = iCalculator.toShow();
        mShowText.setText(show);
        mResultLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 除编辑框和按钮之外区域，设置Touch事件收起键盘
     *
     * @param v     除编辑框和按钮之外区域
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            AppFunction.hideInputMethod(this, mResultLayout);
            return false;
        }
        return false;
    }

    private boolean removeOnStartDateText;

    /**
     * 日期输入框智能自动匹配监听器
     */
    private TextWatcher iTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            removeOnStartDateText = after == 0 && s.toString().endsWith("/");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int length = s.length() - 1;
            if (s == null || length < 0) return;
            if (removeOnStartDateText) {
                removeOnStartDateText = false;
                mStartDateEdit.setText(s.subSequence(0, length));
                mStartDateEdit.setSelection(length);
                return;
            }

            if (length == 0) {
                char c = s.charAt(length);
                if ((c < 48 || c > 49)) {
                    mStartDateEdit.setText(s.insert(0, "0").append("/"));
                    mStartDateEdit.setSelection(3);
                    return;
                }
            }
            if (length == 3) {
                char c = s.charAt(length);
                if ((c < 48 || c > 49)) {
                    mStartDateEdit.setText(s.insert(length, "0"));
                    mStartDateEdit.setSelection(s.length());
                    return;
                }
            }

            if (s.length() == 2 && !"/".equals(s.charAt(length))) {
                mStartDateEdit.setText(s + "/");
                mStartDateEdit.setSelection(s.length() + 1);
                return;
            }
            if (s.length() == 5) {
                mStartEdit.requestFocus();
                return;
            }

        }
    };

    /**
     * 开始时间的时间输入自动匹配器
     */
    private TextWatcher iStartTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //测试日志
//            Log.anchor(s.toString() + ", start:" + start + ", count:" + count + ", after:" + after);
            removeOnStartText = after == 0 && s.toString().endsWith(":");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //测试日志
//            Log.anchor(s.toString() + ", start:" + start + ", before:" + before + ", count:" + count);
//                   if(count == 2){
//                       mStartEdit.setText(s+":");
//                   }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (removeOnStartText) {
                removeOnStartText = false;
                int length = s.length() - 1;
                mStartEdit.setText(s.subSequence(0, length));
                mStartEdit.setSelection(length);
                return;
            }
            if (s.length() == 2 || s.length() == 5) {
                mStartEdit.setText(s + ":");
                mStartEdit.setSelection(s.length() + 1);
                return;
            }
            if (s.length() == 8) {
                mEndEdit.requestFocus();
                return;
            }

        }
    };


    /**
     * 结束时间的时间输入自动匹配器
     */
    private TextWatcher iEndTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            removeOnEndText = after == 0 && s.toString().endsWith(":");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (removeOnEndText) {
                removeOnEndText = false;
                int length = s.length() - 1;
                mEndEdit.setText(s.subSequence(0, length));
                mEndEdit.setSelection(length);
                return;
            }
            if (s.length() == 2 || s.length() == 5) {
                mEndEdit.setText(s + ":");
                mEndEdit.setSelection(s.length() + 1);
            }

        }
    };

}
