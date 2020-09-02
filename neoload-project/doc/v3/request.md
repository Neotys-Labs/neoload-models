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
| [extractors](variable-extractor.md) | The extractor list                                                            | -               | -        |       |
| [assert_content](assert_content.md) | The list of assertions allowing to check the validity of the response content | -               | -        | 7.6   |
| sla_profile                         | The name of the SLA profile to apply to the Request                           | -               | -        | 6.9   |

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

> The bodies containing a binary or multipart/form-data data are not yet supported. 

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



