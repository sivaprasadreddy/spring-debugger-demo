package com.sivalabs.bookmarks.domain;

import com.sivalabs.bookmarks.TestcontainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfig.class)
class CategoryRepositoryTests {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void shouldFindByNameEqualsIgnoreCase() {
        Optional<Category> java = categoryRepository.findByNameEqualsIgnoreCase("java");
        assertThat(java).isPresent();
    }
}