package com.commons.exception;

public class DbException extends Exception {
    public DbException() {
    }

    public DbException(String reason) {
        super(reason);
    }

    public DbException(Throwable cause) {
        super(cause);
    }

    public DbException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
