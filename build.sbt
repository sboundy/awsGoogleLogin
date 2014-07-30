scalaSource in Compile := baseDirectory.value / "app"

libraryDependencies += "org.apache.httpcomponents" % "httpcore" % "4.3"

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.3"

libraryDependencies += "com.typesafe.play" % "play-java_2.10" % "2.2.2"

libraryDependencies += "com.typesafe.play" % "play-java-ebean_2.10" % "2.2.2"

libraryDependencies += "com.google.api-client" % "google-api-client" % "1.14.1-beta"

libraryDependencies += "com.google.api-client" % "google-api-client-gson" % "1.17.0-rc"

libraryDependencies += "com.google.http-client" % "google-http-client" % "1.14.1-beta"

libraryDependencies += "com.hp.hpl.jena" % "json-jena" % "1.0"
