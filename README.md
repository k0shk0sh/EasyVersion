# EasyVersion ![release-status](https://github.com/k0shk0sh/EasyVersion/actions/workflows/release.yml/badge.svg)

EasyVersion is a Gradle plugin that manage your app or library version.

## Before Downloading

Create `easy_version.json` in your root project directory with below content and modify them to
match your needs.

```json
{
  "major": 0,
  "minor": 0,
  "patch": 0,
  "snapshotVersion": null
}
```

## Download [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.k0shk0sh/easyversion/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.k0shk0sh/easyversion)

```kotlin
plugins {
  "com.github.k0shk0sh.easy.version" version "<version>"
}
```

## Possible Configurations

```kotlin
easyVersion {
  snapshotLabel = "-SNAPSHOT" // default.
  snapshotDefinition = SnapshotDefinition.TIMESTAMP // default. Or change to DATE_SECONDS, COMMIT.
  propertiesToSet = listOf("VERSION_NAME") // default.
  setToProjectVersion = true // default.
  logVersion = true // default = false. log versions everytime gradle syncs.
}
```

## Tasks

Running either one of the tasks will set your project to the new version automatically unless if set
to false via the plugin extension configuration. Each task will also ensure to set all your supplied
properties upon completion.

- Next Major
  - Updates the version to the next possible major version and resets snapshot, minor & patch.
  - `./gradlew nextMajor`

- Next Minor
  - Updates the version to the next possible minor version and resets patch & snapshot.
  - `./gradlew nextMinor`

- Next Patch
  - Updates the version to the next patch version and resets snapshot.
  - `./gradlew nextPatch`

- Next Snapshot
  - Updates the snapshot version with either one of the snapshot definition supplied in plugin
    configuration.
  - `./gradlew nextSnapshot`

## Example

Before publishing your new awesome library, module or app, call either one of the tasks first
followed by your releasing task.

You can also refer to [ComposeEasyForms-Library](https://github.com/k0shk0sh/ComposeEasyForms) that
uses this library to manage its release update versioning.

> P.S: this library uses itself to manage its release versioning.

Alternatively see [release.yml](./.github/workflows/release.yml) for more details.

## EasyVersion Properties

EasyVersion comes with two handy extensions specifically for Android projects:

- `rootproject.versionName` returns the current version name or snapshot if its available.
- `rootProject.versionCode` returns the current version code based on `$major$minor$patch.toInt()`.

```kotlin
android {
  defaultConfig {
    versionCode = rootProject.versionCode as Int
    versionName = rootProject.versionName as String
  }
}
```

## Contributions

Please contribute! We will gladly review any pull requests. Make sure to read
the [Contributing](.github/CONTRIBUTING.md) page first though.
