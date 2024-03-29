plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    application
}

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://m2.dv8tion.net/releases")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation(kotlin("reflect"))

    // Discord
    implementation("dev.kord:kord-core-voice:0.13.1")

    // Logging
    implementation("org.slf4j:slf4j-simple:2.0.3")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")

    // Audio player
    implementation("com.sedmelluq:lavaplayer:1.3.73")

    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    // HTTP
    implementation("com.squareup.okhttp:okhttp:2.7.5")

    // Command registering
    implementation("org.reflections:reflections:0.10.2")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.+")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "bot.KGnomeRunnerKt"
        }

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        configurations.compileClasspath.get().forEach {
            from(if (it.isDirectory) it else zipTree(it))
        }
    }

    compileKotlin {
        kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.ExperimentalStdlibApi"
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    }
}
