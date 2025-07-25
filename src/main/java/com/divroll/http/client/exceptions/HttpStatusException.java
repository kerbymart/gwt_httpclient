package com.divroll.http.client.exceptions;

public class HttpStatusException extends Exception {
    private final int statusCode;
    private final String statusText;
    private final String responseBody;

    public HttpStatusException(int statusCode, String statusText, String responseBody) {
        super("HTTP " + statusCode + " " + statusText);
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.responseBody = responseBody;
    }

    public int getStatusCode() { return statusCode; }
    public String getStatusText() { return statusText; }
    public String getResponseBody() { return responseBody; }
}