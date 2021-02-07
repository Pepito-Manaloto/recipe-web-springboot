package com.aaron.recipeweb.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

import com.aaron.recipeweb.entity.Recipe;
import com.aaron.recipeweb.entity.RecipeCount;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeRepository extends ReactiveSortingRepository<Recipe, Integer>
{
    Flux<Recipe> findAllByAuthorAndCategoryId(String author, Integer categoryId, Pageable pageable);

    Mono<Recipe> findByTitle(String title);

    Mono<Integer> countByUpdatedDateGreaterThan(LocalDateTime updatedDate);

    // Automatic Native
    @Query("SELECT c.name AS category, r.author AS author, COUNT(r.id) AS count FROM categories c JOIN recipe r ON c.id = r.category_id GROUP BY c.name HAVING r.author = :author")
    Flux<RecipeCount> countByAuthorGroupByCategory(String author);

    @Modifying
    Mono<Integer> deleteByTitleAndAuthor(String title, String author);
}
