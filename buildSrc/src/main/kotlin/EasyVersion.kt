import com.google.gson.GsonBuilder
import java.io.FileReader
import java.io.FileWriter

data class EasyVersion(
  val major: Int,
  val minor: Int,
  val patch: Int,
  val snapshotVersion: String? = null,
) {
  val version: String get() = "$major.$minor.$patch"
}

internal fun EasyVersion.increaseMajor() = this.copy(major = major + 1, minor = 0, patch = 0)
internal fun EasyVersion.increaseMinor() = this.copy(minor = minor + 1)
internal fun EasyVersion.increasePatch() = this.copy(patch = patch + 1)
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
    .serializeNulls()
    .create()

  /**
   * Load [EasyVersion] from file.
   * @param fileName the file name of EasyVersion.
   * @return [EasyVersion] from [fileName].
   */
  fun easyVersion(fileName: String): EasyVersion {
    return gson.newJsonReader(FileReader(fileName)).use {
      gson.fromJson(it, EasyVersion::class.java)
    }
  }

  /**
   * Write [EasyVersion] to file.
   * @param fileName the file name of EasyVersion.
   * @param easyVersion the [EasyVersion] to write to [fileName].
   */
  fun writeVersion(
    fileName: String,
    easyVersion: EasyVersion,
  ) {
    FileWriter(fileName).use {
      gson.toJson(easyVersion, it)
    }
  }

}
