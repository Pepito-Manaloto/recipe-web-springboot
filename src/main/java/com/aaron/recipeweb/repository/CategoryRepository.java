package com.aaron.recipeweb.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.aaron.recipeweb.entity.Category;

public interface CategoryRepository extends ReactiveCrudRepository<Category, Integer>
{

}
