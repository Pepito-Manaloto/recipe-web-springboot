package com.aaron.recipeweb.service.impl;

import static com.aaron.recipeweb.constant.ResponseMessage.CATEGORIES_NOT_FOUND;

import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aaron.recipeweb.annotation.TransactionalRead;
import com.aaron.recipeweb.entity.Category;
import com.aaron.recipeweb.exception.NotFoundException;
import com.aaron.recipeweb.repository.CategoryRepository;
import com.aaron.recipeweb.service.CategoryService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService
{
    @Value("${log.reactor.level}")
    private String logLevel;

    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    @TransactionalRead
    @Override
    public Flux<Category> getCategories()
    {
        log.info("getCategories. Retrieving from database.");

        return categoryRepository.findAll()
                .log("", Level.parse(logLevel))
                .switchIfEmpty(Mono.error(new NotFoundException(CATEGORIES_NOT_FOUND)));
    }
}
