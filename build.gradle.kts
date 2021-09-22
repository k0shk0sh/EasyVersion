import java.net.URL
import java.util.concurrent.Callable

buildscript {
  dependencies {
    classpath("com.github.k0shk0sh:easyversion:+")
  }
}

plugins {
  `kotlin-dsl`
  id("org.jetbrains.dokka") version "1.5.0"
  id("com.vanniktech.maven.publish") version "0.18.0"
  id("com.github.breadmoirai.github-release") version "2.2.12"
  id("com.github.k0shk0sh.easy.version") version "0.+"
}

easyVersion {
  setToProjectVersion = true
  logVersion = true
}

githubRelease {
  val version = Callable<CharSequence> { project.version.toString() }
  token(System.getenv("GITHUB_TOKEN"))
  owner("k0shk0sh")
  repo("EasyVersion")
  tagName(version)
  targetCommitish("main")
  releaseName { "Release: ${version.call()}" }
  body { "EasyVersion Release: ${version.call()}" }
}

tasks.dokkaHtml.configure {
  dokkaSourceSets {
    named("main") {
      failOnWarning.set(true)
      reportUndocumented.set(true)
      skipEmptyPackages.set(true)
      skipDeprecated.set(true)
      noAndroidSdkLink.set(true)
      jdkVersion.set(8)
      sourceLink {
        localDirectory.set(project.file("src/main/kotlin"))
        remoteUrl.set(URL("https://github.com/k0shk0sh/EasyVersion/blob/main/src/main/kotlin"))
        remoteLineSuffix.set("#L")
      }
    }
  }
  outputDirectory.set(file("docs"))
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
}

dependencies {
  kotlin("kotlin-gradle")
  implementation(gradleApi())
  implementation("com.google.code.gson:gson:2.8.8")
}
