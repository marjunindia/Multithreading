package com.coreservlets.multithreading;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class UrlChecker {
    private String mUrlString, mServerMessage, mForwardedLocation;
    private StatusLineParser mStatusParser;
    private int mStatusCode;
    private static final int DEFAULT_HTTP_PORT = 80;
    
    public UrlChecker(String urlString) {
        mUrlString = urlString;
        String host = "(Unknown)";
        try {
            URL url = new URL(urlString);
            host = url.getHost();
            int port = getPort(url);
            String uri = getUri(url);
            System.out.println("URI is " + uri);
            Socket socket = new Socket(host, port);
            PrintWriter out = SocketUtils.getWriter(socket);
            BufferedReader in = SocketUtils.getReader(socket);
            out.printf("HEAD %s HTTP/1.1\r\n", uri);
            out.printf("Host: %s\r\n", host);
            out.printf("Connection: close\r\n\r\n");
            String serverResult = in.readLine();
            mStatusParser = new StatusLineParser(serverResult);
            mStatusCode = mStatusParser.getStatusCode();
            mServerMessage = mStatusParser.getMessage();
            if (mStatusParser.isForwarded()) {
                mForwardedLocation = location(in);
            }
            socket.close();
        } catch(MalformedURLException mfe) {
            mStatusCode = -1;
            mServerMessage = String.format("Illegal URL format");
        } catch (UnknownHostException uhe) {
            mStatusCode = -2;
            mServerMessage = String.format("Unknown host '%s'", host);
        } catch (IOException ioe) {
            mStatusCode = -3;
            mServerMessage = String.format("IOException '%s'", ioe.getMessage());
        }
    }
    
    public UrlChecker(String urlString, int timeoutThatWasExceeded) {
        mUrlString = urlString;
        mStatusCode = -4;
        mServerMessage =
            String.format("No server response within %s seconds",
                          timeoutThatWasExceeded);
    }
    
    public String getUrlString() {
        return(mUrlString);
    }

    public String getServerMessage() {
        return(mServerMessage);
    }

    public String getForwardedLocation() {
        return(mForwardedLocation);
    }

    public int getStatusCode() {
        return(mStatusCode);
    }
    
    /** Returns true if HEAD request succeeded and status code is in good range (200's) */
    
    public boolean isGood() {
        return((mStatusParser != null) && mStatusParser.isGood());
    }
    
    /** Returns true if HEAD request succeeded and status code is 301 or 302 */
    
    public boolean isForwarded() {
        return((mStatusParser != null) && mStatusParser.isForwarded());
    }
    
    /** Returns true if HEAD request failed or status code is bad (not 200's or 301/302) */
    
    public boolean isBad() {
        return(!isGood() && !isForwarded());
    }

    /** Returns the port of the URL. We don't need to distinguish between
     *  an explicit user-set 80 and an omitted port (where will will also use 80).
     */
    private int getPort(URL url) {
        int userPort = url.getPort();
        if (userPort == -1) {
            return(DEFAULT_HTTP_PORT);
        } else {
            return(userPort);
        } 
    }
    
    /** Returns the URI to send in the HEAD request. If there is nothing
     *  after the hostname in the URL, then the URI should be "/".
     *  Note that getFile can return empty string but not null.
     *  Also note that we compare length to 0 instead of using isEmpty
     *  because isEmpty is not supported on Android.
     */
    private String getUri(URL url) {
        String userUri = url.getFile();
        if (userUri.trim().length() == 0) {
            return("/");
        } else {
            return(userUri);
        }
    }

    private String location(BufferedReader in) throws IOException {
        String line;
        while((line = in.readLine()) != null) {
            if (line.toUpperCase().startsWith("LOCATION")) {
                String[] results = line.split("\\s+", 2);
                return(results[1]);
            }
        }
        return("(Unknown Location)");
    }
    
    @Override
    public String toString() {
        String result;
        if (mStatusParser == null) {
            result = String.format("%s%ncannot be tested: %s", 
                                   mUrlString, mServerMessage);
        } else if (mStatusParser.isGood()) {
            result = String.format("%s%nis GOOD: %s -- %s", 
                                   mUrlString, mStatusCode, mServerMessage);
        } else if (mStatusParser.isForwarded()) {
            result = String.format("%s%nis FORWARDED to%n%s", 
                                   mUrlString, mForwardedLocation);
        } else {
            result = String.format("%s%nis BAD: %s -- %s", 
                                   mUrlString, mStatusCode, mServerMessage);
        }
        return (result);
    }
}
