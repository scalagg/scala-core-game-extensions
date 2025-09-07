import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `maven-publish`
    kotlin("jvm") version "1.9.20"
    kotlin("kapt") version "1.9.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.ajoberstar.grgit") version "4.1.1"
}

var currentBranch: String = grgit.branch.current().name

allprojects {
    group = "gg.scala.game.extensions"
    version = "1.5.0${
        if (currentBranch == "develop") "-dev" else ""
    }"

    repositories {
        mavenCentral()
        maven("https://repo.dmulloy2.net/repository/public/")
        configureScalaRepository()
    }
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("lib"))

    kapt("gg.scala.commons:bukkit:4.0.2")
    compileOnly("gg.scala.commons:bukkit:4.0.2")

    api("com.elmakers.mine.bukkit:EffectLib:10.2")
    implementation("com.squareup.okhttp3:okhttp:3.2.0")

    compileOnly("gg.scala.achievements:scala-achievements-plugin:1.0.4")
    compileOnly("gg.scala.tangerine:tangerine:1.2.4")

    compileOnly("gg.scala.store:spigot:0.1.8")
    compileOnly("gg.scala.chattags.plugin:scala-chattags-plugin:1.2.0")
    compileOnly("gg.scala.friends:scala-friends:2.1.0")
    compileOnly("gg.scala.basics.plugin:scala-basics-plugin:1.0.5")

    compileOnly("xyz.xenondevs:particle:1.8.4")

    compileOnly("gg.scala.spigot:server:1.1.0")

    compileOnly("gg.scala.lemon:bukkit:1.9.1")
    compileOnly("gg.scala.cloudsync:spigot:1.0.1")
}

kotlin {
    jvmToolchain(jdkVersion = 17)
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")
    exclude(
        "**/*.kotlin_metadata",
        "**/*.kotlin_builtins",
        "META-INF/"
    )

    archiveFileName.set(
        "core-game-extensions.jar"
    )
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
    options.fork()
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.javaParameters = true
    kotlinOptions.jvmTarget = "17"
}

publishing {
    repositories.configureScalaRepository(
        dev = currentBranch == "develop"
    )

    publications {
        register(
            name = "mavenJava",
            type = MavenPublication::class,
            configurationAction = shadow::component
        )
    }
}

tasks.getByName("build")
    .dependsOn(
        "shadowJar",
        "publishMavenJavaPublicationToScalaRepository"
    )

fun RepositoryHandler.configureScalaRepository(dev: Boolean = false)
{
    maven("${property("artifactory_contextUrl")}/gradle-${if (dev) "dev" else "release"}") {
        name = "scala"
        credentials {
            username = property("artifactory_user").toString()
            password = property("artifactory_password").toString()
        }
    }
}
