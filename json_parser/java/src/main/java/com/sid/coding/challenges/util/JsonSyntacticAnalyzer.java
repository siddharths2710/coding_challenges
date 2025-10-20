package com.sid.coding.challenges.util;

import java.util.Stack;

import org.json.JSONObject;
import org.json.JSONArray;

import com.sid.coding.challenges.exceptions.JsonParseException;

public final class JsonSyntacticAnalyzer
{

    public enum InstanceType
    {
        JSON_OBJECT,
        JSON_ARRAY,
        NOT_JSON
    }

    public static InstanceType determineInstanceType(JsonToken tipToken)
    {
        InstanceType instanceType = InstanceType.NOT_JSON;

        if (tipToken.getType() == JsonType.RIGHT_BRACE)
        {
            instanceType = InstanceType.JSON_OBJECT;
        }
        else if (tipToken.getType() == JsonType.RIGHT_SQUARE_BRACKET)
        {
            instanceType = InstanceType.JSON_ARRAY;
        }

        return instanceType;
    }

    public static JSONObject parseTokensAndGetJsonObject(Stack<JsonToken> tokens)
       throws JsonParseException
    {

        if (tokens.isEmpty())
        {
            throw new JsonParseException("Empty tokens stack");
        }

        JsonToken token = tokens.pop();
        if (token.getType() != JsonType.RIGHT_BRACE)
        {
            throw new JsonParseException("Expected '}' at the end of the JSON object string");
        }

        final JSONObject jsonObject = new JSONObject();
        while (!tokens.isEmpty() &&
           tokens.peek().getType() != JsonType.LEFT_BRACE)
        {
            String key = null;
            Object value = null;
            boolean isKey = false;
            boolean endParsing = false;

            token = tokens.pop();

            switch(token.getType())
            {
                //Here we don't expect a Left bracket
                case COMMA_SEPARATOR:
                    isKey = false;
                    break;
                case STRING:
                    if (isKey)
                        key = token.getValue();
                    else
                        value = token.getValue();
                    break;
                case COLON_SEPARATOR:
                    isKey = true;
                    break;
                case RIGHT_BRACE:
                    value = parseTokensAndGetJsonObject(tokens);
                    break;
                case RIGHT_SQUARE_BRACKET:
                    value = parseTokensAndGetJsonArray(tokens);
                    break;
                case LEFT_SQUARE_BRACKET:
                    throw new JsonParseException("Expected ']' after the key");
                case LEFT_BRACE:
                    endParsing = true;
                    break;
                case BOOLEAN:
                    value = Boolean.parseBoolean(token.getValue());
                    break;
                case NULL:
                    value = null;
                    break;
                case NUMBER:
                    value = token.getValue();
                    break;
            }

            if (endParsing)
            {
                if (key == null)
                {
                    throw new JsonParseException("Expected a key before the value");
                }
                else
                {
                    jsonObject.put(key, value);
                }
            }

        }

        return jsonObject;
    }

    public static JSONArray parseTokensAndGetJsonArray(Stack<JsonToken> tokens)
       throws JsonParseException
    {
        if (tokens.isEmpty())
        {
            throw new JsonParseException("Empty tokens stack");
        }

        JsonToken token = null;
        final JSONArray jsonArray = new JSONArray();
        while (!tokens.isEmpty() && tokens.peek().getType() != JsonType.LEFT_SQUARE_BRACKET)
        {
            token = tokens.pop();

            switch(token.getType())
            {
                case COMMA_SEPARATOR:
                    break;
                case STRING:
                case NUMBER:
                case BOOLEAN:
                    jsonArray.put(token.getValue());
                    break;
                case NULL:
                    jsonArray.put(JSONObject.NULL);
                    break;
                case LEFT_SQUARE_BRACKET:
                    throw new JsonParseException("Expected ']' after the value");
                case LEFT_BRACE:
                    throw new JsonParseException("Expected ']' after the value");
                case RIGHT_SQUARE_BRACKET:
                    jsonArray.put(parseTokensAndGetJsonArray(tokens));
                    break;
                case RIGHT_BRACE:
                    jsonArray.put(parseTokensAndGetJsonObject(tokens));
                    break;
            }
        }

        if (tokens.isEmpty() ||
            tokens.peek().getType() != JsonType.LEFT_SQUARE_BRACKET)
        {
            throw new JsonParseException("Expected ']' at the end of the JSON array string");
        }

        return jsonArray;
    }
}
