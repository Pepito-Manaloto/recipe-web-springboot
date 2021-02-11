package com.aaron.recipeweb.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.aaron.recipeweb.response.ResponseRecipeListWrapper;

import reactor.core.publisher.Mono;

public interface RecipeService
{
    DateTimeFormatter LAST_UPDATED_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    LocalDateTime DEFAULT_LAST_UPDATED = LocalDateTime.of(1950, 1, 1, 0, 0);

    Mono<ResponseRecipeListWrapper> getRecipes(String lastUpdatedStr);
}
