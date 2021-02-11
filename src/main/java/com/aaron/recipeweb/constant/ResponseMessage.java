package com.aaron.recipeweb.constant;

public enum ResponseMessage
{
    RECIPES_NOT_FOUND("recipes table is empty."),
    CATEGORIES_NOT_FOUND("categories table is empty."),
    INGREDIENTS_NOT_FOUND("ingredients not found for %s."),
    INSTRUCTIONS_NOT_FOUND("instructions not found for %s.") ;

    private String message;

    private ResponseMessage(String message)
    {
        this.message = message;
    }

    public String getMessage(Object... replacements)
    {
        return String.format(message, replacements);
    }
}
