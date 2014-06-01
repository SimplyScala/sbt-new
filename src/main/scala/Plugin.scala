import sbt._
import Keys._

object Plugin extends Plugin {
	import scala.sys.process._

	override lazy val projectSettings = Seq(commands += myCommand)

    lazy val myCommand = Command.command("new") { (state: State) =>
	    scalaBuildFile(projectName)

	    srcMainScala()
	    srcTestScala()

	    readMe(projectName)
	    gitignore(projectName)

	    state
    }

	private lazy val projectName: String = {
		val pwd = "pwd" !!
		val assumedProjectName = pwd.split("/").last.trim

		print(s"project name [$assumedProjectName] : ")
		val projectName = {
			val prompt = readLine()
			if(prompt.trim.isEmpty) assumedProjectName
			else prompt
		}
		projectName
	}

	private def scalaBuildFile(projectName: String) = {
		// Stratégies pour le nom du Build.scala # poser la question à l'utilisateur
			// 1. myProject     => MyProjectBuild
			// 2. my-project    => MyProjectBuild
		val assumedBuildFile =
			(if(projectName.contains("-")) projectName.split("-").map { s => s.head.toUpper + s.tail}.mkString
			else projectName.head.toUpper + projectName.tail) + "Build.scala"

		print(s"build file [$assumedBuildFile] : ")
		val buildFile = {
			val prompt = readLine()
			val fileName = if(prompt.trim.isEmpty) assumedBuildFile else prompt
			new File(s"./project/$fileName")
		}

		// http://www.scala-sbt.org/0.13.0/api/index.html#sbt.IO$
		IO.touch(buildFile)

		writeScalaBuildFile(projectName, buildFile)
	}

	private def srcMainScala() = "mkdir -p src/main/scala" !!
	private def srcTestScala() = "mkdir -p src/test/scala" !!

	private def readMe(projectName: String) = {
		val readme = new File("README.md")

		val lines = Seq(
			projectName,
			"=================================="
		)

		IO.touch(readme)
		IO.writeLines(readme, lines)
	}

	private def gitignore(projectName: String) = {
		val gitignore = new File(".gitignore")

		val lines = Seq(
			"logs",
			"project/project",
			"project/target",
			"target",
			"tmp",
			".history",
			"dist",
			"/.idea",
			"/*.iml",
			"/out",
			"/.idea_modules",
			"/.classpath",
			"/.project",
			"/RUNNING_PID",
			"/.settings",
			"public/builds"
		)

		IO.touch(gitignore)
		IO.writeLines(gitignore, lines)
	}

	private def writeScalaBuildFile(projectName: String, buildFile: File) = {
		val className = buildFile.getName.split("\\.").head

		val lines = Seq(
			"import sbt._",
			"import sbt.Keys._",
		    "",
			s"object $className extends Build {",
			"",
		    s"""    lazy val root = Project(id = \"$projectName\", base = file(\".\"),""",
			"           settings = Project.defaultSettings ++ Seq(",
			s"""          name := \"$projectName\",""",
			"           version := \"0.1-SNAPSHOT\",",
		    "",
			"           libraryDependencies ++= Seq()",
			"       ))",
			"}"
		)

		IO.writeLines(buildFile, lines)
	}
}