package com.aaron.recipeweb.service;

import com.aaron.recipeweb.entity.Category;

import reactor.core.publisher.Flux;

public interface CategoryService
{
    Flux<Category> getCategories();
}
