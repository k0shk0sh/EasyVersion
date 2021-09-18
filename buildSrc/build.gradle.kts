import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
  `kotlin-dsl`
}

dependencies {
  implementation(kotlin("gradle-plugin"))
  implementation(gradleApi())
  implementation("com.google.code.gson:gson:2.8.8")
}

repositories {
  mavenCentral()
}

gradlePlugin {
  plugins {
    create("easyVersion") {
      id = "easyVersion"
      implementationClass = "EasyVersionPlugin"
      displayName = "EasyVersion Gradle Plugin"
      description = "Gradle plugin that manage your App or Library versioning"
    }
  }
}
