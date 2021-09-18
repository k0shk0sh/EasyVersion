package com.github.k0shk0sh.easy.version

import org.gradle.api.*
import org.gradle.kotlin.dsl.create

class EasyVersionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    project.extensions.create<EasyVersionExtension>("easyVersion")

    if (!project.file(FILE_NAME).exists()) {
      throw GradleException("Please create $FILE_NAME in your root project directory")
    }

    project.afterEvaluate {
      setVersionNameAndCode(this)
      tasks.create("nextMajor", nextMajor())
      tasks.create("nextMinor", nextMinor())
      tasks.create("nextPatch", nextPatch())
      tasks.create("nextSnapshot", nextSnapshot())
    }
  }

  /**
   * Initialize project with current configuration on sync.
   */
  private fun setVersionNameAndCode(project: Project) {
    setProjectVersion(
      project, project.getExtension(), EasyVersionFileProvider.easyVersion()
    )
  }

  /**
   * Run nextMajor task.
   */
  private fun nextMajor(): (Task).() -> Unit = {
    setTaskMetaData("major")
    actions = listOf(
      object : Action<Task> {
        override fun execute(t: Task) {
          val extension = project.getExtension()
          val easyVersion = easyVersion().increaseMajor()
          EasyVersionFileProvider.writeVersion(easyVersion)
          setProjectVersion(project, extension, easyVersion)
        }
      },
    )
  }

  /**
   * Run nextMinor task.
   */
  private fun nextMinor(): (Task).() -> Unit = {
    setTaskMetaData("minor")
    actions = listOf(
      object : Action<Task> {
        override fun execute(t: Task) {
          val extension = project.getExtension()
          val easyVersion = easyVersion().increaseMinor()
          EasyVersionFileProvider.writeVersion(easyVersion)
          setProjectVersion(project, extension, easyVersion)
        }
      },
    )
  }

  /**
   * Run nextPatch task.
   */
  private fun nextPatch(): (Task).() -> Unit = {
    setTaskMetaData("patch")
    actions = listOf(
      object : Action<Task> {
        override fun execute(t: Task) {
          val extension = project.getExtension()
          val easyVersion = easyVersion().increasePatch()
          EasyVersionFileProvider.writeVersion(easyVersion)
          setProjectVersion(project, extension, easyVersion)
        }
      },
    )
  }

  /**
   * Run nextSnapshot task.
   */
  private fun nextSnapshot(): (Task).() -> Unit = {
    setTaskMetaData("snapshot")
    actions = listOf(
      object : Action<Task> {
        override fun execute(t: Task) {
          val extension = project.getExtension()
          val easyVersion = easyVersion().setSnapshot(
            if (!extension.snapshotLabel.isNullOrBlank()) {
              "${extension.getSnapshotVersion(project)}${extension.snapshotLabel}"
            } else {
              extension.getSnapshotVersion(project)
            }
          )
          EasyVersionFileProvider.writeVersion(easyVersion)
          setProjectVersion(project, extension, easyVersion)
        }
      },
    )
  }

  /**
   * Get EasyVersion extension.
   */
  private fun Project.getExtension(): EasyVersionExtension {
    return rootProject.extensions.getByType(EasyVersionExtension::class.java)
  }

  /**
   * Initialize task description and group.
   */
  private fun Task.setTaskMetaData(taskName: String) {
    group = "EasyVersion"
    description = "Updating $taskName version"
  }

  /**
   * Sets project version, custom properties & EasyVersion properties.
   */
  private fun setProjectVersion(
    project: Project,
    extension: EasyVersionExtension,
    easyVersion: EasyVersion,
  ) {
    val version = easyVersion.versionName

    if (extension.setToProjectVersion) {
      project.version = version
    }

    extension.propertiesToSet.forEach { project.extensions.extraProperties.set(it, version) }
    project.extensions.extraProperties.set(EASY_VERSION_NAME, easyVersion.versionName)
    project.extensions.extraProperties.set(EASY_VERSION_CODE, easyVersion.versionCode)

    if (extension.logVersion) logEasyVersion(project)
  }

  private fun easyVersion() = EasyVersionFileProvider.easyVersion()

  private fun logEasyVersion(project: Project) {
    println("EasyVersionPlugin: project.version = ${project.version}")
    println("EasyVersionPlugin: project.versionName = ${project.versionName}")
    println("EasyVersionPlugin: project.versionCode = ${project.versionCode}")
  }

}
