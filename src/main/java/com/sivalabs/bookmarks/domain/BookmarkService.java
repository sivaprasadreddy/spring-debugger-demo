package com.sivalabs.bookmarks.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class BookmarkService {
    private static final Logger log = LoggerFactory.getLogger(BookmarkService.class);
    private final BookmarkRepository bookmarkRepository;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, CategoryService categoryService, CategoryRepository categoryRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<BookmarkInfo> findAllBookmarks() {
        return bookmarkRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Optional<BookmarkInfo> findBookmarkById(Long id) {
        return bookmarkRepository.findBookmarkById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Bookmark> findById(Long id) {
        return bookmarkRepository.findById(id);
    }

    @Transactional
    public Bookmark create(CreateBookmarkCmd cmd) {
        var bookmark = new Bookmark(cmd.title(), cmd.url());
        if(cmd.categoryName() != null) {
            Category category = categoryService.findByName(cmd.categoryName()).orElse(null);
            if (category == null) {
                category = categoryService.create(new Category(cmd.categoryName()));
            }
            bookmark.setCategory(category);
        }
        bookmarkRepository.save(bookmark);
        log.info("Created bookmark with id: {}", bookmark.getId());
        return bookmark;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Category create(Category category) {
        category.setId(null);
        return categoryRepository.save(category);
    }

    @Transactional
    public void update(UpdateBookmarkCmd cmd) {
        var bookmarkEntity =
                bookmarkRepository.findById(cmd.id())
                        .orElseThrow(()-> BookmarkNotFoundException.withId(cmd.id()));
        bookmarkEntity.setTitle(cmd.title());
        bookmarkEntity.setUrl(cmd.url());
        if(cmd.categoryName() != null) {
            Category category = categoryService.findByName(cmd.categoryName()).orElse(null);
            if (category == null) {
                category = categoryService.create(new Category(cmd.categoryName()));
            }
            bookmarkEntity.setCategory(category);
        } else {
            bookmarkEntity.setCategory(null);
        }
        bookmarkEntity.setUpdatedAt(Instant.now());
        bookmarkRepository.save(bookmarkEntity);
    }

    @Transactional
    public void deleteById(Long id) {
        var bookmark = bookmarkRepository.findById(id)
                .orElseThrow(()-> BookmarkNotFoundException.withId(id));
        bookmarkRepository.delete(bookmark);
    }
}
