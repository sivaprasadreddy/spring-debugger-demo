package com.sivalabs.bookmarks;

import com.sivalabs.bookmarks.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkService bookmarkService;
    private final ApplicationProperties properties;

    public DataInitializer(BookmarkRepository bookmarkRepository, BookmarkService bookmarkService, ApplicationProperties properties) {
        this.bookmarkRepository = bookmarkRepository;
        this.bookmarkService = bookmarkService;
        this.properties = properties;
    }

    @Override
    public void run(String... args) {
        if(!properties.isDataImportEnabled()) {
            log.info("Data import disabled");
            return;
        }
        log.info("Starting data import");
        bookmarkRepository.deleteAllInBatch();
        List<Bookmark> bookmarks = getBookmarks();
        bookmarks.forEach(b -> bookmarkService.createBookmark(new CreateBookmarkCmd(b.getTitle(), b.getUrl(), b.getCategory().getName())));
        log.info("Completed data import");
    }

    private List<Bookmark> getBookmarks() {
        return List.of(
                new Bookmark(null,"JetBrains Blog","https://blog.jetbrains.com", new Category("Blog")),
                new Bookmark(null,"IntelliJ IDEA Blog","https://blog.jetbrains.com/idea/", new Category("Blog")),
                new Bookmark(null,"Spring Guides","https://spring.io/guides", new Category("Tutorials"))
        );
    }
}
