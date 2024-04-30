plugins {
    id("fabric-loom") version "1.6-SNAPSHOT"
}

base {
    group = property("maven_group") as String
    version = property("mod_version") as String
    archivesName.set(property("archives_base_name") as String)
}

java.toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    maven(url = "https://maven.terraformersmc.com/") // mod-menu
}

dependencies {
    minecraft("com.mojang", "minecraft", property("minecraft_version") as String)
    compileOnly("ca.weblite", "java-objc-bridge", "1.1")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc", "fabric-loader", property("loader_version") as String)
    modImplementation(fabricApi.module("fabric-command-api-v2", property("fabric_version") as String))
    modImplementation(fabricApi.module("fabric-resource-loader-v0", property("fabric_version") as String))

    modImplementation("com.terraformersmc", "modmenu", property("mod-menu_version") as String)
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
