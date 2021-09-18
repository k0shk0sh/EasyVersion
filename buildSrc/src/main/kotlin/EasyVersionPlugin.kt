import org.gradle.api.*

class EasyVersionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    val extension = project.extensions.create(
      "easyVersion", EasyVersionExtension::class.java, project,
    )

    val filePath = extension.fileName
    val propertiesToSet = extension.propertiesToSet
    val setToProjectVersion = extension.setToProjectVersion
    val snapshotLabel = extension.snapshotLabel
    val snapshotVersion = extension.getSnapshotVersion(project)

    project.afterEvaluate {
      tasks.create("nextMajor", nextMajor(extension))
      tasks.create("nextMinor", nextMinor(extension))
      tasks.create("nextPatch", nextPatch(extension))
      tasks.create("nextSnapshot", nextSnapshot(extension))
    }
  }

  private fun nextMajor(
    extension: EasyVersionExtension,
  ): (Task).() -> Unit = {
    setTaskMetaData("major")
    actions = listOf(
      object : Action<Task> {
        override fun execute(t: Task) {
          val easyVersion = easyVersion(extension.fileName).increaseMajor()
          EasyVersionFileProvider.writeVersion(extension.fileName, easyVersion)
          setProjectVersion(project, extension, easyVersion)
        }
      },
    )
  }

  private fun nextMinor(
    extension: EasyVersionExtension,
  ): (Task).() -> Unit = {
    setTaskMetaData("minor")
    actions = listOf(
      object : Action<Task> {
        override fun execute(t: Task) {
          val easyVersion = easyVersion(extension.fileName).increaseMinor()
          EasyVersionFileProvider.writeVersion(extension.fileName, easyVersion)
          setProjectVersion(project, extension, easyVersion)
        }
      },
    )
  }

  private fun nextPatch(
    extension: EasyVersionExtension,
  ): (Task).() -> Unit = {
    setTaskMetaData("patch")
    actions = listOf(
      object : Action<Task> {
        override fun execute(t: Task) {
          val easyVersion = easyVersion(extension.fileName).increasePatch()
          EasyVersionFileProvider.writeVersion(extension.fileName, easyVersion)
          setProjectVersion(project, extension, easyVersion)
        }
      },
    )
  }

  private fun nextSnapshot(
    extension: EasyVersionExtension,
  ): (Task).() -> Unit = {
    setTaskMetaData("snapshot")
    actions = listOf(
      object : Action<Task> {
        override fun execute(t: Task) {
          val easyVersion = easyVersion(extension.fileName).setSnapshot(
            extension.getSnapshotVersion(project)
          )
          EasyVersionFileProvider.writeVersion(extension.fileName, easyVersion)
          setProjectVersion(project, extension, easyVersion)
        }
      },
    )
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
      easyVersion.version
    }
    if (extension.setToProjectVersion) {
      project.version = version
    }
    extension.propertiesToSet.forEach { project.extensions.extraProperties.set(it, version) }
  }

  private fun easyVersion(fileName: String) = EasyVersionFileProvider.easyVersion(fileName)
}
