# WebSocket Channel

The WebSocket Channel Action opens a WebSocket connection through an HTTP upgrade and keeps it open for the rest of the iteration. Apart from `messages_mapping` it is defined like a [request](request.md): `url`, `server`, `headers` and `extractors`.

The upgrade handshake is a GET with no body, per RFC 6455, so the channel has no `method`, `body` or `parts` settings.

A channel has no identifier of its own: it is designated by its complete path, from where it is declared down to its `name`. Two channels may therefore share a `name` as long as they are in different containers.

#### Available settings
| Name                                | Description                                                                                           | Accept variable | Required | Since |
|:----------------------------------- |:----------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name                                | The WebSocket Channel name                                                                            | -               | &#x2713; | 2026.3 |
| description                         | The WebSocket Channel description                                                                     | -               | -        | 2026.3 |
| url                                 | The channel URL, with the `http` or `https` scheme, or a path relative to `server`                    | &#x2713;        | &#x2713; | 2026.3 |
| server                              | The name of the server to use when `url` is a relative path                                           | -               | -        | 2026.3 |
| headers                             | The headers of the upgrade request. `Upgrade: websocket` is implied, see [The `Upgrade` header](#the-upgrade-header) | &#x2713;        | -        | 2026.3 |
| extractors                          | Variable extractors applied to the channel response                                                   | -               | -        | 2026.3 |
| messages_mapping                    | How to extract the correlation id from an inbound frame. Mandatory as soon as a synchronous `websocket_request` uses the channel | - | - | 2026.3 |

NeoLoad stores a channel URL with the `http`/`https` scheme, derived from whether the server uses SSL, so `ws` and `wss` are not accepted: write `http://` for `ws://` and `https://` for `wss://`.

#### The `Upgrade` header

The header `Upgrade: websocket` is what makes the channel a WebSocket upgrade for NeoLoad, which refuses to open a channel without it. It is implied, so there is no need to write it in `headers`:

* when the project is loaded, NeoLoad adds `Upgrade: websocket` to a channel that has no `Upgrade` header;
* when a project is exported, `Upgrade: websocket` is not written, whatever its case. Any other value of `Upgrade` is written as it is, and makes the channel fail when it runs.

The other headers of the handshake (`Connection`, `Sec-WebSocket-Key`, `Sec-WebSocket-Version`) are built when the connection is opened (the key is generated for each one), so they are not needed either.

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
    url: https://host:443/socket
    headers:
    - Origin: https://host
    extractors:
    - name: session_id
      jsonpath: $.session
    messages_mapping:
      jsonpath: $.correlationId
```
