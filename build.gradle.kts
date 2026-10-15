import java.util.zip.ZipFile

plugins {
    id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT"
    `maven-publish`
}

group = property("maven_group") as String
version = "${property("mod_version")}+fly-mc${property("mc_line")}"

base {
    archivesName.set(property("archives_base_name") as String)
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://api.modrinth.com/maven") {
        content { includeGroup("maven.modrinth") }
    }
    flatDir {
        dirs("libs", "../../create-rei/CreateReiViewer-Fly/build/libs")
    }
}

val recipeViewer = ":CreateReiViewer:${property("createreiviewer_version")}"

loom {
    accessWidenerPath = file("src/main/resources/create_aquatic_ambitions.accesswidener")
    mods {
        create("create_aquatic_ambitions") {
            sourceSet(sourceSets.main.get())
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
    implementation("maven.modrinth:create-fly:${property("create_fabric_version")}")

    compileOnly(recipeViewer)
    include(recipeViewer)
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(25)
    options.compilerArgs.addAll(listOf("-Xmaxerrs", "10000", "-Xmaxwarns", "1000"))
}

tasks.processResources {
    val modMetadata = mapOf(
        "version" to project.version.toString(),
        "mc_line" to project.property("mc_line") as String,
        "minecraft_dependency_version" to project.property("minecraft_dependency_version") as String,
        "fabric_loader_version" to project.property("fabric_loader_version") as String,
        "create_fabric_version_range" to project.property("create_fabric_version_range") as String,
    )
    inputs.properties(modMetadata)
    filesMatching("fabric.mod.json") {
        expand(modMetadata)
    }
}

tasks.jar {
    from("LICENSE")
    from("NOTICE")
}

tasks.named<Jar>("sourcesJar") {
    from("LICENSE")
    from("NOTICE")
}

val ownedSourceRoots = listOf("net/davio/aquaticambitions/")
val allowedResourceRoots = listOf(
    "assets/create_aquatic_ambitions/",
    "data/create_aquatic_ambitions/",
    "data/c/",
    "data/create/",
    "data/minecraft/",
)
val allowedRootFiles = listOf(
    "fabric.mod.json",
    "create_aquatic_ambitions.accesswidener",
    "LICENSE",
    "NOTICE",
)

fun foreignEntries(archive: File): List<String> {
    val bad = mutableListOf<String>()
    ZipFile(archive).use { zip ->
        val entries = zip.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            if (entry.isDirectory) continue
            val name = entry.name
            if (name.startsWith("META-INF/")) continue
            if (name in allowedRootFiles) continue
            if (allowedResourceRoots.any { name.startsWith(it) }) continue
            if (name.endsWith(".class") || name.endsWith(".java")) {
                if (ownedSourceRoots.none { name.startsWith(it) }) bad.add(name)
                continue
            }
            bad.add(name)
        }
    }
    return bad
}

val checkNamespaces = tasks.register("checkNamespaces") {
    description = "Fails if the mod jar or the sources jar carries an entry outside the mod's own namespace."
    group = "verification"
    dependsOn(tasks.named("jar"), tasks.named("sourcesJar"))
    val archives = listOf(tasks.named<Jar>("jar"), tasks.named<Jar>("sourcesJar")).map { it.flatMap { jar -> jar.archiveFile } }
    doLast {
        val files = archives.map { it.get().asFile }
        val problems = files.flatMap { archive ->
            foreignEntries(archive).map { "${archive.name}: $it" }
        }
        if (problems.isNotEmpty()) {
            throw GradleException(
                "Foreign entries in the published artifacts:\n" + problems.joinToString("\n")
            )
        }
        logger.lifecycle("checkNamespaces: ${files.size} archives clean")
    }
}

tasks.named("check") {
    dependsOn(checkNamespaces)
}
