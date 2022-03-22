plugins {
    id("fabric-loom") version "0.12-SNAPSHOT"
}

base {
    group = property("maven_group") as String
    version = property("mod_version") as String
    archivesName.set(property("archives_base_name") as String)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    minecraft("com.mojang", "minecraft", property("minecraft_version") as String)
    modImplementation("net.fabricmc", "fabric-loader", property("loader_version") as String)
    mappings(loom.officialMojangMappings())
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
