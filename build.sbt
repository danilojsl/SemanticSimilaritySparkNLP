lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """SemanticSimilaritySparkNLP""",
    organization := "com.example",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.12.15",
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  )

val sparkVersion = "3.2.0"
val playVersion = "5.0.0"
val sparkNLPVersion = "4.2.6"

lazy val playDependencies = Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % playVersion % Test
)

lazy val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion
)

lazy val sparkNLPDependencies = Seq(
  "com.johnsnowlabs.nlp" %% "spark-nlp" % sparkNLPVersion
)

libraryDependencies ++= Seq(guice) ++ playDependencies ++ sparkDependencies ++ sparkNLPDependencies

