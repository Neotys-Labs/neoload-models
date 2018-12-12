# Request 

A request defines a plain HTTP request.

#### Available settings are

| Name                                | Description                            | Accept variable   | Required/Optional |
| ----------------------------------- | -------------------------------------- | ----------------- | ----------------- |
| [url](#url)                         | The URL to hit                         | Yes               | Required          |
| [server](#server)                   | The server name to use                 | No                | Optional          |
| [method](#method)                   | The request method                     | No                | Optional          |
| [headers](#headers)                 | The request header list                | Yes               | Optional          |
| [body](#body)                       | The request body                       | Yes               | Optional          |
| [extractors](variable-extractor.md) | The extractor list                     | No                | Optional          |

#### Example

Defining a HTTP request with a GET method.

```yaml
request:
  url: http://petstore.swagger.io:80/v2/pet/findByStatus?status=available
```

## url

Define the encoded url of the HTTP request. Use strings convention to define an URL: `http[s]://{host}[:{port}][/{path}][?{query}]`

A URL can be defined with an absolute URL or a relative URL. A relative URL requires the `server` field.<br/>
The `host`, `port`, `path` and `query` parameters can use a variable.

If the evaluation of a variable must be encoded from the `query` parameter, use string convention to encode: `__encodeURL(${my_variable})`.

#### Example 1

Defining a HTTP request with an absolute URL.

```yaml
request:
  url: http://petstore.swagger.io:80/v2/pet/findByStatus?status=available
```

#### Example 2

Defining a HTTP request with a relative URL.

```yaml
request:
  url: /v2/pet/findByStatus?status=available
  server: petstore.swagger.io
```

#### Example 3

Defining a HTTP request with an absolute URL in using some variables.

```yaml
request:
  url: http://${var_host}:${var_port}/v2/pet/findByStatus?status=${var_status}
```

#### Example 4

Defining a HTTP request with an absolute URL in encoding a variable.

```yaml
request:
  url: /v2/pet/findByStatus?status=__encodeURL(${var_status})
  server: petstore.swagger.io
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
* <method name> for `CUSTOM` case

The default value is "GET".

#### Example

Defining a HTTP request with a GET method.

```yaml
request:
  url: http://petstore.swagger.io:80/v2/pet/findByStatus?status=available
  method: GET
```

#### Example

Defining a HTTP request with a CUSTOM method.

```yaml
request:
  url: http://www.compagny.com/select?name=product
  method: MY_CUSTOM_METHOD
```

## server

Define the [server](#server) to use to HTTP request. The `server` field is required if a relative URL is defined in the `url` field.

#### Example

Defining a HTTP request with a relative URL.

```yaml
request:
  url: /v2/pet/findByStatus?status=available
  server: petstore.swagger.io
```

## headers

Define the headers to attach to HTTP request. Use special strings convention to define the header list:

```yaml
headers: 
  header-name: header-value
```

The `header-name` parameter represent the header name. This parameter is required.<br> 
The `header-value` parameter represent the header value. This parameter can be optional and use a variable.

#### Example 1

Defining a HTTP request with a header.

```yaml
request:
  url: http://petstore.swagger.io:80/v2/pet/findByStatus?status=available
  headers:
  - accept: application/json
```

#### Example 2

Defining a HTTP request with a header in using a variable.

```yaml
request:
  url: http://petstore.swagger.io:80/v2/pet/findByStatus?status=available
  headers:
  - accept: ${var_accept}
```

## body

Define the request body to use to HTTP request. 

The variables can be used in the request body. 

If the request header uses the content type `application/x-www-form-urlencoded`, the variables can be also used in the parameter name and value of the request body. If the evaluation of a variable must be encoded, use string convention to encode: `__encodeURL(${my_variable})`

#### Example 1

Defining a HTTP request with a JSon body in using some variables.

```yaml
request:
  url: https://petstore.swagger.io/v2/pet
  method: POST
  headers:
  - accept: application/json
  - Content-Type: application/json
  body: |
    {"id":${var_id},"category":{"id":${var_category_id},"name":${var_category_id}},"name":${var_name},"photoUrls":["string"],"tags":[{"id": 0,"name":"string"}],"status": "available"}
```

#### Example 2

Defining a HTTP request with a Form body in using some variables.

```yaml
request:
  url: https://www.compagny.com/select?name=dog
  method: POST
  body: |
    name=__encodeURL(${var_dog_name})&breed=__encodeURL(${var_dog_breed})
```


