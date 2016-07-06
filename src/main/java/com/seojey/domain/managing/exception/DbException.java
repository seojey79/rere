package com.seojey.domain.managing.exception;

/**
 * Created by kimjun on 16. 6. 2..
 */
public class DbException extends RuntimeException {
    public DbException(String msg) {
        super(msg);
    }
}
