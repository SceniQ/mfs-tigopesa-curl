package com.mfs.client.tigopesa.jar.util;

import java.util.Map;

/**
 * Request carrier used to send request to partner
 */
public class MTInterfaceConRequest {

    private String serviceUrl;

    private String httpmethodName;

    private int port;

    private boolean doOutPut;

    private Map<String, String> headers;

    private String username;

    private String password;

    private String httpmethodType;

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getHttpmethodName() {
        return httpmethodName;
    }

    public void setHttpmethodName(String httpmethodName) {
        this.httpmethodName = httpmethodName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isDoOutPut() {
        return doOutPut;
    }

    public void setDoOutPut(boolean doOutPut) {
        this.doOutPut = doOutPut;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHttpmethodType() {
        return httpmethodType;
    }

    public void setHttpmethodType(String httpmethodType) {
        this.httpmethodType = httpmethodType;
    }

}
