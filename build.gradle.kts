/*
 *  Copyright Gergely Nagy <greg@webhejj.hu>
 *
 *  Licensed under the Apache License, Version 2.0;
 *  you may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */

plugins {
    `java-library`
    `maven-publish`
    id("com.github.ben-manes.versions") version "0.51.0"
}

group = "hu.webhejj"
version = "0.1.0"
description = "Webhejj Tables Java utility library"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.opencsv:opencsv:5.9")
    implementation("org.apache.poi:poi-ooxml:5.3.0")
    implementation("hu.webhejj:webhejj-commons:2.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.3")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
