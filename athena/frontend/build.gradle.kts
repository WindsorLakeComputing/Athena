plugins {
    id("com.moowork.node")
}

node {
    version = "10.15.3"
    npmVersion = "6.8.0"
    download = true
}

tasks {
    register("buildApp") {
        group = "build"
        description = "Builds artifact using webpack"
        dependsOn("yarn_build")
    }
}
