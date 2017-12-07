package com.coreservlets.multithreading;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/** Compares and contrasts a number of different approaches to
 *  multithreaded programming.
 *  <p>
 *  From <a href="http://www.coreservlets.com/android-tutorial/">
 *  the coreservlets.com Android programming tutorial series</a>.
 */
public class MultithreadingInitialActivity extends Activity {
    /** Initializes the app when it is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    /** Switches to the ThreadingBasicsActivity when the associated button is clicked. 
     *  You must also register the new Activity in AndroidManifest.xml. 
     */
    public void switchToThreadingBasics(View clickedButton) {
        ActivityUtils.goToActivity(this, ThreadingBasicsActivity.class);
    }
    
    /** Switches to the RaceConditionsActivity when the associated button is clicked. 
     */
    public void switchToRaceConditions(View clickedButton) {
        ActivityUtils.goToActivity(this, RaceConditionsActivity.class);
    }
    
    /** Switches to the ThreadSafeActivity when the associated button is clicked. 
     */
    public void switchToThreadSafe(View clickedButton) {
        ActivityUtils.goToActivity(this, ThreadSafeActivity.class);
    }
    
    /** Switches to the UrlCheckerActivity when the associated button is clicked. 
     */
    public void switchToUrlChecker(View clickedButton) {
        ActivityUtils.goToActivity(this, UrlCheckerActivity.class);
    }
  
    /** Switches to the SimpleImageDownloaderActivity when the associated button is clicked. 
     */
    public void switchToSimpleImageDownloader(View clickedButton) {
        ActivityUtils.goToActivity(this, SimpleImageDownloaderActivity.class);
    }
    
    /** Switches to the ImageDownloader1Activity when the associated button is clicked. 
     */
    public void switchToImageDownloader1(View clickedButton) {
        ActivityUtils.goToActivity(this, ImageDownloader1Activity.class);
    }
    
    /** Switches to the ImageDownloader2Activity when the associated button is clicked. 
     */
    public void switchToImageDownloader2(View clickedButton) {
        ActivityUtils.goToActivity(this, ImageDownloader2Activity.class);
    }
}