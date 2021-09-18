package com.github.k0shk0sh.easy.version

import com.google.gson.GsonBuilder
import java.io.FileReader
import java.io.FileWriter

internal data class EasyVersion(
  val major: Int = 0,
  val minor: Int = 0,
  val patch: Int = 0,
  val snapshotVersion: String? = null,
)

/**
 * Increase major version and reset the reset.
 */
internal fun EasyVersion.increaseMajor() = this.copy(
  major = major + 1, minor = 0, patch = 0, snapshotVersion = null,
)

/**
 * Increase minor version and reset [EasyVersion.patch] and [EasyVersion.snapshotVersion].
 */
internal fun EasyVersion.increaseMinor() = this.copy(
  minor = minor + 1, patch = 0, snapshotVersion = null
)

/**
 * Increase patch version and reset [EasyVersion.snapshotVersion].
 */
internal fun EasyVersion.increasePatch() = this.copy(
  patch = patch + 1, snapshotVersion = null
)

/**
 * Sets new snapshot version.
 */
internal fun EasyVersion.setSnapshot(snapshotVersion: String) = this.copy(
  snapshotVersion = snapshotVersion
)

/**
 * Return version name of [EasyVersion.major].[EasyVersion.minor].[EasyVersion.patch]
 */
internal val EasyVersion.versionName: String
  get() = if (snapshotVersion.isNullOrBlank()) {
    "$major.$minor.$patch"
  } else {
    snapshotVersion
  }

/**
 * Return version code of [EasyVersion.major] [EasyVersion.minor] [EasyVersion.patch] combined.
 */
internal val EasyVersion.versionCode: Int get() = "$major$minor$patch".toInt()

/**
 * Class that provide Read/Write of [EasyVersion].
 */
object EasyVersionFileProvider {
  /**
   * Used to serialize and deserialize [EasyVersion]
   */
  private val gson = GsonBuilder()
    .serializeNulls()
    .setPrettyPrinting()
    .create()

  /**
   * Load [EasyVersion] from file.
   * @return deserialized [EasyVersion] from [FILE_NAME] file.
   */
  internal fun easyVersion(): EasyVersion {
    return gson.newJsonReader(FileReader(FILE_NAME)).use {
      gson.fromJson(it, EasyVersion::class.java)
    }
  }

  /**
   * Write [EasyVersion] to file.
   * @param easyVersion the [EasyVersion] to write to [FILE_NAME] file.
   */
  internal fun writeVersion(
    easyVersion: EasyVersion,
  ) {
    FileWriter(FILE_NAME).use {
      gson.toJson(easyVersion, it)
    }
  }
}
