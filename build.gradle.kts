//import com.github.k0shk0sh.easy.version.SnapshotDefinition

plugins {
  `kotlin-dsl`
  id("com.vanniktech.maven.publish") version "0.18.0"
}

gradlePlugin {
  plugins {
    create("easyVersion") {
      id = "com.github.k0shk0sh.easy.version"
      implementationClass = "com.github.k0shk0sh.easy.version.EasyVersionPlugin"
      displayName = "EasyVersion Gradle Plugin"
      description = "Gradle plugin that manage your App or Library versioning"
    }
  }
}

repositories {
  mavenCentral()
  mavenLocal()
}

dependencies {
  kotlin("kotlin-gradle")
  implementation(gradleApi())
  implementation("com.google.code.gson:gson:2.8.8")
}
