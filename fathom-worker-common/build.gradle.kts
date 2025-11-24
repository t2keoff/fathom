plugins {
    `fathom-java`
    `fathom-common`
    `fathom-publish`
    `fathom-repositories`
}

dependencies {
    api(project(":fathom-common"))
}

fathomPublish {
    artifactId = "worker-common"
}
