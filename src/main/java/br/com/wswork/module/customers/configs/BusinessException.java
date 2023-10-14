package br.com.wswork.module.customers.configs;

public class BusinessException extends RuntimeException{

    private final int statusCode;
    private final String statusText;
    private final String message;

    public BusinessException(final int statusCode, final String statusText, final String message) {
        super(message);
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }
}
