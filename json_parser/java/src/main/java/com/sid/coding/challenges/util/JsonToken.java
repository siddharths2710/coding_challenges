package com.sid.coding.challenges.util;

public class JsonToken
{
    private JsonType type;
    private String value;

    public JsonToken(JsonType type, String value)
    {
        this.type = type;
        this.value = value;
    }

    public JsonType getType()
    {
        return type;
    }

    public String getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return "JsonToken [type=" + type + ", value=" + value + "]";
    }
}