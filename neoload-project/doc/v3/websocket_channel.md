# WebSocket Channel

The WebSocket Channel Action opens a WebSocket connection through an HTTP upgrade and keeps it open for the rest of the iteration. Apart from `messages_mapping` and `push_messages` it is defined like a [request](request.md): `url`, `server`, `headers` and `extractors`.

The upgrade handshake is a GET with no body, per RFC 6455, so the channel has no `method`, `body` or `parts` settings.

`id` is the handle a `websocket_request` points at through its `channel` property. It must be unique within a User Path, and it is deliberately restricted to letters, digits, `.`, `_` and `-` so that a reference stays unquoted in YAML and can be compared literally. It is not the same thing as `name`: renaming a channel does not break the requests that use it.

#### Available settings
| Name                                | Description                                                                                           | Accept variable | Required | Since |
|:----------------------------------- |:----------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name                                | The WebSocket Channel name                                                                            | -               | &#x2713; | 2026.3 |
| id                                  | The handle a `websocket_request` references through `channel`. Unique within the User Path. Matches `^[A-Za-z0-9][A-Za-z0-9._-]*$` | - | &#x2713; | 2026.3 |
| description                         | The WebSocket Channel description                                                                     | -               | -        | 2026.3 |
| url                                 | The channel URL. Accepts the `ws`/`wss` schemes as well as `http`/`https`, or a path relative to `server` | &#x2713;    | &#x2713; | 2026.3 |
| server                              | The name of the server to use when `url` is a relative path                                           | -               | -        | 2026.3 |
| headers                             | The headers of the upgrade request                                                                    | &#x2713;        | -        | 2026.3 |
| extractors                          | Variable extractors applied to the channel response                                                   | -               | -        | 2026.3 |
| messages_mapping                    | How to extract the correlation id from an inbound frame. Mandatory as soon as a synchronous `websocket_request` uses the channel | - | - | 2026.3 |
| push_messages                       | The inbound messages the channel expects to receive                                                   | -               | -        | 2026.3 |

NeoLoad stores a channel URL with the `http`/`https` scheme, derived from whether the server uses SSL. `ws` and `wss` are accepted here for convenience when writing as-code by hand, so a project exported from NeoLoad is not necessarily textually identical to one you wrote yourself.

#### messages_mapping

The value this extracts from each inbound frame is compared against the `mapping_id` of a synchronous `websocket_request` in order to pair a response with the request waiting for it.

| Name           | Description                                                                          | Required |
|:-------------- |:------------------------------------------------------------------------------------ |:--------:|
| xpath          | XPath expression to extract the correlation id. Mutually exclusive with `jsonpath`    | -        |
| jsonpath       | JSONPath expression to extract the correlation id. Mutually exclusive with `xpath`    | -        |
| regexp         | Regular expression to extract the correlation id. Defaults to `(.*)`                  | -        |
| template       | Template applied to the extraction result. Defaults to `$1$`                           | -        |
| decode         | Decoder to apply before extracting: `html`, `url` or `custom`                          | -        |
| custom_decoder | Class name of the decoder to use, only when `decode` is `custom`                       | -        |
| encoding       | Encoding of the inbound message content                                                | -        |

```yaml
messages_mapping:
  jsonpath: $.correlationId
```

#### push_messages

| Name        | Description                                                                                  | Required |
|:----------- |:-------------------------------------------------------------------------------------------- |:--------:|
| name        | The push message name. Defaults to `push_message`                                             | -        |
| description | The push message description                                                                  | -        |
| conditions  | Conditions identifying the message. When empty, the message matches every inbound frame        | -        |
| match       | How `conditions` are combined: `any` (default) or `all`                                        | -        |
| charset     | Charset used to decode the inbound message content                                             | -        |
| extractors  | Variable extractors applied to the inbound message                                             | -        |

#### Example

Defining MyChannel with a correlation-id mapping and two expected inbound messages.

```yaml
- websocket_channel:
    name: MyChannel
    id: ws_main
    description: My first WebSocket channel
    url: wss://host:443/socket
    headers:
    - Origin: https://host
    extractors:
    - name: session_id
      jsonpath: $.session
    messages_mapping:
      jsonpath: $.correlationId
    push_messages:
    - name: pong
      conditions:
      - "'${msg_type}' == 'pong'"
    - name: broadcast
      charset: UTF-8
```
