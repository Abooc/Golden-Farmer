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
import org.lee.android.app.JanbanMaker.common.JiabanCalculator;
import org.lee.android.app.JanbanMaker.R;
import org.lee.android.util.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener
        , View.OnTouchListener, TextView.OnEditorActionListener {

    private TextView mMessageText;
    private TextView mShowText;
    private EditText mStartEdit;
    private EditText mEndEdit;

    private ArrayList<String> arrayList = new ArrayList<String>();

    private boolean removeOnStartText;
    private boolean removeOnEndText;
    private View mResultLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Tracker t = ((AppApplication)getApplication()).getTracker(
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
        mStartEdit = (EditText) findViewById(R.id.Start);
        mEndEdit = (EditText) findViewById(R.id.End);
        findViewById(R.id.Add).setOnClickListener(this);

        mStartEdit.setOnEditorActionListener(this);
        mEndEdit.setOnEditorActionListener(this);
        mStartEdit.addTextChangedListener(iStartTextWatcher);
        mEndEdit.addTextChangedListener(iEndTextWatcher);

        findViewById(R.id.Content).setOnTouchListener(this);
        mResultLayout = findViewById(R.id.ResultLayout);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Share:
                Toast.show("建设中...");
                break;
            case R.id.Clear:
                mResultLayout.setVisibility(View.GONE);
                arrayList.clear();
                break;
            case R.id.About:
                AboutActivity.launch(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        switch (textView.getId()) {
            case R.id.Start:
                mEndEdit.requestFocus();
                return true;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Add:
                boolean isAdded = add();
                if(isAdded)
                    OK();
                break;
        }
    }

    private boolean add(){
        String start = mStartEdit.getText().toString().trim();
        String end = mEndEdit.getText().toString().trim();
        if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
            Toast.show("开始和结束时间必须填写！");
            return false;
        }
        arrayList.add(start + ">" + end);

        mEndEdit.setText(null);
        mStartEdit.setText(null);
        mStartEdit.requestFocus();
        return true;
    }

    private void OK(){
        AppFunction.hideInputMethod(this, getWindow().getDecorView());
        JiabanCalculator iCalculator = new JiabanCalculator();

        for (int i = 0; i < arrayList.size(); i++) {
            String[] time = arrayList.get(i).split(">");

            iCalculator.add(time[0], time[1]);

        }
        iCalculator.calculate();
        String ss = iCalculator.result();
        TextView Show2 = (TextView) findViewById(R.id.Show2);
        Show2.setText(ss);

        String show = iCalculator.toShow();
        mShowText.setText(show);
        mResultLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            AppFunction.hideInputMethod(this, mResultLayout);
            return true;
        }
        return false;
    }

    private TextWatcher iStartTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Log.anchor(s.toString() + ", start:" + start + ", count:" + count + ", after:" + after);
            removeOnStartText = after == 0 && s.toString().endsWith(":");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.anchor(s.toString() + ", start:" + start + ", before:" + before + ", count:" + count);
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

    private String toShow(ArrayList<String> data) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < data.size(); i++) {
            String str = data.get(i);
            buffer.append("开始：" + str + "\n");
        }
        return buffer.toString();
    }

}
