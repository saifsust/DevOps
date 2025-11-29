package com.kubernetes;

import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;

public enum PublicLibrary {
    SPRING_WEB_MVC("org.springframework.boot:spring-boot-starter-webmvc", ""),
    SPRING_WEB_MVC_TEST("org.springframework.boot:spring-boot-starter-webmvc-test", "");

    private final String artifact;
    private final String version;

    PublicLibrary(String artifact, String version) {
        this.artifact = artifact;
        this.version = version;
    }

    public String getRepo() {
        return ShortTypeHandling.castToString(new GStringImpl(new String[]{this.version}, new String[]{this.artifact}));
    }
}