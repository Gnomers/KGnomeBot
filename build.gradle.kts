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
    implementation("dev.kord:kord-core-voice:0.14.0")

    // Logging
    implementation("org.slf4j:slf4j-simple:2.0.3")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")

    // Audio player
//    implementation("com.sedmelluq:lavaplayer:1.3.47")
    implementation(files("/libs/com.sedmelluq/lavaplayer/1.3.73/414880b4e42365a6d2f014b251edc02c6f073e00/lavaplayer-1.3.73.jar"))
    implementation(files("/libs/com.sedmelluq/lava-common/1.1.2/c9e2c5192a93847edd6b96c2f93530fdcda85028/lava-common-1.1.2.jar"))
//    implementation(files("/libs/com.sedmelluq/lavaplayer/1.3.77/89401c843d79108a69319bfc2e9e043335165ce5/lavaplayer-1.3.77.jar"))

    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    // HTTP
    implementation("com.squareup.okhttp:okhttp:2.7.5")
    implementation("org.apache.httpcomponents:httpclient:4.5")


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
