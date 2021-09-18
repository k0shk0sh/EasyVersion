package com.github.k0shk0sh.easy.version

import org.gradle.api.Project

/**
 * EasyVersion plugin custom [EASY_VERSION_NAME] property.
 */
val Project.versionName get() = extensions.extraProperties.get(EASY_VERSION_NAME)

/**
 * EasyVersion plugin custom [EASY_VERSION_CODE] property.
 */
val Project.versionCode get() = extensions.extraProperties.get(EASY_VERSION_CODE)
