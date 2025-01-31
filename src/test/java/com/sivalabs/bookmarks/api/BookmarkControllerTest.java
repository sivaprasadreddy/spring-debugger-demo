package com.sivalabs.bookmarks.api;

import com.sivalabs.bookmarks.TestcontainersConfig;
import com.sivalabs.bookmarks.domain.Bookmark;
import com.sivalabs.bookmarks.domain.BookmarkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestcontainersConfig.class)
class BookmarkControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @BeforeEach
    void setUp() {
        bookmarkRepository.deleteAllInBatch();
    }

    @Test
    void shouldGetAllBookmarks() {
        bookmarkRepository.saveAndFlush(new Bookmark("JetBrains Blog","https://blog.jetbrains.com"));
        bookmarkRepository.saveAndFlush(new Bookmark("IntelliJ IDEA Blog","https://blog.jetbrains.com/idea/"));

        Bookmark[] bookmarks = restTemplate.getForObject("/api/bookmarks", Bookmark[].class);

        assertThat(bookmarks.length).isEqualTo(2);
        assertThat(bookmarks[0].getTitle()).isEqualTo("IntelliJ IDEA Blog");
        assertThat(bookmarks[1].getTitle()).isEqualTo("JetBrains Blog");
    }

    @Test
    void shouldGetBookmarkById() {
        Bookmark jetBrainsBlog = new Bookmark("JetBrains Blog", "https://blog.jetbrains.com");
        Long id = bookmarkRepository.saveAndFlush(jetBrainsBlog).getId();

        Bookmark bookmark = restTemplate.getForObject("/api/bookmarks/{id}", Bookmark.class, id);

        assertThat(bookmark).isNotNull();
        assertThat(bookmark.getTitle()).isEqualTo("JetBrains Blog");
        assertThat(bookmark.getUrl()).isEqualTo("https://blog.jetbrains.com");
    }

    @Test
    void shouldCreateBookmark() {
        var newBookmark = new BookmarkController.CreateBookmarkPayload("JetBrains Academy", "https://www.jetbrains.com/academy/", "Tutorials");

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/api/bookmarks", newBookmark, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldUpdateBookmark() {
        Bookmark initialBookmark = new Bookmark("JetBrains Blog","https://blog.jetbrains.com");
        Long id = bookmarkRepository.saveAndFlush(initialBookmark).getId();

        var updatedBookmark = new BookmarkController.UpdateBookmarkPayload("Updated Title", "https://updated.url", "Blogs");
        restTemplate.put("/api/bookmarks/{id}", updatedBookmark, id);

        Bookmark foundBookmark = bookmarkRepository.findById(id).orElse(null);
        assertThat(foundBookmark).isNotNull();
        assertThat(foundBookmark.getTitle()).isEqualTo("Updated Title");
        assertThat(foundBookmark.getUrl()).isEqualTo("https://updated.url");
    }

    @Test
    void shouldDeleteBookmarkById() {
        Bookmark demoBlog = new Bookmark("Demo Blog", "https://blog.demo.com");
        Long id = bookmarkRepository.saveAndFlush(demoBlog).getId();

        restTemplate.delete("/api/bookmarks/{id}", id);

        Bookmark foundBookmark = bookmarkRepository.findById(id).orElse(null);
        assertThat(foundBookmark).isNull();
    }
}