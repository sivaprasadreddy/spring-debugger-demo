package com.sivalabs.bookmarks;

import com.sivalabs.bookmarks.domain.Bookmark;
import com.sivalabs.bookmarks.domain.BookmarkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final BookmarkRepository bookmarkRepository;
    private final ApplicationProperties properties;

    public DataInitializer(BookmarkRepository bookmarkRepository, ApplicationProperties properties) {
        this.bookmarkRepository = bookmarkRepository;
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
        bookmarkRepository.saveAll(getBookmarks());
        log.info("Completed data import");
    }

    private List<Bookmark> getBookmarks() {
        return List.of(
                new Bookmark("JetBrains Blog","https://blog.jetbrains.com"),
                new Bookmark("IntelliJ IDEA Blog","https://blog.jetbrains.com/idea/")
        );
    }
}
