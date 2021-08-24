package com.atmDispense.error;

public class ATMCashDispenserException extends Exception{

    public ATMCashDispenserException() {
        super();
    }

    public ATMCashDispenserException(String message) {
        super(message);
    }

    public ATMCashDispenserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ATMCashDispenserException(Throwable cause) {
        super(cause);
    }

    protected ATMCashDispenserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
