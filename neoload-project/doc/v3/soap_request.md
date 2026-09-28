# Soap Request

A soap_request defines a SOAP request. The request body is not written inline: it is read from an
external XML file referenced by `content.path`.

#### Available settings are

| Name                     | Description                                                          | Accept variable | Required | Since |
|:------------------------ |:---------------------------------------------------------------------|:---------------:|:--------:|:-----:|
| name                     | The Soap Request name. Defaults to `soap_request`                   | -               | -        | 2026.3|
| description              | The Soap Request description                                        | -               | -        | 2026.3|
| [url](#url)              | The URL to hit                                                       | &#x2713;        | &#x2713; | 2026.3|
| [server](#server)        | The server name to use                                               | -               | -        | 2026.3|
| [content](#content)      | The external XML file used as the request body                      | -               | &#x2713; | 2026.3|
| [wsdl](#wsdl)             | The WSDL file the request originates from, for reference only       | -               | -        | 2026.3|

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

Defining a SOAP request with a WSDL reference.

```yaml
soap_request:
  name: MySoapRequest
  url: http://petstore.swagger.io:80/
  content:
    path: ./requests/mySOAPRequest.xml
  wsdl:
    path: ./requests/myService.wsdl
```

## url

Define the URL of the SOAP request. A URL can be defined with an absolute URL or a relative URL. A relative URL requires the `server` field.

## server

Define the name of the [server](server.md) to use for the SOAP request. The `server` field is required if a relative URL is defined in the `url` field.

## content

Define the external XML file used as the SOAP request body.

| Name              | Description                                                                 | Accept variable | Required | Since |
|:----------------- |:--------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| path              | The path of the XML file used as the request body, relative to the project folder | -         | &#x2713; | 2026.3|

## wsdl

Define the WSDL file this SOAP request originates from. This reference is purely informative: the request
actually sent is always the content of the `content` XML file, never rebuilt from the WSDL.

| Name              | Description                                                                 | Accept variable | Required | Since |
|:----------------- |:--------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| path              | The path of the WSDL file, relative to the project folder                  | -                | &#x2713; | 2026.3|
