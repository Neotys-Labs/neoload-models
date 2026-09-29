# WebSocket Channel

The WebSocket Channel Action opens a WebSocket connection through an HTTP upgrade and keeps it open for the rest of the iteration. Apart from `messages_mapping` it is defined like a [request](request.md): `url`, `server`, `headers` and `extractors`.

The upgrade handshake is a GET with no body, per RFC 6455, so the channel has no `method`, `body` or `parts` settings.

A channel has no identifier of its own: it is designated by its complete path, from where it is declared down to its `name` (see [Referencing a channel](websocket_request.md#referencing-a-channel)). Two channels may therefore share a `name` as long as they are in different containers.

#### Available settings
| Name                                | Description                                                                                           | Accept variable | Required | Since |
|:----------------------------------- |:----------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name                                | The WebSocket Channel name                                                                            | -               | &#x2713; | 2026.3 |
| description                         | The WebSocket Channel description                                                                     | -               | -        | 2026.3 |
| url                                 | The channel URL. Accepts the `ws`/`wss` schemes as well as `http`/`https`, or a path relative to `server` | &#x2713;    | &#x2713; | 2026.3 |
| server                              | The name of the server to use when `url` is a relative path                                           | -               | -        | 2026.3 |
| headers                             | The headers of the upgrade request                                                                    | &#x2713;        | -        | 2026.3 |
| extractors                          | Variable extractors applied to the channel response                                                   | -               | -        | 2026.3 |
| messages_mapping                    | How to extract the correlation id from an inbound frame. Mandatory as soon as a synchronous `websocket_request` uses the channel | - | - | 2026.3 |

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

#### Example

Defining MyChannel with a correlation-id mapping.

```yaml
- websocket_channel:
    name: MyChannel
    description: My first WebSocket channel
    url: wss://host:443/socket
    headers:
    - Origin: https://host
    extractors:
    - name: session_id
      jsonpath: $.session
    messages_mapping:
      jsonpath: $.correlationId
```
