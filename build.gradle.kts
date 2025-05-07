import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

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
    signing
    id("com.github.ben-manes.versions") version "0.51.0"
}

group = "hu.webhejj"
version = "1.0.0"
description = "Webhejj Tables Java utility library"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    api("com.opencsv:opencsv:5.11")
    api("org.apache.poi:poi-ooxml:5.4.1")

    testImplementation(platform("org.junit:junit-bom:5.12.2"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                packaging = "jar"
                name.set("webhejj-tables")
                description.set(project.description)
                url.set("https://github.com/gnagy/webhejj-tables")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("gnagy")
                        name.set("Gergely Nagy")
                        email.set("greg@webhejj.hu")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/gnagy/webhejj-tables.git")
                    developerConnection.set("scm:git:ssh://github.com/gnagy/webhejj-tables.git")
                    url.set("https://github.com/gnagy/webhejj-tables")
                }
            }
        }
    }

    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = project.findProperty("ossrhUsername") as String?
                password = project.findProperty("ossrhPassword") as String?
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

// https://github.com/ben-manes/gradle-versions-plugin
tasks.withType<DependencyUpdatesTask> {
    revision = "release"
    rejectVersionIf {
        setOf("-alpha", "-beta", "-rc", "-dev").any { candidate.version.lowercase().contains(it) }
    }
}
