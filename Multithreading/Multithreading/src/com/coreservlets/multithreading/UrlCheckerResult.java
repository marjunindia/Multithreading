package com.coreservlets.multithreading;

public class UrlCheckerResult {
    private String mUrlString;
    private UrlChecker mUrlResult;
    
    public UrlCheckerResult(String urlString) {
        this.mUrlString = urlString;
    }
    
    public String getUrlString() {
        return(mUrlString);
    }
    
    public UrlChecker getUrlResult() {
        return(mUrlResult);
    }
    
    public void setUrlResult(UrlChecker urlResult) {
        this.mUrlResult = urlResult;
    }
}
