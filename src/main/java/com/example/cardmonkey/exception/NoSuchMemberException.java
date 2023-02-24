package com.example.cardmonkey.exception;

public class NoSuchMemberException extends IllegalArgumentException {

    private static final String MESSAGE = "해당 회원 정보를 찾을 수 없습니다.";

    public NoSuchMemberException() {
        super(MESSAGE);
    }
}
