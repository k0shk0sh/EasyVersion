import java.net.URL
import java.util.concurrent.Callable
import com.github.k0shk0sh.easy.version.SnapshotDefinition

plugins {
  `kotlin-dsl`
  id("org.jetbrains.dokka") version "1.5.0"
  id("com.vanniktech.maven.publish") version "0.18.0"
  id("com.github.breadmoirai.github-release") version "2.2.12"
  id("com.github.k0shk0sh.easy.version") version "+" // get latest always
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
  releaseName(version)
  body(changelog())
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
  mavenLocal()
}

dependencies {
  kotlin("kotlin-gradle")
  implementation(gradleApi())
  implementation("com.google.code.gson:gson:2.8.8")
}
