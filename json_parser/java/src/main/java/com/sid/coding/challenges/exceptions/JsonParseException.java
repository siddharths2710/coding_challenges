package com.sid.coding.challenges.exceptions;

public class JsonParseException extends Exception {

    public JsonParseException(String message)
    {
        super(message);
    }

    public JsonParseException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public JsonParseException(Throwable cause)
    {
        super(cause);
    }
}
