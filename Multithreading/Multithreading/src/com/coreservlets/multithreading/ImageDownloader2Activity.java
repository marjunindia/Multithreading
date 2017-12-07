package com.coreservlets.multithreading;

import android.os.AsyncTask;
import android.view.View;

public class ImageDownloader2Activity extends ImageDownloader1Activity {
    @Override
    protected void addImages(String[] images) {
        for (String image: images) {
            ImageDownloadTask task = new ImageDownloadTask();
            task.execute(image);
        }
    }

    private class ImageDownloadTask extends AsyncTask<String, Void, View> {
        @Override
        public View doInBackground(String... urls) {
           return(BitmapUtils.viewForImage(urls[0], 
                                           ImageDownloader2Activity.this));
        }
        
        @Override
        public void onPostExecute(View viewToAdd) {
            mResultsRegion.addView(viewToAdd);
        }
    }
}