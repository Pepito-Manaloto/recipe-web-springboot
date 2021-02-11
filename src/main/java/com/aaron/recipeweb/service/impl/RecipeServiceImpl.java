package com.aaron.recipeweb.service.impl;

import static com.aaron.recipeweb.constant.ResponseMessage.CATEGORIES_NOT_FOUND;
import static com.aaron.recipeweb.constant.ResponseMessage.INGREDIENTS_NOT_FOUND;
import static com.aaron.recipeweb.constant.ResponseMessage.INSTRUCTIONS_NOT_FOUND;
import static com.aaron.recipeweb.constant.ResponseMessage.RECIPES_NOT_FOUND;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aaron.recipeweb.entity.Category;
import com.aaron.recipeweb.entity.Ingredient;
import com.aaron.recipeweb.entity.Instruction;
import com.aaron.recipeweb.entity.Recipe;
import com.aaron.recipeweb.exception.NotFoundException;
import com.aaron.recipeweb.repository.CategoryRepository;
import com.aaron.recipeweb.repository.IngredientRepository;
import com.aaron.recipeweb.repository.InstructionRepository;
import com.aaron.recipeweb.repository.RecipeRepository;
import com.aaron.recipeweb.response.ResponseRecipe;
import com.aaron.recipeweb.response.ResponseRecipeListWrapper;
import com.aaron.recipeweb.service.RecipeService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService
{
    @Value("${log.reactor.level}")
    private String logLevel;

    private RecipeRepository recipeRepository;
    private CategoryRepository categoryRepository;
    private IngredientRepository ingredientRepository;
    private InstructionRepository instructionRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository, CategoryRepository categoryRepository, IngredientRepository ingredientRepository,
            InstructionRepository instructionRepository)
    {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.instructionRepository = instructionRepository;
    }

    @Override
    public Mono<ResponseRecipeListWrapper> getRecipes(String lastUpdatedStr)
    {
        log.info("getRecipes. Start. lastUpdatedStr={}", lastUpdatedStr);
        LocalDateTime lastUpdated = parseLastUpdated(lastUpdatedStr);

        log.info("getRecipes. Retrieving from database. lastUpdated={}", lastUpdated);

        return recipeRepository.countByUpdatedDateGreaterThan(lastUpdated)
                .log("", Level.FINE)
                .flatMap(this::responseRecipeList);
    }

    private LocalDateTime parseLastUpdated(String lastUpdatedStr)
    {
        LocalDateTime lastUpdated;
        if(StringUtils.isNotBlank(lastUpdatedStr))
        {
            lastUpdated = LocalDateTime.parse(lastUpdatedStr, LAST_UPDATED_FORMATTER);
        }
        else
        {
            lastUpdated = DEFAULT_LAST_UPDATED;
        }

        return lastUpdated;
    }

    // Get categories, then use that to get all recipes, then convert the list of ResponseRecipe into ResponseRecipeListWrapper
    private Mono<ResponseRecipeListWrapper> responseRecipeList(Integer recipeCount)
    {
        return findCategories().flatMap(this::listOfResponseRecipe)
                .map(list -> toResponseRecipeListWrapper(recipeCount, list))
                .log("", Level.FINE);
    }

    // Find then convert to a Map<recipe_id, name>
    private Mono<Map<Integer, String>> findCategories()
    {
        Function<Category, Integer> categoryToRecipeId = c -> c.getId();
        Function<Category, String> categoryToCategoryName = c -> c.getName();

        return categoryRepository.findAll()
                .switchIfEmpty(Mono.error(new NotFoundException(CATEGORIES_NOT_FOUND)))
                .collectMap(categoryToRecipeId, categoryToCategoryName)
                .log("", Level.FINE);
    }

    // For each recipe, we will call aggregateRecipeWithIngredientsAndInstructions, which will then convert all results into ResponseRecipe
    private Mono<List<ResponseRecipe>> listOfResponseRecipe(Map<Integer, String> categoryNameIdentifiedById)
    {
        return recipeRepository.findAll()
                .switchIfEmpty(Mono.error(new NotFoundException(RECIPES_NOT_FOUND)))
                .flatMap(r -> aggregateRecipeWithIngredientsAndInstructions(r, categoryNameIdentifiedById))
                .collectList()
                .log("", Level.FINE);
    }

    // Zip will retrieve both at the same time and aggregate the result to a Tuple<T1, T2>, then proceed to create ResponseRecipe
    private Flux<ResponseRecipe> aggregateRecipeWithIngredientsAndInstructions(Recipe r, Map<Integer, String> categoryNameIdentifiedById)
    {
        return Flux.zip(getIngredientsByRecipeId(r), getInstructionsByRecipeId(r))
                .map(tuple -> toResponseRecipe(r, categoryNameIdentifiedById, tuple));
    }

    private Mono<List<Ingredient>> getIngredientsByRecipeId(Recipe r)
    {
        return ingredientRepository.findAllByRecipeId(r.getRecipeId())
                .switchIfEmpty(Mono.error(new NotFoundException(INGREDIENTS_NOT_FOUND, r.getTitle())))
                .collectList()
                .log("", Level.FINE);
    }

    private Mono<List<Instruction>> getInstructionsByRecipeId(Recipe r)
    {
        return instructionRepository.findAllByRecipeId(r.getRecipeId())
                .switchIfEmpty(Mono.error(new NotFoundException(INSTRUCTIONS_NOT_FOUND, r.getTitle())))
                .collectList()
                .log("", Level.FINE);
    }

    private ResponseRecipe toResponseRecipe(Recipe r, Map<Integer, String> categoryNameIdentifiedById,
            Tuple2<List<Ingredient>, List<Instruction>> ingredientsAndInstructions)
    {
        return ResponseRecipe.builder()
                .category(categoryNameIdentifiedById.get(r.getCategoryId()))
                .title(r.getTitle())
                .preparationTime(r.getPreparationTime())
                .servings(r.getServings())
                .description(r.getDescription())
                .ingredients(ingredientsAndInstructions.getT1())
                .instructions(ingredientsAndInstructions.getT2())
                .build();
    }

    private ResponseRecipeListWrapper toResponseRecipeListWrapper(Integer recipeCount, List<ResponseRecipe> responseRecipeList)
    {
        return ResponseRecipeListWrapper.builder()
                .recentlyAddedCount(recipeCount)
                .recipes(responseRecipeList)
                .build();
    }
}
