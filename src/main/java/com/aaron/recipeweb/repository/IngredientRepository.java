package com.aaron.recipeweb.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.aaron.recipeweb.entity.Ingredient;

import reactor.core.publisher.Flux;

public interface IngredientRepository extends ReactiveCrudRepository<Ingredient, Integer>
{
    Flux<Ingredient> findAllByRecipeId(Integer recipeId);
}
