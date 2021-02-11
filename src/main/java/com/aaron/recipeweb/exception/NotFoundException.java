package com.aaron.recipeweb.exception;

import com.aaron.recipeweb.constant.ResponseMessage;

public class NotFoundException extends Exception
{
    private static final long serialVersionUID = -1952133099926608731L;

    public NotFoundException(ResponseMessage message, Object... replacements)
    {
        super(message.getMessage(replacements));
    }
}
