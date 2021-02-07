package com.aaron.recipeweb.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.aaron.recipeweb.entity.Ingredient;

public interface IngredientRepository extends ReactiveCrudRepository<Ingredient, Integer>
{

}
