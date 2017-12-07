package com.coreservlets.multithreading;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UrlCheckerActivity extends Activity {
    private LinearLayout mResultsRegion;
    private EditText mUrlsToTest;
    private int mGoodUrlColor, mForwardedUrlColor, mBadUrlColor;
    private Drawable mGoodUrlIcon, mForwardedUrlIcon, mBadUrlIcon;
    List<UrlCheckerResult> mResultsHolder = new ArrayList<UrlCheckerResult>();
    private final static int HEAD_TIMEOUT = 10;
    private float mResultTextSize;
    private int mResultPaddingSize;
    
    /** Initializes the app when it is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.url_checker);
        mUrlsToTest = (EditText)findViewById(R.id.urls_to_test);
        mResultsRegion = (LinearLayout)findViewById(R.id.results_region);
        Resources resources = getResources();
        mGoodUrlColor = resources.getColor(R.color.url_good);
        mForwardedUrlColor = resources.getColor(R.color.url_forwarded);
        mBadUrlColor = resources.getColor(R.color.url_bad);
        mGoodUrlIcon = resources.getDrawable(R.drawable.emo_im_happy);
        mForwardedUrlIcon = resources.getDrawable(R.drawable.emo_im_wtf);
        mBadUrlIcon = resources.getDrawable(R.drawable.emo_im_sad);
        mResultTextSize = resources.getDimension(R.dimen.url_checker_result_size);
        mResultPaddingSize = (int)resources.getDimension(R.dimen.url_checker_padding_size);
    }

    public void checkUrls(View clickedButton) {
        mResultsRegion.removeAllViews();
        mResultsRegion.requestLayout();
        String[] urls = mUrlsToTest.getText().toString().split("\\s+"); // \s means whitespace
        ExecutorService taskList = Executors.newFixedThreadPool(50);
        for (String url: urls) {
            UrlCheckerResult resultHolder = new UrlCheckerResult(url);
            mResultsHolder.add(resultHolder);
            taskList.execute(new UrlTester(resultHolder));
        }
        try {
            taskList.shutdown(); // Prohibits new tasks but does not stop or remove existing ones.
            taskList.awaitTermination(HEAD_TIMEOUT, TimeUnit.SECONDS); // 10 seconds or all tasks done, whichever is first
        } catch (InterruptedException e) {}
        for(UrlCheckerResult resultHolder: mResultsHolder) {
            UrlChecker result = resultHolder.getUrlResult();
            if (result == null) {
                result = 
                    new UrlChecker(resultHolder.getUrlString(), HEAD_TIMEOUT);
            }
            TextView displayedResult = new TextView(this);
            displayedResult.setTextColor(chooseColor(result));
            displayedResult.setTextSize(mResultTextSize);
            displayedResult.setText(result.toString());
            displayedResult.setCompoundDrawablesWithIntrinsicBounds(chooseIcon(result), null, null, null);
            displayedResult.setCompoundDrawablePadding(mResultPaddingSize);
            mResultsRegion.addView(displayedResult);
        }
    }
    
    private class UrlTester implements Runnable {
        private UrlCheckerResult mResultHolder;
        
        public UrlTester(UrlCheckerResult resultHolder) {
            mResultHolder = resultHolder;
        }
        
        public void run() {
            UrlChecker result = new UrlChecker(mResultHolder.getUrlString());
            mResultHolder.setUrlResult(result);
        }
    }
    
    private int chooseColor(UrlChecker urlResult) {
        if (urlResult.isGood()) {
            return(mGoodUrlColor);
        } else if (urlResult.isForwarded()) {
            return(mForwardedUrlColor);
        } else {
            return(mBadUrlColor);
        }
    }
    
    private Drawable chooseIcon(UrlChecker urlResult) {
        if (urlResult.isGood()) {
            return(mGoodUrlIcon);
        } else if (urlResult.isForwarded()) {
            return(mForwardedUrlIcon);
        } else {
            return(mBadUrlIcon);
        }
    }
}