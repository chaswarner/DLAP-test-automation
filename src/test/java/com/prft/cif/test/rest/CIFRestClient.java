package com.prft.cif.test.rest;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.prft.cif.test.crypto.Encryption;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @author Anant Gowerdhan
 */
public class CIFRestClient {

    private final Logger LOGGER = Logger.getLogger(this.getClass());

    private static final String KEY = "PBKDF2WithHmacSHA512";

    private String _restApiURL;
    private String _username;
    private String _password;
    private String _query;
    private String _endpoint;

    public static final String CONTENT_TYPE_JSON = "json";

    /**
     * @param url
     */
    @Inject
    public void setRestApiURL(@Named("rest.api.url") String url) {
        this._restApiURL = url;
    }

    /**
     * @return
     */
    public String getRestApiURL() {
        return _restApiURL;
    }

    /**
     * @param username
     */
    @Inject(optional = true)
    public void setUsername(@Named("rest.user.name") String username) {
        this._username = username;
    }

    /**
     * @return
     */
    public String getUsername() {
        return _username;
    }

    /**
     * @param password
     */
    @Inject(optional = true)
    public void setPasswordEncrypted(@Named("rest.user.password.encrypt") String password) throws Exception {
        Encryption encryption = new Encryption();
        this._password = encryption.decrypt(password, KEY);

    }

    /**
     * @param password
     */
    @Inject(optional = true)
    public void setPasswordPlain(@Named("rest.user.password.plain") String password) {
        this._password = password;
    }

    /**
     * @return
     */
    public String getPassword() {
        return _password;
    }

    /**
     * @return
     */
    public String getQuery() {
        return _query;
    }

    /**
     * @param query
     */
    public void setQuery(String query) {
        this._query = query;
    }

    /**
     * @return
     */
    public String getEndpoint() {
        return _endpoint;
    }

    /**
     * @param endpoint
     */
    public void setEndpoint(String endpoint) {
        this._endpoint = endpoint;
    }

    /**
     * @param body
     * @param contentType
     * @return
     * @throws IOException
     * @throws AuthenticationException
     */
    public String post(String body, String contentType)
            throws IOException, AuthenticationException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        String restURL = getRestApiURL();
        if (getEndpoint() != null)
            restURL += getEndpoint();
        if (getQuery() != null)
            restURL += getQuery();
        HttpPost httpPost = new HttpPost(restURL);

        httpPost.setEntity(new StringEntity(body));
        if (contentType.equals(CONTENT_TYPE_JSON)) {
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
        }
        UsernamePasswordCredentials creds
                = new UsernamePasswordCredentials(getUsername(), getPassword());
        httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));

        CloseableHttpResponse response = client.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());
        client.close();
        return responseBody;
    }

    /**
     * @param body
     * @param contentType
     * @return
     * @throws IOException
     * @throws AuthenticationException
     */
    public String put(String body, String contentType)
            throws IOException, AuthenticationException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        String restURL = getRestApiURL();
        if (getEndpoint() != null)
            restURL += getEndpoint();
        if (getQuery() != null)
            restURL += getQuery();
        LOGGER.debug("Rest URL ==> " + restURL);
        HttpPut httpPut = new HttpPut(restURL);

        httpPut.setEntity(new StringEntity(body));
        if (contentType.equals(CONTENT_TYPE_JSON)) {
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-type", "application/json");
        }
        UsernamePasswordCredentials creds
                = new UsernamePasswordCredentials(getUsername(), getPassword());
        httpPut.addHeader(new BasicScheme().authenticate(creds, httpPut, null));

        CloseableHttpResponse response = client.execute(httpPut);
        String responseBody = EntityUtils.toString(response.getEntity());
        client.close();
        return responseBody;
    }

    /**
     * @return
     * @throws IOException
     * @throws AuthenticationException
     */
    public String get()
            throws IOException, AuthenticationException {
        // CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpClient client = HttpClientBuilder.create().build();
        String restURL = getRestApiURL();
        if (getEndpoint() != null)
            restURL += getEndpoint();
        if (getQuery() != null)
            restURL += getQuery();
        System.out.println(restURL);
        HttpGet httpGet = new HttpGet(restURL);

        UsernamePasswordCredentials creds
                = new UsernamePasswordCredentials(getUsername(), getPassword());
        httpGet.addHeader(new BasicScheme().authenticate(creds, httpGet, null));

        CloseableHttpResponse response = client.execute(httpGet);
        String body = EntityUtils.toString(response.getEntity());
        client.close();
        return body;

    }

}
