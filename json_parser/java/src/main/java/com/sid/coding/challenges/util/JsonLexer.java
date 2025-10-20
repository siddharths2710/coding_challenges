package com.sid.coding.challenges.util;

import java.io.Reader;

import java.util.Stack;

import com.sid.coding.challenges.exceptions.JsonParseException;

public final class JsonLexer
{
    public static Stack<JsonToken> lex(String json) throws JsonParseException
    {

        if (json == null || json.isEmpty())
        {
            throw new JsonParseException("Null or Empty JSON string");
        }

        final Stack<JsonToken> tokens = new Stack<>();
        final char[] chars = json.toCharArray();
        final StringBuilder stringBuilder = new StringBuilder();

        if (chars.length <= 1 || (chars[0] != '{' && chars[0] != '['))
        {
            throw new JsonParseException("Invalid JSON content to parse");
        }

        for (int i = 0; i < chars.length; i++)
        {
            final char c = chars[i];
            switch (c)
            {
                case '{':
                    tokens.push(new JsonToken(JsonType.LEFT_BRACE, "{"));
                    break;
                case '}':
                    tokens.push(new JsonToken(JsonType.RIGHT_BRACE, "}"));
                    break;
                case '[':
                    tokens.push(new JsonToken(JsonType.LEFT_SQUARE_BRACKET, "["));
                    break;
                case ']':
                    tokens.push(new JsonToken(JsonType.RIGHT_SQUARE_BRACKET, "]"));
                    break;
                case ':':
                    tokens.push(new JsonToken(JsonType.COLON_SEPARATOR, ":"));
                    break;
                case ',':
                    tokens.push(new JsonToken(JsonType.COMMA_SEPARATOR, ","));
                    break;
                case '"':
                    stringBuilder.setLength(0);
                    while (i < chars.length && chars[i] != '"')
                    {
                        stringBuilder.append(chars[i]);
                        i++;
                    }
                    if (i == chars.length)
                    {
                        throw new JsonParseException("Found Unterminated string inside JSON content");
                    }
                    if (stringBuilder.length() == 0)
                    {
                        tokens.push(new JsonToken(JsonType.STRING, ""));
                    }
                    else
                    {
                        tokens.push(new JsonToken(JsonType.STRING, stringBuilder.toString()));
                    }

                    break;
                case 't':
                    if (i + 3 < chars.length && chars[i + 1] == 'r' &&
                        chars[i + 2] == 'u' && chars[i + 3] == 'e')
                    {
                        tokens.push(new JsonToken(JsonType.BOOLEAN, "true"));
                        i += 3;
                    }
                    else
                    {
                        throw new JsonParseException("Found invalid boolean value inside JSON content");
                    }
                    break;
                case 'f':
                    if (i + 4 < chars.length && chars[i + 1] == 'a' &&
                        chars[i + 2] == 'l' && chars[i + 3] == 's' && chars[i + 4] == 'e')
                    {
                        tokens.push(new JsonToken(JsonType.BOOLEAN, "false"));
                        i += 4;
                    }
                    else
                    {
                        throw new JsonParseException("Found invalid boolean value inside JSON content");
                    }
                    break;
                case 'n':
                    if (i + 3 < chars.length && chars[i + 1] == 'u' &&
                        chars[i + 2] == 'l' && chars[i + 3] == 'l')
                    {
                        tokens.push(new JsonToken(JsonType.NULL, "null"));
                        i += 3;
                    }
                    else
                    {
                        throw new JsonParseException("Found invalid null value inside JSON content");
                    }
                    break;
            }

        }
        return tokens;
    }
}
