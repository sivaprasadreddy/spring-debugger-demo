package com.sivalabs.bookmarks.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final CategoryService categoryService;

    public BookmarkService(BookmarkRepository bookmarkRepository, CategoryService categoryService) {
        this.bookmarkRepository = bookmarkRepository;
        this.categoryService = categoryService;
    }

    public List<BookmarkInfo> findAllBookmarks() {
        return bookmarkRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<BookmarkInfo> findBookmarkById(Long id) {
        return bookmarkRepository.findBookmarkById(id);
    }

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
        return bookmarkRepository.save(bookmark);
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
