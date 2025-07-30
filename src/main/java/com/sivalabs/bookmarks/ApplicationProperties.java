package com.sivalabs.bookmarks;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    private boolean dataImportEnabled;
    private int pageSize;

    public boolean isDataImportEnabled() {
        return dataImportEnabled;
    }

    public void setDataImportEnabled(boolean dataImportEnabled) {
        this.dataImportEnabled = dataImportEnabled;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
