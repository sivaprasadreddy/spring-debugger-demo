package com.sivalabs.bookmarks.api;

import com.sivalabs.bookmarks.domain.*;
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
        var bookmark = bookmarkService.findBookmarkById(id)
                        .orElseThrow(()-> BookmarkNotFoundException.withId(id));
        return ResponseEntity.ok(bookmark);
    }

    @PostMapping
    ResponseEntity<Void> createBookmark(@Valid @RequestBody CreateBookmarkPayload payload) {
        var cmd = new CreateBookmarkCmd(payload.title(), payload.url(), payload.categoryName());
        var savedBookmark = bookmarkService.create(cmd);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .build(savedBookmark.getId());
        return ResponseEntity.created(url).build();
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> updateBookmark(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookmarkPayload payload) {
        var cmd = new UpdateBookmarkCmd(id, payload.title(), payload.url(), payload.categoryName());
        bookmarkService.update(cmd);
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

    record CreateBookmarkPayload(
            @NotEmpty(message = "Title is required")
            String title,
            @NotEmpty(message = "Url is required")
            String url,
            String categoryName) {}

    record UpdateBookmarkPayload(
            @NotEmpty(message = "Title is required")
            String title,
            @NotEmpty(message = "Url is required")
            String url,
            String categoryName) {
    }
}
