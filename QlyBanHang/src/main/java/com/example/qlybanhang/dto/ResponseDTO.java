package com.example.qlybanhang.dto;

public class ResponseDTO<T> {
    private int total;
    private int page;
    private String errors;
    private T data;
}
