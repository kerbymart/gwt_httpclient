# HttpClient for GWT

A modern HTTP client library for Java applications, designed for web environments and compatible with Google Web Toolkit (GWT).

## Features

- RESTful API support (GET, POST, PUT, DELETE)
- Promise-based asynchronous API
- JSON request/response handling
- Header and query parameter support
- Basic authentication
- Comprehensive error handling
- Timeout support

## Installation

Add the HttpClient dependency to your project. For Maven projects:

```xml
<dependency>
    <groupId>com.divroll</groupId>
    <artifactId>http-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

For GWT projects, ensure you inherit the required modules in your .gwt.xml file:

```xml
<inherits name="com.divroll.http.HttpClient"/>
```

## Usage Examples

### Making a GET Request

```java
HttpClient.get("https://api.example.com/posts/1")
    .header("accept", "application/json")
    .asJson()
    .then(response -> {
        // Handle successful response
        JsonNode body = response.getBody();
        System.out.println("Status: " + response.getStatus());
        System.out.println("Response body: " + body.toString());
    }, error -> {
        // Handle error
        System.err.println("Request failed: " + error.getMessage());
    });
```

### Making a POST Request

```java
// Create payload
JSONObject payload = new JSONObject();
payload.put("title", "New Post");
payload.put("body", "Content of the new post");
payload.put("userId", 1);

HttpClient.post("https://api.example.com/posts")
    .header("Content-Type", "application/json")
    .body(payload.toString())
    .asJson()
    .then(response -> {
        // Handle successful response
        JsonNode body = response.getBody();
        System.out.println("Created post with ID: " + body.getObject().getInt("id"));
    }, error -> {
        // Handle error
        System.err.println("Request failed: " + error.getMessage());
    });
```

### Making a PUT Request

```java
JSONObject payload = new JSONObject();
payload.put("id", 1);
payload.put("title", "Updated Title");
payload.put("body", "Updated content");

HttpClient.put("https://api.example.com/posts/1")
    .header("Content-Type", "application/json")
    .body(payload.toString())
    .asJson()
    .then(response -> {
        // Handle successful response
    }, error -> {
        // Handle error
    });
```

### Making a DELETE Request

```java
HttpClient.delete("https://api.example.com/posts/1")
    .asJson()
    .then(response -> {
        System.out.println("Post deleted successfully");
    }, error -> {
        System.err.println("Failed to delete post: " + error.getMessage());
    });
```

### Adding Query Parameters

```java
HttpClient.get("https://api.example.com/posts")
    .queryString("userId", "1")
    .queryString("sort", "desc")
    .asJson()
    .then(response -> {
        // Process list of posts
    }, error -> {
        // Handle error
    });
```

### Using Basic Authentication

```java
HttpClient.get("https://api.example.com/protected-resource")
    .basicAuth("username", "password")
    .asJson()
    .then(response -> {
        // Process response
    }, error -> {
        // Handle error
    });
```

### Error Handling

The library provides specialized exceptions for different HTTP status codes:

- `BadRequestException` (400)
- `UnauthorizedRequestException` (401)
- `NotFoundRequestException` (404)
- `ClientErrorRequestException` (4xx)
- `ServerErrorRequestException` (5xx)
- `TimeoutException`

You can catch these specific exceptions or handle errors through the promise rejection handler:

```java
HttpClient.get("https://api.example.com/resource")
    .asJson()
    .then(response -> {
        // Success handler
    }, error -> {
        if (error instanceof NotFoundRequestException) {
            System.err.println("Resource not found");
        } else if (error instanceof UnauthorizedRequestException) {
            System.err.println("Authentication required");
        } else {
            System.err.println("Request failed: " + error.getMessage());
        }
    });
```

### Setting Timeouts

```java
HttpClient.get("https://api.example.com/resource")
    .setTimeout(30000) // 30 seconds timeout
    .asJson()
    .then(/* ... */);
```

## Advanced Usage

### Working with Headers

```java
// Add single header
HttpClient.get(url).header("Custom-Header", "value");

// Add multiple headers
Set<Header> headers = new HashSet<>();
headers.add(new Header("Header1", "Value1"));
headers.add(new Header("Header2", "Value2"));
HttpClient.get(url, headers, null);
```

### Working with Responses

The library provides several ways to handle responses:

1. **As JSON** (automatically parsed):
```java
.asJson().then(response -> {
    JsonNode json = response.getBody();
    // Work with JSON structure
});
```

2. **As String** (raw response):
```java
.asString().then(response -> {
    String body = response.getBody();
    // Process raw string
});
```

3. **As Binary** (experimental):
```java
.asBinary().then(response -> {
    InputStream stream = response.getBody();
    // Process binary data
});
```

## Configuration

The default timeout is set to 60 seconds (60000 ms). You can change this per request:

```java
HttpClient.get(url)
    .setTimeout(30000) // 30 seconds
    .asJson();
```

## License

This project is licensed under the Apache License, Version 2.0. See the LICENSE file for details.
