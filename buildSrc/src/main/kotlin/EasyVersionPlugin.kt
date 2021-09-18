import org.gradle.api.*

class EasyVersionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    project.extensions.create("easyVersion", EasyVersionExtension::class.java, project)

    project.afterEvaluate {
      tasks.create("nextMajor", nextMajor())
      tasks.create("nextMinor", nextMinor())
      tasks.create("nextPatch", nextPatch())
      tasks.create("nextSnapshot", nextSnapshot())
    }
  }

  private fun nextMajor(): (Task).() -> Unit = {
    setTaskMetaData("major")
    actions = listOf(
      object : Action<Task> {
        override fun execute(t: Task) {
          val extension = getExtension()
          val easyVersion = easyVersion(extension.fileName).increaseMajor()
          EasyVersionFileProvider.writeVersion(extension.fileName, easyVersion)
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
          val extension = getExtension()
          val easyVersion = easyVersion(extension.fileName).increaseMinor()
          EasyVersionFileProvider.writeVersion(extension.fileName, easyVersion)
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
          val extension = getExtension()
          val easyVersion = easyVersion(extension.fileName).increasePatch()
          EasyVersionFileProvider.writeVersion(extension.fileName, easyVersion)
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
          val extension = getExtension()
          val easyVersion = easyVersion(extension.fileName).setSnapshot(
            extension.getSnapshotVersion(project)
          )
          EasyVersionFileProvider.writeVersion(extension.fileName, easyVersion)
          setProjectVersion(project, extension, easyVersion)
        }
      },
    )
  }

  private fun Task.getExtension(): EasyVersionExtension {
    return project.rootProject.extensions.getByType(EasyVersionExtension::class.java)
  }

  private fun Task.setTaskMetaData(taskName: String) {
    group = "EasyVersion"
    description = "Updating $taskName version"
  }

  private fun setProjectVersion(
    project: Project,
    extension: EasyVersionExtension,
    easyVersion: EasyVersion,
  ) {
    val version = if (!easyVersion.snapshotVersion.isNullOrEmpty()) {
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
  }

  private fun easyVersion(fileName: String) = EasyVersionFileProvider.easyVersion(fileName)
}
