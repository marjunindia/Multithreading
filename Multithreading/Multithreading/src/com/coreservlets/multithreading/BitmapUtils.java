package com.coreservlets.multithreading;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BitmapUtils {
    /** Loads an image from the Web and stores it in a Bitmap, which is suitable for
     *  displaying in an ImageView.
     */
    public static Bitmap bitmapFromUrl(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        InputStream in = urlConnection.getInputStream();
        Bitmap bitmapImage = BitmapFactory.decodeStream(in);
        urlConnection.disconnect();
        return(bitmapImage);
    }
    
    /** Creates a View to show information about the image referenced by urlString. 
     *  Either creates an ImageView that shows the image, or creates a TextView 
     *  that shows an error message.
     */
    public static View viewForImage(String urlString, Context context) {
        Bitmap bitmapImage = null;
        String errorMessage = "";
        try {
            bitmapImage = BitmapUtils.bitmapFromUrl(urlString);
        } catch (MalformedURLException mue) {
            errorMessage = String.format("Malformed URL %s", urlString);
        } catch (IOException ioe) {
            errorMessage = String.format("Error downloading image: %s", ioe.getMessage());
        }
        if (bitmapImage != null) {
            ImageView displayedImage = new ImageView(context);
            displayedImage.setImageBitmap(bitmapImage);
            displayedImage.setPadding(5, 5, 5, 5);
            return(displayedImage);
        } else {
            TextView displayedMessage = new TextView(context);
            displayedMessage.setText(errorMessage);
            return(displayedMessage);
        }
    }
    
    private BitmapUtils() {} // Uninstantiatable class: static methods only
}
