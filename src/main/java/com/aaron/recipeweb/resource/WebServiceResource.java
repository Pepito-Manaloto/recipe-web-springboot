package com.aaron.recipeweb.resource;

import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aaron.recipeweb.entity.Category;
import com.aaron.recipeweb.response.ResponseRecipeListWrapper;
import com.aaron.recipeweb.service.CategoryService;
import com.aaron.recipeweb.service.RecipeService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// TODO: Refactor to restful endpoint, once android is updated
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/web_service")
public class WebServiceResource
{
    private CategoryService categoryService;
    private RecipeService recipeService;

    @GetMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Category> getCategories()
    {
        log.info("getCategories. Start.");

        Flux<Category> response = categoryService.getCategories();

        log.info("getCategories. End.");

        return response;
    }

    // TODO: refactor header to queryparam once android is updated
    @GetMapping(path = "/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseRecipeListWrapper> getRecipes(ServerHttpRequest request)
    {
        log.info("getRecipes. Start.");

        String lastUpdatedStr = request.getHeaders().getFirst("last_updated");

        Mono<ResponseRecipeListWrapper> response = recipeService.getRecipes(lastUpdatedStr);

        log.info("getRecipes. End.");

        return response;
    }
}
