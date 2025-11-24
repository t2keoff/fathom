plugins {
    `fathom-java`
    `fathom-common`
    `fathom-publish`
    `fathom-repositories`
}

dependencies {
    api(project(":fathom-common"))
    api("org.springframework.boot:spring-boot-starter-data-mongodb")
}

fathomPublish {
    artifactId = "mongo"
}
