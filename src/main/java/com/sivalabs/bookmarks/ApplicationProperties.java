package com.sivalabs.bookmarks;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    private boolean dataImportEnabled;

    public boolean isDataImportEnabled() {
        return dataImportEnabled;
    }
    public void setDataImportEnabled(boolean dataImportEnabled) {
        this.dataImportEnabled = dataImportEnabled;
    }
}
