package com.example.fileapp.util;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UUIDTest {

    @Test
    public void rolling_test() {
        UUID uuid = UUID.randomUUID(); // 랜덤 해시값 리턴
        String value = uuid.toString();
        System.out.println(value);
    }
}