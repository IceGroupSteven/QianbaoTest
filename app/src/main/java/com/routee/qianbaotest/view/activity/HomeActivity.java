package com.routee.qianbaotest.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.routee.qianbaotest.R;
import com.routee.qianbaotest.base.BaseActivity;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private Button mBtCollapsing;
    private Button mBtOcr;
    private Button mBtPdf;
    private Button mBtTwoLevelPulldown;
    private Intent mIntent;

    @Override
    public int rootView() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        mBtCollapsing = (Button) findViewById(R.id.bt_collapsing);
        mBtOcr = (Button) findViewById(R.id.bt_ocr);
        mBtPdf = (Button) findViewById(R.id.bt_pdf);
        mBtTwoLevelPulldown = (Button) findViewById(R.id.bt_two_level_refresh_layout);
        mBtCollapsing.setOnClickListener(this);
        mBtOcr.setOnClickListener(this);
        mBtPdf.setOnClickListener(this);
        mBtTwoLevelPulldown.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_collapsing:
                mIntent = new Intent(this, CollapsinToolbarActivity.class);
                startActivity(mIntent);
                break;
            case R.id.bt_ocr:
                mIntent = new Intent(this, OcrActivity.class);
                startActivity(mIntent);
                break;
            case R.id.bt_pdf:
                mIntent = new Intent(this, PdfViewActivity.class);
                startActivity(mIntent);
                break;
            case R.id.bt_two_level_refresh_layout:
                mIntent = new Intent(this, TwoLevelPulldownActivity.class);
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }
}
