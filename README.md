# EasyVersion

EasyVersion is a Gradle plugin that manage your app or library version.

## Download

Please downloading the plugin, please create `easy_version.json` in your root project directory with
below content.

```json
{
  "major": 0,
  "minor": 0,
  "patch": 0,
  "snapshotVersion": null
}
```

```kotlin
plugins {
  "easyVersion" version "<version>"
}
```

## Possible Configurations

```kotlin
easyVersionConfig {
  snapshotLabel = "-SNAPSHOT" // default.
  snapshotDefinition = SnapshotDefinition.TIMESTAMP // default. Or change to DATE_SECONDS, COMMIT.
  propertiesToSet = listOf("VERSION_NAME") // default.
  setToProjectVersion = true // default.
  logVersion = true // default = false. log versions everytime gradle syncs.
}
```

## Tasks

Running either one of the tasks will set your project to the new version automatically unless if set
to false via the plugin extension configuration. They will also ensure to set all your supplied
properties upon completion.

- Next Major
  - Updates the version to the next possible major version and resets snapshot, minor and patch.
  - `./gradlew nextMajor`

- Next Minor
  - Updates the version to the next possible minor version and resets patch.
  - `./gradlew nextMinor`

- Next Patch
  - Updates the version to the next patch version.
  - `./gradlew nextPatch`

- Next Snapshot
  - Updates the snapshot version with either of one the snapshot definition.
  - `./gradlew nextSnapshot`

## EasyVersion Properties

EasyVersion comes with two handy extensions specifically for Android projects:

- `project.versionName` returns the current version name or snapshot if its available.
- `project.versionCode` returns the current version code based on `$major$minor$patch.toInt()`.
