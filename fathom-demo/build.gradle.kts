plugins {
    `fathom-java`
    `fathom-app`
    `fathom-publish`
    `fathom-repositories`
}

dependencies {
    api(project(":fathom-mongo"))
    api("org.springframework.boot:spring-boot-starter-web")
}

fathomPublish {
    artifactId = "demo"
}
