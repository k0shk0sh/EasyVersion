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

  private fun setVersionNameAndCode(project: Project) {
    EasyVersionFileProvider.easyVersion().let {
      setProjectVersion(
        project, project.getExtension(), it, isSnapshot = !it.snapshotVersion.isNullOrBlank()
      )
    }
  }

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

  private fun nextSnapshot(): (Task).() -> Unit = {
    setTaskMetaData("snapshot")
    actions = listOf(
      object : Action<Task> {
        override fun execute(t: Task) {
          val extension = project.getExtension()
          val easyVersion = easyVersion().setSnapshot(
            extension.getSnapshotVersion(project)
          )
          EasyVersionFileProvider.writeVersion(easyVersion)
          setProjectVersion(project, extension, easyVersion, isSnapshot = true)
        }
      },
    )
  }

  private fun Project.getExtension(): EasyVersionExtension {
    return rootProject.extensions.getByType(EasyVersionExtension::class.java)
  }

  private fun Task.setTaskMetaData(taskName: String) {
    group = "EasyVersion"
    description = "Updating $taskName version"
  }

  private fun setProjectVersion(
    project: Project,
    extension: EasyVersionExtension,
    easyVersion: EasyVersion,
    isSnapshot: Boolean = false,
  ) {
    val version = if (isSnapshot && !easyVersion.snapshotVersion.isNullOrEmpty()) {
      if (extension.snapshotLabel.isNullOrEmpty()) {
        easyVersion.snapshotVersion
      } else {
        "${easyVersion.snapshotVersion}${extension.snapshotLabel}"
      }
    } else {
      easyVersion.versionName
    }

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
