package com.kubernetes;

import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;

import static com.kubernetes.Versions.JUPITER_VERSION;
import static com.kubernetes.Versions.LOMBOK_VERSION;
import static com.kubernetes.Versions.SPOCK_VERSION;

enum PrivateLibrary {
    LOMBOK("org.projectlombok:lombok", LOMBOK_VERSION),
    SPOCK_CORE("org.spockframework:spock-core", SPOCK_VERSION),
    SPOCK_SPRING("org.spockframework:spock-spring", SPOCK_VERSION),
    JUPITER_API("org.junit.jupiter:junit-jupiter-api", JUPITER_VERSION),
    JUPITER_ENGINE("org.junit.jupiter:junit-jupiter-engine", JUPITER_VERSION),
    JUPITER_PLATFORM_LAUNCHER("org.junit.platform:junit-platform-launcher", JUPITER_VERSION),
    JUPITER_PLATFORM_ENGINE("org.junit.platform:junit-platform-engine", JUPITER_VERSION),
    JUPITER_PLATFORM_COMMONS("org.junit.platform:junit-platform-commons", JUPITER_VERSION);

    private final String artifact;
    private final String version;

    PrivateLibrary(String artifact, String version) {
        this.artifact = artifact;
        this.version = version;
    }

    public String getRepo() {
        return ShortTypeHandling.castToString(new GStringImpl(new String[]{this.version}, new String[]{this.artifact}));
    }
}