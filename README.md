![Logo](logo.jpg)

# Trustly Technical Challenge for Developers

## Objective

Develop an API that returns the total number of lines and the total number of bytes of all the
files of a given public Github repository, grouped by file extension.

## Requirements

* Your API must be written using Java 8 or newer, ECMAscript 2015 or newer, or C# 8.0;
* Data must be retrieved from Github website by using web scraping techniques. **Do not use Github’s API**;
* **Do not use web scraping libraries.** We would like to know your ideas on how it can be done;
* Your API must support thousands of concurrent requests;
* We think it’s ok if the first request to a particular repository takes some time to respond (since you depend on Github website response times), but we don’t expect the subsequent requests to be long;
* We don’t expect to get timeout errors;
* We must understand your code and use your API without having to ask you any questions. Our primary language is English so please use it on comments and documentation;
* We’d like to see **SOLID principles** in your solution;
* You are free to choose your API contracts (parameters and response formats) but we’d like to be able to integrate it with any other existing solutions;
* You don’t need to persist any data (but feel free to do it if you want);
* We’d like to see at least one automated test;
* You must deploy your solution to a cloud provider **like** Amazon AWS or Heroku and send us the link to access it. It’s a plus if you publish a Docker image with your application (including its dependencies) in a registry like Docker Hub and let us know how to get it. 

## Tools

* JDK 8 - Java Development Kit;
* Spring - Java Framework;
* Swagger - API Documentation;
* Digital Ocean - Cloud provider.

## Public endpoints

* [/getExtensionMetadata](http://emersoncr.com.br:8080/getExtensionMetadata?repository=github.com/ECRomaneli/githubscraper), retrieve number of bytes and lines grouped by file extension;
* [/getFilesGroupedByExtension](http://emersoncr.com.br:8080/getFilesGroupedByExtension?repository=github.com/ECRomaneli/githubscraper), retrieve files grouped by extension;
* [Swagger Documentation](http://emersoncr.com.br:8080/swagger-ui.html#/scraping-service).

## Solution

1. Since no scraping tools can be used, scraping tools were created from the beginning in a completely generic way without linking to the solution;
2. A structure was created to hold the data of the scraped site;
3. In-depth search was used to search all files in the pre-created structure using recursion;
4. The information is cached via Singleton into RAM memory;
5. The information was grouped and made available via DTO only as necessary.

## Author

* Created and maintained by [Emerson C. Romaneli](https://github.com/ECRomaneli) (@ECRomaneli).

## License

* Under [MIT License](LICENSE).