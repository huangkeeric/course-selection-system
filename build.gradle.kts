plugins {
    java
}

group = "huangke"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("mysql:mysql-connector-java:8.0.27")

    testImplementation("junit", "junit", "4.12")
}
