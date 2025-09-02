package com.sivalabs.bookmarks.domain;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Category createCategory(Category category) {
        Optional<Category> categoryOptional = this.findByName(category.getName());
        if(categoryOptional.isPresent()) {
            throw new IllegalArgumentException("Category already exists");
        }
        category.setId(null);
        return categoryRepository.save(category);
    }

    @Cacheable("category-by-name")
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByNameEqualsIgnoreCase(name);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
}
