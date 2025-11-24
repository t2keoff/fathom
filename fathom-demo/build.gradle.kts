plugins {
    `fathom-java`
    `fathom-app`
    `fathom-publish`
    `fathom-repositories`
}

dependencies {
    api(project(":fathom-mongo"))
    api(project(":fathom-worker-zookeeper"))
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.cloud:spring-cloud-starter-zookeeper-discovery")
}

fathomPublish {
    artifactId = "demo"
}
