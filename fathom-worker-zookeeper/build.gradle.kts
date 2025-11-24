plugins {
    `fathom-java`
    `fathom-common`
    `fathom-publish`
    `fathom-repositories`
}

dependencies {
    api(project(":fathom-worker-common"))
    api("org.springframework.cloud:spring-cloud-starter-zookeeper-config")
}

fathomPublish {
    artifactId = "worker-zookeeper"
}
