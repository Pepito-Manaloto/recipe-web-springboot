package com.aaron.recipeweb.service.impl;

import org.springframework.stereotype.Service;

import com.aaron.recipeweb.annotation.TransactionalRead;
import com.aaron.recipeweb.entity.Category;
import com.aaron.recipeweb.repository.CategoryRepository;
import com.aaron.recipeweb.service.CategoryService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService
{
    private CategoryRepository categoryRepository;

    @TransactionalRead
    @Override
    public Flux<Category> getCategories()
    {
        log.info("getCategories. Retrieving from database.");

        return categoryRepository.findAll();
    }

}
