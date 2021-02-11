package com.aaron.recipeweb.resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aaron.recipeweb.entity.Category;
import com.aaron.recipeweb.service.CategoryService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/web_service")
public class WebServiceResource
{
    private CategoryService categoryService;

    @GetMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Category> getCategories()
    {
        log.info("getCategories. Start.");

        Flux<Category> response = categoryService.getCategories();

        log.info("getCategories. End.");

        return response;
    }
}
