package com.github.k0shk0sh.easy.version

import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.gradle.api.Project

/**
 * EasyVersion configuration extension class.
 */
abstract class EasyVersionExtension {

  /**
   * Define which properties the plugin will set after its task has completed.
   * EX: listOf(VERSION_NAME) which later will set the latest version to that property.
   *
   * Default: listOf([VERSION_NAME])
   */
  var propertiesToSet: List<String> = listOf(VERSION_NAME)

  /**
   * Define whether to set the project version after the plugin task is completed or not.
   *
   * Default: true.
   */
  var setToProjectVersion: Boolean = true

  /**
   * Define the snapshot label to append to what snapshot definition generates.
   *
   * Default: -SNAPSHOT
   */
  var snapshotLabel: String? = "-SNAPSHOT"

  /**
   * Define what to use to construct the snapshot version.
   *
   * Default: [SnapshotDefinition.DATE_SECONDS]
   */
  var snapshotDefinition: SnapshotDefinition = SnapshotDefinition.DATE_SECONDS

  /**
   * Generate your own custom snapshot version.
   *
   * If not set, then [snapshotDefinition] is used.
   */
  var snapshotVersionGenerator: (() -> String)? = null

  /**
   * Define if you like the version to logged everytime a gradle sync is happening.
   *
   * Default: false
   */
  var logVersion: Boolean = false

  internal fun getSnapshotVersion(project: Project): String {
    return when (snapshotDefinition) {
      SnapshotDefinition.TIMESTAMP -> System.currentTimeMillis().toString()
      SnapshotDefinition.DATE_SECONDS -> LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyy-mm-dd-ss"))
      SnapshotDefinition.COMMIT -> project.runCommandLine("git rev-parse --verify --short HEAD")
    }
  }
}

/**
 * Tells the plugin how to construct the snapshot version.
 */
enum class SnapshotDefinition {
  /**
   * Configure the plugin to use [System.currentTimeMillis] as the version.
   */
  TIMESTAMP,

  /**
   * Configure the plugin to (yyyy-mm-dd-ss) as the version.
   */
  DATE_SECONDS,

  /**
   * Configure the plugin to use the short commit hash as the version.
   */
  COMMIT;
}

/**
 * Run a command line.
 */
private fun Project.runCommandLine(command: String): String {
  val byteOut = ByteArrayOutputStream()
  project.exec {
    commandLine = command.split(" ")
    standardOutput = byteOut
  }
  return String(byteOut.toByteArray()).trim()
}
