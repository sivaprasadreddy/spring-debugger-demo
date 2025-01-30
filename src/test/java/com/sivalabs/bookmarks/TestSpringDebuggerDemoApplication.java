package com.sivalabs.bookmarks;

import org.springframework.boot.SpringApplication;

public class TestSpringDebuggerDemoApplication {

    public static void main(String[] args) {
        SpringApplication
                .from(SpringDebuggerDemoApplication::main)
                .with(TestcontainersConfig.class)
                .run(args);
    }

}
