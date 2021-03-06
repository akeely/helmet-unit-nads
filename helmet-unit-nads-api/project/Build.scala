import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "helmet-unit-nads-api"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "org.mongodb" %% "casbah" % "2.6.3"
    //"commons-codec" %% "commons-codec" % "1.8"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
