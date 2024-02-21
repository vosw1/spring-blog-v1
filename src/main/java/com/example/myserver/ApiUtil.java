package com.example.myserver;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiUtil<T> {
    private int status;
    private String msg;
    private T body;
}