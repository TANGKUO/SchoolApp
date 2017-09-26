package org.pointstone.cugapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.pointstone.cugapp.R;
import org.pointstone.cugapp.utils.OwnToast;

import im.delight.android.webview.AdvancedWebView;

/**
 * Created by Administrator on 2017/1/6.
 */

public class WebActivity extends SwipeBackAppCompatActivity implements AdvancedWebView.Listener{
    Toolbar mToolbar;
    private AdvancedWebView webView;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mToolbar= (Toolbar) findViewById(R.id.toolbar_setting);
        initToolbar();
        webView= (AdvancedWebView) findViewById(R.id.webview);
        Intent intent = getIntent();
        webView.setListener(this, this);
        String url=intent.getStringExtra("url");
        boolean preventCaching = true;
        webView.loadUrl(url, preventCaching);

    }

    /**
     * 选项菜单
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {

        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setHomeAsUpIndicator(R.drawable.setting_back);

            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        pd=ProgressDialog.show(WebActivity.this, "","加载中......");
        pd.setCancelable(true);
        pd.show();
    }

    @Override
    public void onPageFinished(String url) {
        pd.dismiss();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        OwnToast.Short(description);
        pd.dismiss();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}
