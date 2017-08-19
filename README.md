java-html
=========

java-html is an example of simple embedded server with a web service. Communication is based on jsonrpc4j and the
embedded web server is Jetty. There is a simple Web UI server from localhost:8080 and a web service on
localhost:8080/api.

Building and running
====================

The projet is hosted on Github and build by Maven. 

````sh
mvn clean install
````

Now go to http://localhost:8080/

Options
=======

The easiest way for running the application is to use maven:

````sh
mvn exec:java
````

After building the sources you can run the server directly using the jar package. 

````sh
java -jar target/extractor-0.0.1.jar
````

The jar package produced contains all dependencies neccessary for running the application.

You can change the server port by passing the `--port` argument:

````sh
java -jar target/extractor-0.0.1.jar --port 12345
````

Command line clients
===================

You can test the web service with dedicated jsonrpc4j client. This accepts the flag `--apiUrl`, which is the full
URL to the API (by default located on HTTP context `/api/`) 

````sh
java -cp target/extractor-0.0.1.jar com.github.yin.html.main.WeServiceClientMain <urls...> 
````

There's also a local command line utility for testing the algorithm.

````sh
java -cp target/extractor-0.0.1.jar com.github.yin.html.main.CommandLineMain <urls...> 
````
