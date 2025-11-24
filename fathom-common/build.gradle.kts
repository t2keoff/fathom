plugins {
    `fathom-java`
    `fathom-common`
    `fathom-publish`
    `fathom-repositories`
}

dependencies {
    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.data:spring-data-commons:3.5.6")
}

fathomPublish {
    artifactId = "common"
}
