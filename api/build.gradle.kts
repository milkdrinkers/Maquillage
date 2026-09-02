import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import org.gradle.internal.extensions.stdlib.capitalized

plugins {
    alias(libs.plugins.publisher)
    signing
}

mavenPublishing {
    coordinates(
        groupId = "io.github.milkdrinkers",
        artifactId = base.archivesName.get().lowercase(),
        version = version.toString().let { originalVersion ->
            if (!originalVersion.contains("-SNAPSHOT"))
                originalVersion
            else
                originalVersion.substringBeforeLast("-SNAPSHOT") + "-SNAPSHOT" // Force append just -SNAPSHOT if snapshot version
        }
    )

    pom {
        name.set(base.archivesName.get().split("-").map { it.capitalized() }.joinToString("-"))
        description.set(rootProject.description.orEmpty())
        url.set("https://github.com/milkdrinkers/Maquillage")
        inceptionYear.set("2026")

        licenses {
            license {
                name.set("GNU General Public License Version 3")
                url.set("https://www.gnu.org/licenses/gpl-3.0.en.html#license-text")
                distribution.set("https://www.gnu.org/licenses/gpl-3.0.en.html#license-text")
            }
        }

        developers {
            developer {
                id.set("rooooose-b")
                name.set("Rose")
                url.set("https://github.com/rooooose-b")
                organization.set("Milkdrinkers")
            }
            developer {
                id.set("darksaid98")
                name.set("darksaid98")
                url.set("https://github.com/darksaid98")
                organization.set("Milkdrinkers")
            }
        }

        scm {
            url.set("https://github.com/milkdrinkers/Maquillage")
            connection.set("scm:git:git://github.com/milkdrinkers/Maquillage.git")
            developerConnection.set("scm:git:ssh://github.com:milkdrinkers/Maquillage.git")
        }
    }

    configure(JavaLibrary(
        javadocJar = JavadocJar.None(), // We want to use our own javadoc jar
    ))

    // Publish to Maven Central
    publishToMavenCentral(automaticRelease = true)

    // Sign all publications
    signAllPublications()
}

signing {
    isRequired = false // Skip signing if no credentials are provided, e.g. for local publishing
}
