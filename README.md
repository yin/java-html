java-html
=========

java-html is an example of simple embedded server with a web service. Communication is based on jsonrpc4j and the
embedded web server is Jetty. There is a simple Web UI server from localhost:8080 and a web service on
localhost:8080/api.

Building and running
====================

````sh
mvn clean install
mvn exec:java
````

Now go to http://localhost:8080/

Options
=======

After building the sources you can run the server directly:

````sh
java -jar target/extractor-0.0.1.jar
````

You can change the server port by passing the --port argument:

````sh
java -jar target/extractor-0.0.1.jar --port 12345
````
