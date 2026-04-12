import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `maven-publish`
    kotlin("jvm") version "2.0.20"
    kotlin("kapt") version "2.0.20"
    id("io.github.goooler.shadow") version "8.1.8"
    id("org.ajoberstar.grgit") version "4.1.1"
}

var currentBranch: String = grgit.branch.current().name

allprojects {
    group = "gg.scala.game.extensions"
    version = "1.5.3${
        if (currentBranch == "develop") "-dev" else ""
    }"

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.dmulloy2.net/repository/public/")
        maven("https://jitpack.io")
        maven("https://repo.xenondevs.xyz/releases")
        maven("https://repo.aikar.co/content/groups/aikar/")
        configureScalaRepository()
    }
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("lib"))

    kapt("gg.scala.commons:bukkit:4.0.7")
    compileOnly("gg.scala.commons:bukkit:4.0.7")

    kapt("gg.scala.commons:core:4.0.7")
    compileOnly("gg.scala.commons:core:4.0.7")
    compileOnly("gg.scala.commons:serversync:4.0.7")
    compileOnly("gg.scala.commons:serializers:4.0.7")

    kapt("me.lucko:helper:5.6.14")
    kapt("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    kapt("gg.scala.flavor:flavor:0.1.6")
    compileOnly("me.lucko:helper:5.6.14")
    compileOnly("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    compileOnly("gg.scala.flavor:flavor:0.1.6")

    api("com.elmakers.mine.bukkit:EffectLib:10.2")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    compileOnly("gg.scala.achievements:scala-achievements-plugin:1.0.5")
    compileOnly("gg.scala.tangerine:tangerine:1.2.4")

    compileOnly("gg.scala.store:spigot:1.0.0")
    compileOnly("gg.scala.store:shared:1.0.0")
    compileOnly("gg.scala.chattags.plugin:scala-chattags-plugin:1.2.0")
    compileOnly("gg.scala.friends:scala-friends:2.0.1")
    compileOnly("gg.scala.basics.plugin:scala-basics-plugin:1.0.6")

    compileOnly("xyz.xenondevs:particle:1.7.1")

    compileOnly("gg.scala.spigot:server:1.1.3")

    compileOnly("gg.scala.lemon:bukkit:2.0.1")
    compileOnly("gg.scala.cloudsync:spigot:1.0.3")

    compileOnly("com.github.cryptomorin:XSeries:13.2.0")
    compileOnly("io.lettuce:lettuce-core:6.2.0.RELEASE")
    compileOnly("org.mongodb:mongodb-driver-sync:4.9.0")
    compileOnly("org.json:json:20231013")
}

kotlin {
    jvmToolchain(jdkVersion = 21)
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("all")
    dependencies {
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib:.*"))
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk7:.*"))
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk8:.*"))
        exclude(dependency("org.jetbrains.kotlin:kotlin-reflect:.*"))
    }
    exclude(
        "**/*.kotlin_metadata",
        "**/*.kotlin_builtins",
        "META-INF/",
        "kotlin/**",
        "kotlinx/**",
        "org/jetbrains/kotlin/**"
    )

    archiveFileName.set(
        "core-game-extensions-all.jar"
    )
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
    options.fork()
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.javaParameters = true
    kotlinOptions.jvmTarget = "21"
}

publishing {
    repositories {
        mavenLocal()
        configureScalaRepository(dev = currentBranch == "develop")
    }

    publications {
        register(
            name = "mavenJava",
            type = MavenPublication::class
        ) {
            from(components["java"])
        }
    }
}

tasks.getByName("build")
    .dependsOn("shadowJar")

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
