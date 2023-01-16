## Build and run the project


To build and run the project:

Build the project. Enter: `sbt run`. The project builds and starts the embedded HTTP server. Since this downloads libraries and dependencies, the amount of time required depends partly on your connection's speed.

After the message `Server started, ...` displays, enter the following URL in a browser: <http://localhost:9000>

To run as Prod:

``
sbt runProd
``

``
sbt stopProd
``