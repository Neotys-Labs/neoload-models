# Soap Request

A soap_request defines a SOAP request. The request body is not written inline: it is read from an
external XML file referenced by `content.path`.

#### Available settings are

| Name                     | Description                                                          | Accept variable | Required | Since |
|:------------------------ |:---------------------------------------------------------------------|:---------------:|:--------:|:-----:|
| name                     | The Soap Request name. Defaults to `soap_request`                   | -               | -        |       |
| description              | The Soap Request description                                        | -               | -        |       |
| [url](#url)              | The URL to hit                                                       | &#x2713;        | &#x2713; |       |
| [server](#server)        | The server name to use                                               | -               | -        |       |
| [parameters](#parameters)| The query string parameter list                                     | &#x2713;        | -        |       |
| [headers](#headers)      | The request header list                                              | &#x2713;        | -        |       |
| [content](#content)      | The external XML file used as the request body                      | -               | &#x2713; |       |
| [content_assertions](content_assertion.md) | The list of assertions to validate the response content | -         | -        |       |
| assertions               | Deprecated alias of `content_assertions`, read-only. Use `content_assertions` instead. | - | -   |       |
| [duration_assertion](duration_assertion.md) | Checks that the request completed within a given duration | -   | -        |       |
| [size_assertion](size_assertion.md) | The assertion to validate the response size                  | -               | -        |       |
| sla_profile              | The name of the SLA profile to apply to the request                 | -               | -        |       |
| follow_redirects         | When `true`, the HTTP redirections are followed. See [follow_redirects](request.md#follow_redirects) | - | - |       |
| charset                  | The charset used to encode the request body. See [charset](request.md#charset) | - | -  |       |
| response_storage         | Stores the response body in a file. See [response_storage](request.md#response_storage) | - | - |       |
| request_security_profile | The name of the [request profile](web_services_security.md#request_profiles) applied to the request | - | - |       |
| response_security_profile | The name of the [response profile](web_services_security.md#response_profiles) used to process the response | - | - |       |

#### Example 1

Defining a SOAP request with an absolute URL.

```yaml
soap_request:
  url: http://petstore.swagger.io:80/
  content:
    path: ./requests/mySOAPRequest.xml
```

#### Example 2

Defining a SOAP request with a relative URL and a server.

```yaml
soap_request:
  name: MySoapRequest
  description: My first SOAP request
  url: /soap
  server: server_petstore
  content:
    path: ./requests/mySOAPRequest.xml
```

#### Example 3

Defining a SOAP request with assertions and an SLA profile.

```yaml
soap_request:
  url: http://petstore.swagger.io:80/
  content:
    path: ./requests/mySOAPRequest.xml
  sla_profile: MySlaProfile
  content_assertions:
  - contains: Success
  duration_assertion:
    less_than: 2048
  size_assertion:
    less_than: 4096
```

#### Example 4

Defining a SOAP request secured by [Web Services Security](web_services_security.md) profiles.

```yaml
soap_request:
  url: /soap
  server: server_petstore
  content:
    path: ./requests/mySOAPRequest.xml
  request_security_profile: MyRequestProfile
  response_security_profile: MyResponseProfile
```

## url

Define the URL of the SOAP request. A URL can be defined with an absolute URL or a relative URL. A relative URL requires the `server` field.

## server

Define the name of the [server](server.md) to use for the SOAP request. The `server` field is required if a relative URL is defined in the `url` field.

## parameters

Define the query string parameters to attach to the SOAP request URL with the following format:

```yaml
parameters:
  parameter-name: parameter-value
```

The `parameter-name` parameter represents the parameter name. This parameter is required.<br>
The `parameter-value` parameter represents the parameter value. This parameter can be optional and can use a variable.

#### Example

Defining a SOAP request with a parameter.

```yaml
soap_request:
  url: http://petstore.swagger.io:80/
  parameters:
  - id: "42"
  content:
    path: ./requests/mySOAPRequest.xml
```

## headers

Define the headers to attach to the SOAP request with the following format:

```yaml
headers:
  header-name: header-value
```

The `header-name` parameter represents the header name. This parameter is required.<br>
The `header-value` parameter represents the header value. This parameter can be optional and can use a variable.

#### Example

Defining a SOAP request with a header.

```yaml
soap_request:
  url: http://petstore.swagger.io:80/
  headers:
  - SOAPAction: http://example.com/action
  content:
    path: ./requests/mySOAPRequest.xml
```

## content

Define the external XML file used as the SOAP request body.

| Name              | Description                                                                 | Accept variable | Required | Since |
|:----------------- |:--------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| path              | The path of the XML file used as the request body, relative to the project folder | -         | &#x2713; |       |

## follow_redirects, charset and response_storage

These settings work as on an HTTP request, see [request](request.md#follow_redirects). A SOAP request always
has a body, so `charset` is always kept. When `charset` is not set, the request body is encoded as `ISO-8859-1`,
even if the XML file declares another encoding.
