package carnival.gusac.com.gusaccarnival40.utils;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/**
 * Created by Messi10 on 28-Feb-15.
 */

/*
Override the Apache HttpEntityEnclosingRequestBase and return the Get method
for requesting the server.
 */
public class HttpGetEntity extends HttpEntityEnclosingRequestBase {
    private static final String REQUEST_MODE = "GET";

    public HttpGetEntity(String url) {
        super();
        setURI(URI.create(url));
    }

    @Override
    public String getMethod() {
        return REQUEST_MODE;
    }
}
