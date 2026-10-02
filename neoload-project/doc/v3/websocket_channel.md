# WebSocket Channel

The WebSocket Channel Action opens a WebSocket connection through an HTTP upgrade and keeps it open for the rest of the iteration. Apart from `messages_mapping` and `push_messages` it is defined like a [request](request.md): `url`, `server`, `headers` and `extractors`.

The upgrade handshake is a GET with no body, per RFC 6455, so the channel has no `method`, `body` or `parts` settings.

A channel has no identifier of its own: it is designated by its complete path, from where it is declared down to its `name` (see [Referencing a channel](websocket_request.md#referencing-a-channel)). Two channels may therefore share a `name` as long as they are in different containers.

#### Available settings
| Name                                | Description                                                                                           | Accept variable | Required | Since |
|:----------------------------------- |:----------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name                                | The WebSocket Channel name                                                                            | -               | &#x2713; | 2026.3 |
| description                         | The WebSocket Channel description                                                                     | -               | -        | 2026.3 |
| url                                 | The channel URL, with the `http` or `https` scheme, or a path relative to `server`                    | &#x2713;        | &#x2713; | 2026.3 |
| server                              | The name of the server to use when `url` is a relative path                                           | -               | -        | 2026.3 |
| headers                             | The headers of the upgrade request                                                                    | &#x2713;        | -        | 2026.3 |
| extractors                          | Variable extractors applied to the channel response                                                   | -               | -        | 2026.3 |
| messages_mapping                    | How to extract the correlation id from an inbound frame. Mandatory as soon as a synchronous `websocket_request` uses the channel | - | - | 2026.3 |
| push_messages                       | The handlers of the frames the channel receives, see [push_messages](#push_messages)                   | -               | -        | 2026.3 |

NeoLoad stores a channel URL with the `http`/`https` scheme, derived from whether the server uses SSL, so `ws` and `wss` are not accepted: write `http://` for `ws://` and `https://` for `wss://`.

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

A push message handles the frames the channel receives, for instance messages the server sends on its own, or replies that cannot be paired with a request. For each frame:

1. a frame answering a waiting synchronous `websocket_request` goes to that request, and no push message runs;
2. otherwise, **every** push message whose `conditions` are true runs, in the order of the list;
3. only if none of them matched, every push message **without `conditions`** runs: **a push message without `conditions` is the fallback**.

A running push message applies its `extractors` and `assertions` to the frame, then runs its `steps`. While the conditions are evaluated, the variable `${NL-MessageContent}` holds the content of the frame.

| Name        | Description                                                                                                  | Accept variable | Required | Since |
|:----------- |:------------------------------------------------------------------------------------------------------------ |:---------------:|:--------:|:-----:|
| name        | The push message name. Defaults to `push_message`. Part of the path of a channel declared in `steps`        | -               | -        | 2026.3 |
| description | The push message description                                                                                 | -               | -        | 2026.3 |
| conditions  | The conditions on the frame, with the syntax of an [if](if.md). When present, at least one. Without `conditions` the push message is the fallback | &#x2713; | - | 2026.3 |
| match       | `any` or `all`: how the `conditions` are combined. Defaults to `any`. Only allowed with `conditions`        | -               | -        | 2026.3 |
| charset     | The charset of the frame content                                                                             | -               | -        | 2026.3 |
| extractors  | Variable extractors applied to the frame, before `steps` run                                                 | -               | -        | 2026.3 |
| assertions  | [Content assertions](assertion.md) on the frame                                                              | -               | -        | 2026.3 |
| steps       | The steps to run. May be empty: the push message then still takes the frames it matches, and is counted in the results | - | - | 2026.3 |

A forgotten or misspelt `conditions` key is not an error: the push message silently becomes a fallback.

Push messages run on the thread that receives the frames of the channel, so further frames wait until they are done. Put long actions inside a [fork](fork.md).

A request inside a push message, and a channel declared inside one, are referenced as described in [Referencing a channel](websocket_request.md#requests-and-channels-inside-a-push-message).

```yaml
push_messages:
- name: on_ping
  conditions:
  - "'${NL-MessageContent}' contains 'ping'"
  steps:
  - websocket_request:
      channel: actions>MyChannel
      body: pong
- name: on_order_update
  conditions:
  - "'${NL-MessageContent}' contains 'order'"
  - "'${NL-MessageContent}' contains 'status'"
  match: all
  extractors:
  - name: order_id
    jsonpath: $.order.id
  assertions:
  - contains: status
  steps:
  - request:
      url: https://host/orders/${order_id}
- name: on_heartbeat
  conditions:
  - "'${NL-MessageContent}' == 'hb'"
- name: anything_else
  steps:
  - websocket_request:
      channel: actions>MyChannel
      body: unknown message
```

`on_heartbeat` has no steps: it only keeps heartbeat frames away from the fallback. `anything_else` has no `conditions`: it is the fallback.

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
