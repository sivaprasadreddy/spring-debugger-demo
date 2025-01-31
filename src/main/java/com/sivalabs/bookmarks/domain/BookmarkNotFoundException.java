package com.sivalabs.bookmarks.domain;

public class BookmarkNotFoundException extends RuntimeException {
    public BookmarkNotFoundException(String message) {
        super(message);
    }

    public static BookmarkNotFoundException withId(Long id) {
        return new BookmarkNotFoundException("Bookmark with id " + id + " not found");
    }
}