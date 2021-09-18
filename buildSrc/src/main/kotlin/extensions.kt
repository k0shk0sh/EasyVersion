import org.gradle.api.Project

val Project.versionName get() = extensions.extraProperties.get(EASY_VERSION_NAME)
val Project.versionCode get() = extensions.extraProperties.get(EASY_VERSION_CODE)