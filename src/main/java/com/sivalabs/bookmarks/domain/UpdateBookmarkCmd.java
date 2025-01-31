package com.sivalabs.bookmarks.domain;

public record UpdateBookmarkCmd(Long id, String title, String url, String categoryName) {
}
