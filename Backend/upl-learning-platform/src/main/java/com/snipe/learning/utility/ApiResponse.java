package com.snipe.learning.utility;


public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private Integer totalItems;

    public ApiResponse() {}

    public ApiResponse(boolean success, String message, T data, Integer totalItems) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.totalItems = totalItems;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
