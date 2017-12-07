package com.coreservlets.multithreading;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageDownloader1Activity extends Activity {
    protected LinearLayout mResultsRegion;
    protected EditText mImagesToDownload;

    
    /** Initializes the app when it is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_downloader);
        mImagesToDownload = (EditText)findViewById(R.id.images_to_download);
        mResultsRegion = (LinearLayout)findViewById(R.id.results_region);
    }

    public void downloadImages(View clickedButton) {
        mResultsRegion.removeAllViews();
        mResultsRegion.requestLayout();
        String[] images = mImagesToDownload.getText().toString().split("\\s+"); // \s means whitespace
        addImages(images);
    }
    
    protected void addImages(String[] images) {
        ExecutorService taskList = Executors.newFixedThreadPool(50);
        for (String image: images) {
            taskList.execute(new ImageDownloader(image));
        }
    }
    
    private class ImageDownloader implements Runnable {
        private String mImageUrl;
        
        public ImageDownloader(String imageUrl) {
            mImageUrl = imageUrl;
        }
        
        /** You can make the View to display the image (or to display an error message)
         *  directly here in the run method. However, you canNOT add that View to the
         *  display here, since background threads are prohibited from changing the UI.
         *  So, we use post to tell the UI thread to add the View.
         */
        public void run() {
            View viewToAdd = 
                BitmapUtils.viewForImage(mImageUrl, 
                                         ImageDownloader1Activity.this);
            mResultsRegion.post(new ViewAdder(viewToAdd));
        }
    }
    
    private class ViewAdder implements Runnable {
        private View mViewToAdd;
        
        public ViewAdder(View viewToAdd) {
            mViewToAdd = viewToAdd;
        }
        
        public void run() {
            mResultsRegion.addView(mViewToAdd);
        }
    }
}