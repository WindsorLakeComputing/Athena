import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

// Serves as a global variable
group = "com.MadHatter"
version = System.getenv("BUILD_VERSION") ?: "0.0.1-SNAPSHOT"
val snippetsDir = file("build/generated-snippets")
val javaVersion = "1.8"

buildscript {
	val kotlinVersion = "1.3.20"
	val springBootVersion = "2.1.4.RELEASE"

	// Defines where we get our dependencies
	repositories {
		mavenCentral()
	}
	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
		classpath("org.springframework.cloud:spring-cloud-contract-gradle-plugin:2.1.1.RELEASE")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
		classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
		classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
		classpath("postgresql:postgresql:9.1-901-1.jdbc4")
	}
}

plugins {
	id("org.springframework.boot").version("2.1.2.RELEASE")
	id("org.jetbrains.kotlin.jvm").version("1.2.71")
	id("org.jetbrains.kotlin.plugin.spring").version( "1.2.71")
	id("io.spring.dependency-management").version("1.0.6.RELEASE")
	id("com.moowork.node").version("1.2.0")
	id("org.jetbrains.kotlin.plugin.jpa").version("1.3.21")
	id("org.flywaydb.flyway").version("5.2.4")
	id("org.springframework.cloud.contract").version("2.1.1.RELEASE")
	id("org.asciidoctor.jvm.convert").version("2.2.0")
}

repositories {
	mavenCentral()
}

tasks {
	val copyReact by registering(Copy::class) {
		group = "build"
		description = "Assemble the react components"
		includeEmptyDirs = false
		inputs.files("frontend/src")
		from("frontend/build")
		into("src/main/resources/static")
		dependsOn(":frontend:buildApp")
	}

	"asciidoctor"(AsciidoctorTask::class) {
		println("running assciidoctor")
		attributes(mapOf(
			"snippets" to snippetsDir
		))
		
		sources(delegateClosureOf<PatternSet> {
			include("/**")
		})

		setOutputDir(file("src/main/resources/static/docs"))
	}

	"bootJar" {
		dependsOn(copyReact)
		mustRunAfter(copyReact)
		dependsOn("asciidoctor")
		mustRunAfter("asciidoctor")
	}

	"assemble" {
		dependsOn("asciidoctor")
		mustRunAfter("asciidoctor")
		doLast {
			println("artifact_path: ${project.tasks["jar"].outputs.files.singleFile}")
		}
	}

	"test" {
		outputs.dir(snippetsDir)
	}
}

val developmentOnly = configurations.create("developmentOnly")
configurations.runtimeClasspath.extendsFrom(developmentOnly)

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-cloud-connectors:2.1.4.RELEASE")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.boot:spring-boot-starter-web:2.1.3.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("io.pivotal.spring.cloud:spring-cloud-sso-connector:2.1.3.RELEASE")
	implementation("org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure:2.1.4.RELEASE")
	implementation("org.apache.commons:commons-csv:1.5")
	implementation("org.jetbrains.kotlin:kotlin-noarg:1.3.20")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.github.microutils:kotlin-logging:1.6.24")
	implementation("org.postgresql:postgresql:42.2.5")
	implementation("org.flywaydb:flyway-core:5.2.4")
	implementation("postgresql:postgresql:9.1-901-1.jdbc4")
	implementation("org.springframework.boot:spring-boot-starter-amqp:2.1.4.RELEASE")
	implementation("org.springframework.restdocs:spring-restdocs-asciidoctor:2.0.3.RELEASE")

	testImplementation("com.h2database:h2")
	testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner:2.1.0.RELEASE")
	testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier:2.1.1.RELEASE")
	testImplementation("org.springframework.boot:spring-boot-starter-test:2.1.3.RELEASE")
	testImplementation("org.springframework.security:spring-security-test:5.1.4.RELEASE")
	testImplementation("org.springframework.cloud:spring-cloud-contract-gradle-plugin:2.1.1.RELEASE")
	testImplementation("com.github.tomakehurst:wiremock:2.23.2")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc:2.0.3.RELEASE")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:Greenwich.RELEASE")
	}
}

contracts {
	basePackageForTests = "com.MadHatter.Athena.contracts"
	packageWithBaseClasses = "com.MadHatter.Athena.contracts"
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
	jvmTarget = javaVersion
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
	jvmTarget = javaVersion
}

tasks.getByName<BootRun>("bootRun") {
	environment("SPRING_PROFILES_ACTIVE", "local")
	environment("minervaUri", "http://localhost:8099")
}

