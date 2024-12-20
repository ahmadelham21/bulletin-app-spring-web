package com.finshot.bulletin.dto.request;


public class DeleteRequest {
    private Long id;
    private String password;

    public DeleteRequest(Long id, String password) {
        this.id = id;
        this.password = password;
    }

    public DeleteRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
