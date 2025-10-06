Project Anatomy

- First assemble the project for creating **bootJar**
- Copied **bootJar** from build directory to docker for building docker images

```shell
  ./gradlew task BootJarCopyToDocker
```

- To clean the docker directory override **clean** task. Cleaning cmd is

```shell
./gradlew task clean
```