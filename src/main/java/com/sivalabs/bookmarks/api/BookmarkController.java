package com.sivalabs.bookmarks.api;

import com.sivalabs.bookmarks.domain.Bookmark;
import com.sivalabs.bookmarks.domain.BookmarkInfo;
import com.sivalabs.bookmarks.domain.BookmarkNotFoundException;
import com.sivalabs.bookmarks.domain.BookmarkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
class BookmarkController {
    private final BookmarkService bookmarkService;

    BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @GetMapping
    List<BookmarkInfo> getBookmarks() {
        return bookmarkService.findAllBookmarks();
    }

    @GetMapping("/{id}")
    ResponseEntity<BookmarkInfo> getBookmarkById(@PathVariable Long id) {
        var bookmark =
                bookmarkService.findBookmarkById(id)
                        .orElseThrow(()-> new BookmarkNotFoundException("Bookmark not found"));
        return ResponseEntity.ok(bookmark);
    }

    record CreateBookmarkPayload(
            @NotEmpty(message = "Title is required")
            String title,
            @NotEmpty(message = "Url is required")
            String url) {}

    @PostMapping
    ResponseEntity<Void> createBookmark(
            @Valid @RequestBody CreateBookmarkPayload payload) {
        var bookmark = new Bookmark(payload.title(), payload.url());
        var savedBookmark = bookmarkService.create(bookmark);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .build(savedBookmark.getId());
        return ResponseEntity.created(url).build();
    }

    record UpdateBookmarkPayload(
            @NotEmpty(message = "Title is required")
            String title,
            @NotEmpty(message = "Url is required")
            String url) {
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> updateBookmark(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookmarkPayload payload) {
        var bookmark = new Bookmark(id, payload.title(), payload.url());
        bookmarkService.update(bookmark);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    void deleteBookmark(@PathVariable Long id) {
        bookmarkService.deleteById(id);
    }

    @ExceptionHandler(BookmarkNotFoundException.class)
    ResponseEntity<Void> handle(BookmarkNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
