package com.sivalabs.bookmarks.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }


    public void deleteById(Long id) {
        var bookmark = bookmarkRepository.findById(id)
                        .orElseThrow(()-> new BookmarkNotFoundException("Bookmark not found"));
        bookmarkRepository.delete(bookmark);
    }

    public List<BookmarkInfo> findAllBookmarks() {
        return bookmarkRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<BookmarkInfo> findBookmarkById(Long id) {
        return bookmarkRepository.findBookmarkById(id);
    }

    public Bookmark create(Bookmark bookmark) {
        bookmark.setId(null);
        return bookmarkRepository.save(bookmark);
    }

    public Optional<Bookmark> findById(Long id) {
        return bookmarkRepository.findById(id);
    }

    public void update(Bookmark bookmark) {
        var bookmarkEntity =
                bookmarkRepository.findById(bookmark.getId())
                        .orElseThrow(()-> new BookmarkNotFoundException("Bookmark not found"));
        bookmarkEntity.setTitle(bookmark.getTitle());
        bookmarkEntity.setUrl(bookmark.getUrl());
        bookmarkEntity.setUpdatedAt(Instant.now());
        bookmarkRepository.save(bookmarkEntity);
    }
}
