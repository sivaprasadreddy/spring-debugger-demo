package com.sivalabs.bookmarks.domain;

import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

/**
 * Projection for {@link Bookmark}
 */
public interface BookmarkInfo {
    Long getId();

    String getTitle();

    String getUrl();

    @Value("#{target.category == null ? null : target.category.name}")
    String getCategoryName();

    Instant getCreatedAt();
}