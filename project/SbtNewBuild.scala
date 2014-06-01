import sbt._
import Keys._

object SbtNewBuild extends Build {
	// make library => 'sbt + package' & 'sbt + make-pom'

	lazy val root = Project(id = "sbt-new", base = file("."),
		settings = Project.defaultSettings ++ Seq(
			name := "sbt-new",

			organization := "com.github.simplyscala",
			description := "provides sbt plugin to create sbt project with \"clasic\" structure (src/main/scala, .gitignore etc...)",

			version := "0.1-SNAPSHOT",

			scalaVersion := "2.10.4",

			publishMavenStyle := true,
			publishArtifact in Test := false,
			pomIncludeRepository := { _ => false },

			pomExtra := (
				<url>https://github.com/SimplyScala/sbt-new</url>
					<licenses>
						<license>
							<name>GPLv3</name>
							<url>http://www.gnu.org/licenses/gpl-3.0.html</url>
							<distribution>repo</distribution>
						</license>
					</licenses>
					<scm>
						<url>git@github.com:SimplyScala/sbt-new.git</url>
						<connection>scm:git:git@github.com:SimplyScala/sbt-new.git</connection>
					</scm>
					<developers>
						<developer>
							<id>ugobourdon</id>
							<name>bourdon.ugo@gmail.com</name>
							<url>https://github.com/ubourdon</url>
						</developer>
					</developers>
				),

			publishTo <<= version { v: String =>
				val nexus = "https://oss.sonatype.org/"
				if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
				else Some("releases" at nexus + "service/local/staging/deploy/maven2")
			}
		)
	)
}