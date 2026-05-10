plugins {
	alias(libs.plugins.fabric.loom)
}

version = providers.gradleProperty("mod_version").get()
group = providers.gradleProperty("maven_group").get()

repositories {

}

loom {
	splitEnvironmentSourceSets()

	mods {
		register("kiss-claims") {
			sourceSet(sourceSets.main.get())
//			sourceSet(sourceSets.getByName("client"))
		}
	}
}

dependencies {
	minecraft("com.mojang:minecraft:${libs.versions.minecraft.get()}")
	implementation("net.fabricmc:fabric-loader:${libs.versions.fabric.loader.get()}")
	implementation("net.fabricmc.fabric-api:fabric-api:${libs.versions.fabric.api.get()}")

	project(":core")
}

tasks.processResources {
	val version = version
	inputs.property("version", version)

	filesMatching("fabric.mod.json") {
		expand("version" to version)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 25
}

java {
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_25
	targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
	val projectName = project.name
	inputs.property("projectName", projectName)

	from("LICENSE") {
		rename { "${it}_$projectName" }
	}
}
