import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import java.io.FileReader
import java.io.FileWriter
import org.gradle.internal.impldep.org.testng.reporters.XMLReporter.FILE_NAME

internal data class EasyVersion(
  @Expose val major: Int = 0,
  @Expose val minor: Int = 0,
  @Expose val patch: Int = 0,
  @Expose val snapshotVersion: String? = null,
) {
  val versionName: String
    get() = if (snapshotVersion.isNullOrEmpty()) {
      "$major.$minor.$patch"
    } else {
      snapshotVersion
    }

  val versionCode: Int get() = "$major$minor$patch".toInt()
}

internal fun EasyVersion.increaseMajor() = this.copy(
  major = major + 1, minor = 0, patch = 0, snapshotVersion = null,
)

internal fun EasyVersion.increaseMinor() = this.copy(minor = minor + 1, snapshotVersion = null)
internal fun EasyVersion.increasePatch() = this.copy(patch = patch + 1, snapshotVersion = null)
internal fun EasyVersion.setSnapshot(snapshotVersion: String) = this.copy(
  snapshotVersion = snapshotVersion
)

/**
 * Class that provide Read/Write of [EasyVersion].
 */
object EasyVersionFileProvider {
  /**
   * Used to serialize and deserialize [EasyVersion]
   */
  private val gson = GsonBuilder()
    .excludeFieldsWithoutExposeAnnotation()
    .serializeNulls()
    .create()

  /**
   * Load [EasyVersion] from file.
   * @param fileName the file name of EasyVersion.
   * @return [EasyVersion] from [fileName].
   */
  internal fun easyVersion(fileName: String = FILE_NAME): EasyVersion {
    return gson.newJsonReader(FileReader(fileName)).use {
      gson.fromJson(it, EasyVersion::class.java)
    }
  }

  /**
   * Write [EasyVersion] to file.
   * @param fileName the file name of EasyVersion.
   * @param easyVersion the [EasyVersion] to write to [fileName].
   */
  internal fun writeVersion(
    fileName: String,
    easyVersion: EasyVersion,
  ) {
    FileWriter(fileName).use {
      gson.toJson(easyVersion, it)
    }
  }

}
