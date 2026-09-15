# Request 

A request defines a plain HTTP request.

#### Available settings are

| Name                                | Description                                                                   | Accept variable | Required | Since |
|:----------------------------------- |:----------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| [url](#url)                         | The URL to hit                                                                | &#x2713;        | &#x2713; |       |
| [server](#server)                   | The server name to use                                                        | -               | -        |       |
| [method](#method)                   | The request method                                                            | -               | -        |       |
| [headers](#headers)                 | The request header list                                                       | &#x2713;        | -        |       |
| [body](#body)                       | The request body                                                              | &#x2713;        | -        |       |
| [bodybinary](#bodybinary)           | The request body, as a base64-encoded binary payload                          | -               | -        |       |
| [parts](#parts)                     | The multipart/form-data parts                                                 | &#x2713;        | -        | 2026.3|
| [extractors](variable-extractor.md) | The extractor list                                                            | -               | -        |       |
| [content_assertions](content_assertion.md)  | The list of assertions to validate the response content                       | -               | -        | 2026.3   |
| assertions                          | Deprecated alias of `content_assertions`, read-only. Use `content_assertions` instead. | -               | -        | 7.6   |
| [duration_assertion](duration_assertion.md) | Checks that the request completed within a given duration                 | -               | -        | 2026.3 |
| [size_assertion](size_assertion.md) | The assertion to validate the response size                                   | -               | -        | 2026.3 |
| sla_profile                         | The name of the SLA profile to apply to the request                           | -               | -        | 6.9   |
| followRedirects                     | When `true`, the HTTP redirections returned by the server are followed.</br>The default value is `false`. | -               | -        |       |

#### Example 1

Defining an HTTP request with a GET method.

```yaml
request:
  url: http://petstore.swagger.io:80/v2/pet/findByStatus?status=available
```

#### Example 2

Defining an HTTP request with a GET method and a SLA profile.

```yaml
request:
  url: http://petstore.swagger.io:80/v2/pet/findByStatus?status=available
  sla_profile: MySlaProfile
```

## url

Define the URL of the HTTP request. A URL can be defined with an absolute URL or a relative URL. A relative URL requires the `server` field.

Use convention to define an URL: `http[s]://{host}[:{port}][/{path}][?{query}]`. Variables can be used from the `host`, `port`, `path` parameters and from the name/value pairs of the `query` parameter. To encode the evaluation of a variable from the name/value pairs of the `query` parameter, use convention: `__encodeURL(${my_variable})`.

#### Example 1

Defining an HTTP request with an absolute URL.

```yaml
request:
  url: http://petstore.swagger.io:80/v2/pet/findByStatus?status=available
```

#### Example 2

Defining an HTTP request with a relative URL.

```yaml
request:
  url: /v2/pet/findByStatus?status=available
  server: server_petstore
```

#### Example 3

Defining an HTTP request with an absolute URL in using some variables.

```yaml
request:
  url: http://${var_host}:${var_port}/v2/pet/findByStatus?status=${var_status_value}
```

#### Example 4

Defining an HTTP request with an absolute URL in encoding the evaluation of a variable.

```yaml
request:
  url: /v2/pet/findByStatus?status=__encodeURL(${var_status_value})
  server: server_petstore
```

## method

Define the request method to use to HTTP request.

The available values are:
* `GET`
* `POST`
* `HEAD`
* `PUT`
* `DELETE`
* `OPTIONS`
* `TRACE`
* `{method-name}` for `CUSTOM` case

The default value is "GET".

#### Example

Defining an HTTP request with a POST method.

```yaml
request:
  url: https://petstore.swagger.io/v2/pet
  method: POST
  headers:
  - accept: application/json
  - Content-Type: application/json
  body: |
    {
      "id": 0,
      "category": {
        "id": 0,
        "name": "string"
      },
      "name": "doggie",
      "photoUrls": [
        "string"
      ],
      "tags": [
        {
          "id": 0,
          "name": "string"
        }
      ],
      "status": "available"
    }  
```

## server

Define the name of the [server](server.md) to use for the HTTP request. The `server` field is required if a relative URL is defined in the `url` field.

#### Example

Defining an HTTP request with a relative URL.

```yaml
request:
  url: /v2/pet/findByStatus?status=available
  server: server_petstore
```

## headers

Define the headers to attach to the HTTP request with the following format:

```yaml
headers: 
  header-name: header-value
```

The `header-name` parameter represents the header name. This parameter is required.<br> 
The `header-value` parameter represents the header value. This parameter can be optional and can use a variable.

#### Example

Defining an HTTP request with a header.

```yaml
request:
  url: http://petstore.swagger.io:80/v2/pet/findByStatus?status=available
  headers:
  - accept: application/json
  - Content-Type: ${var_content_type}
```

## body

Define the request body to use for the HTTP request. Variables can be used in the request body. 

In using the `Content-Type` header with `application/x-www-form-urlencoded`, the variables can be used from the name/value pairs of the request body. To encode the evaluation of a variable from the name/value pairs, use convention: `__encodeURL(${my_variable})`.

> Multipart/form-data is defined with [`parts`](#parts), not `body`. A binary body is defined with `bodybinary` (Base64-encoded) instead of `body`.

#### Example 1

Defining an HTTP request with a JSon body in using some variables.

```yaml
request:
  url: https://petstore.swagger.io/v2/pet
  method: POST
  headers:
  - accept: application/json
  - Content-Type: application/json
  body: |
    {
      "id": 0,
      "category": {
        "id": ${var_category_id},
        "name": "${var_category_name}"
      },
      "name": "doggie",
      "photoUrls": [
        "string"
      ],
      "tags": [
        {
          "id": 0,
          "name": "string"
        }
      ],
      "status": "${var_status}"
    }  
```

#### Example 2

Defining an HTTP request with a Form body in using some variables.

```yaml
request:
  url: https://www.compagny.com/select?animal=dog
  method: POST
  headers:
  - Content-Type: application/x-www-form-urlencoded
  body: |
    name=__encodeURL(${var_dog_name})&breed=__encodeURL(${var_dog_breed})
```

## bodybinary

Define a binary request body, encoded in base64. Use it for payloads such as `application/octet-stream` or any other binary format.

Variables cannot be used in `bodybinary`

The `body` and `bodybinary` fields are mutually exclusive: a request must define at most one of them.

Like `body`, `bodybinary` is only sent for the `POST` and `PUT` methods, and is ignored for the others.

#### Example

Defining an HTTP request sending the bytes of `Hello binary world!`.

```yaml
request:
  url: https://www.compagny.com/upload
  method: POST
  headers:
  - Content-Type: application/octet-stream
  bodybinary: SGVsbG8gYmluYXJ5IHdvcmxkIQ==
```

## parts

Define the multipart/form-data parts of the HTTP request. Use `parts` instead of `body` for multipart uploads.

Each part requires a `name`. A text part uses `value`. A file part uses `source_filename` (file on disk, relative to the NeoLoad project folder) and optionally `filename` (name sent to the server).

In CheckVU CLI, `source_filename` must stay inside the project folder: absolute paths and `../` traversal are rejected.

#### Available settings are

| Name              | Description                                                                 | Accept variable | Required | Since |
|:----------------- |:--------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name              | The form field name                                                         | &#x2713;        | &#x2713; | 2026.3|
| content_type      | The part Content-Type                                                       | &#x2713;        | -        | 2026.3|
| charset           | The part charset                                                            | &#x2713;        | -        | 2026.3|
| transfer_encoding | The part Content-Transfer-Encoding                                          | &#x2713;        | -        | 2026.3|
| value             | The text content of the part                                                | &#x2713;        | -        | 2026.3|
| filename          | The file name sent to the server                                            | &#x2713;        | -        | 2026.3|
| source_filename   | The path of the file used as part content, relative to the project folder   | &#x2713;        | -        | 2026.3|

#### Example

Defining an HTTP request with a text part and a file part.

```yaml
request:
  url: https://example.com/upload
  method: POST
  parts:
  - name: comment
    content_type: text/plain
    charset: UTF-8
    transfer_encoding: 8bit
    value: hello
  - name: file
    content_type: image/jpeg
    filename: upload.jpg
    source_filename: upload.jpg
```

