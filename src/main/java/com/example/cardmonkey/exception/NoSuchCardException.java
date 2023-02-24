package com.example.cardmonkey.exception;

public class NoSuchCardException extends IllegalArgumentException {

    private static final String MESSAGE = "해당 카드 정보를 찾을 수 없습니다.";

    public NoSuchCardException() {
        super(MESSAGE);
    }
}
