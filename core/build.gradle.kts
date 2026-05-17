plugins {
    java
}

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

tasks.test {
    useJUnitPlatform()
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

dependencies {
    implementation(libs.slf4j)
    implementation(libs.jtoml)
    implementation(libs.jspecify)

    implementation(libs.jdbi.core)
    implementation(libs.jdbi.sqlobject)
    implementation(libs.jdbi.mysql)

    testImplementation(libs.bundles.junit)
}