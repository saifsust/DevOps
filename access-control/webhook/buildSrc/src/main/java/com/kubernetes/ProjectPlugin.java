package com.kubernetes;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.plugins.GroovyPlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.testing.Test;
import org.gradle.api.tasks.testing.logging.TestLogEvent;
import org.gradle.jvm.toolchain.JavaLanguageVersion;
import org.gradle.jvm.toolchain.JavaToolchainService;
import org.gradle.jvm.toolchain.JavaToolchainSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.kubernetes.PrivateLibrary.JUPITER_API;
import static com.kubernetes.PrivateLibrary.JUPITER_ENGINE;
import static com.kubernetes.PrivateLibrary.JUPITER_PLATFORM_COMMONS;
import static com.kubernetes.PrivateLibrary.JUPITER_PLATFORM_ENGINE;
import static com.kubernetes.PrivateLibrary.JUPITER_PLATFORM_LAUNCHER;
import static com.kubernetes.PrivateLibrary.LOMBOK;
import static com.kubernetes.PrivateLibrary.SPOCK_CORE;
import static com.kubernetes.PrivateLibrary.SPOCK_SPRING;
import static com.kubernetes.Versions.JAVA_VERSION;
import static org.gradle.api.plugins.JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME;
import static org.gradle.api.plugins.JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME;
import static org.gradle.api.plugins.JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME;
import static org.gradle.api.plugins.JavaPlugin.TEST_RUNTIME_ONLY_CONFIGURATION_NAME;

public final class ProjectPlugin implements Plugin<@NotNull Project> {
    private static final boolean ENABLED_BUILDING_JAR_TASK = true;
    private static final boolean ENABLED_STACKTRACE = true;
    private static final boolean ENABLED_STANDARD_STREAM = true;

    @Override
    public void apply(Project project) {
        loadPrivateDependencies(project);
        loadRepositories(project);
        loadPlugins(project);
        loadJava(project);
        enabledBuildingJarTask(project);
        usedJUnitTestPlatform(project);
    }

    private void usedJUnitTestPlatform(Project project) {
        project.getTasks().withType(Test.class, testTask -> {
            testTask.useJUnitPlatform();
            testTask.getOutputs().upToDateWhen(this::enabledTestCaseDetails);
            testTask.testLogging(testLoggingContainer -> {
                testLoggingContainer.getEvents().addAll(
                        Set.of(
                                TestLogEvent.PASSED,
                                TestLogEvent.SKIPPED,
                                TestLogEvent.FAILED,
                                TestLogEvent.STANDARD_OUT
                        )
                );
                testLoggingContainer.setShowStackTraces(ENABLED_STACKTRACE);
                testLoggingContainer.setShowStandardStreams(ENABLED_STANDARD_STREAM);
            });
        });
    }

    private void enabledBuildingJarTask(Project project) {
        project.getTasks()
                .named(JavaPlugin.JAR_TASK_NAME)
                .configure(this::enabledJar);
    }

    private boolean enabledTestCaseDetails(Task task) {
        return !task.getEnabled();
    }

    private void enabledJar(Task jar) {
        jar.setEnabled(ENABLED_BUILDING_JAR_TASK);
    }

    private void loadJava(Project project) {
        project.getExtensions().getByType(JavaToolchainService.class)
                .launcherFor(
                        this::applyJava
                );
    }

    private void applyJava(JavaToolchainSpec javaToolChain) {
        javaToolChain.getLanguageVersion()
                .set(
                        JavaLanguageVersion.of(JAVA_VERSION)
                );
    }

    private void loadPlugins(Project project) {
        project.getPluginManager().apply(GroovyPlugin.class);
    }

    private void loadRepositories(Project project) {
        project.getRepositories().mavenCentral();
        project.getRepositories().mavenLocal();
    }

    private void loadPrivateDependencies(Project project) {
        DependencyHandler handler = project.getDependencies();

        addRepository(handler, IMPLEMENTATION_CONFIGURATION_NAME, LOMBOK.getRepo());
        addRepository(handler, ANNOTATION_PROCESSOR_CONFIGURATION_NAME, LOMBOK.getRepo());
        addRepository(handler, TEST_IMPLEMENTATION_CONFIGURATION_NAME, SPOCK_CORE.getRepo());
        addRepository(handler, TEST_IMPLEMENTATION_CONFIGURATION_NAME, SPOCK_SPRING.getRepo());
        addRepository(handler, TEST_RUNTIME_ONLY_CONFIGURATION_NAME, JUPITER_API.getRepo());
        addRepository(handler, TEST_RUNTIME_ONLY_CONFIGURATION_NAME, JUPITER_ENGINE.getRepo());
        addRepository(handler, TEST_RUNTIME_ONLY_CONFIGURATION_NAME, JUPITER_PLATFORM_COMMONS.getRepo());
        addRepository(handler, TEST_RUNTIME_ONLY_CONFIGURATION_NAME, JUPITER_PLATFORM_ENGINE.getRepo());
        addRepository(handler, TEST_RUNTIME_ONLY_CONFIGURATION_NAME, JUPITER_PLATFORM_LAUNCHER.getRepo());
    }

    private void addRepository(
            DependencyHandler handler,
            String configName,
            String repository
    ) {
        handler.add(configName, repository);
    }
}