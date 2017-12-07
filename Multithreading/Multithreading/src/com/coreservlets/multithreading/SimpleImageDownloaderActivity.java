package com.coreservlets.multithreading;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SimpleImageDownloaderActivity extends Activity {
    private LinearLayout mMainWindow;
    private EditText mImageToDownload;
    private View mViewForImage;
    
    /** Initializes the app when it is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_image_downloader);
        mMainWindow = (LinearLayout)findViewById(R.id.main_window);
        mImageToDownload = (EditText)findViewById(R.id.image_to_download);
    }
    
    public void downloadImage(View clickedButton) {
        if (mViewForImage != null) {
            mMainWindow.removeView(mViewForImage);
        }
        String imageUrl = mImageToDownload.getText().toString();
        mViewForImage = BitmapUtils.viewForImage(imageUrl, this);
        mMainWindow.addView(mViewForImage);
    }
}