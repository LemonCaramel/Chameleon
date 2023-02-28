plugins {
    id("fabric-loom") version "1.1-SNAPSHOT"
}

base {
    group = property("maven_group") as String
    version = property("mod_version") as String
    archivesName.set(property("archives_base_name") as String)
}

java.toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    maven(url = "https://maven.terraformersmc.com/") // mod-menu
}

dependencies {
    minecraft("com.mojang", "minecraft", property("minecraft_version") as String)
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc", "fabric-loader", property("loader_version") as String)
    modImplementation(fabricApi.module("fabric-command-api-v2", property("fabric_version") as String))

    modImplementation("com.terraformersmc", "modmenu", property("mod-menu_version") as String)
}

loom {
    accessWidenerPath.set( file("src/main/resources/chameleon-dock.accesswidener") )
}

tasks{
    processResources {
        inputs.property("version", project.version)
        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to version))
        }
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${project.name}" }
        }
    }
}
