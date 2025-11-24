plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "fathom"

include(":fathom-common")
include(":fathom-common-zookeeper")
include(":fathom-mongo")
include(":fathom-demo")