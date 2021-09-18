import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
  `kotlin-dsl`
}

kotlin {
  explicitApi()
}

dependencies {
  implementation(kotlin("gradle-plugin"))
  implementation(gradleApi())
  implementation("com.google.code.gson:gson:2.8.8")
}

repositories {
  mavenCentral()
}
