package com.sivalabs.bookmarks.domain;

public record CreateBookmarkCmd(String title, String url, String categoryName) {
}
