package com.sivalabs.bookmarks.domain;

import com.sivalabs.bookmarks.TestcontainersConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.TransactionSystemException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(TestcontainersConfig.class)
class BookmarkServiceTest {
    @Autowired
    BookmarkService bookmarkService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldCreateCategoryEvenWhenBookmarkCreationFails() {
        String categoryName = "java25";
        Optional<Category> categoryOptional =
                categoryRepository.findByNameEqualsIgnoreCase(categoryName);
        assertThat(categoryOptional).isEmpty();

        var cmd = new CreateBookmarkCmd(
                "My New Article",
                null,
                categoryName);
        assertThatThrownBy(()-> bookmarkService.create(cmd))
                .isInstanceOf(TransactionSystemException.class);

        categoryOptional = categoryRepository.findByNameEqualsIgnoreCase(categoryName);
        assertThat(categoryOptional).isNotEmpty();
    }
}