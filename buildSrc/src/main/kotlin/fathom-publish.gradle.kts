plugins {
    `java-library`
    `maven-publish`
}

group = "dev.takeoff.fathom"
version = "1.0.0"

publishing {
    repositories {
        mavenLocal()
    }
}

interface FathomPublishExtension {
    var artifactId: String
}

val extension = extensions.create<FathomPublishExtension>("fathomPublish")

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = extension.artifactId
                from(project.components["java"])
            }
        }
    }
}

fun RepositoryHandler.maven(
    name: String,
    url: String,
    username: String,
    password: String,
    snapshots: Boolean = true
) {
    val isSnapshot = version.toString().endsWith("-SNAPSHOT")
    if (isSnapshot && !snapshots) {
        return
    }

    this.maven {
        this.name = name
        this.url = uri(url)
        this.credentials {
            this.username = findProperty(username) as String
            this.password = findProperty(password) as String
        }
    }
}