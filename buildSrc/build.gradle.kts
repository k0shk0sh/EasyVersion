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
    create("easyversion") {
      id = "com.github.k0shk0sh.easy.version"
      implementationClass = "EasyVersionPlugin"
    }
  }
}
