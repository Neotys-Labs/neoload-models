# WebSocket Request

The WebSocket Request Action sends a frame on a [websocket_channel](websocket_channel.md), or closes it.

A request is asynchronous unless `synchronous` is set: it sends its frame and moves on. A synchronous request waits for the inbound frame whose correlation id, extracted by the channel's `messages_mapping`, equals its `mapping_id`. Only a synchronous request has a response, so `mapping_id`, `extractors` and `assertions` only apply to it, as in NeoLoad, which only shows them for a synchronous request.

#### Available settings
| Name                                | Description                                                                                           | Accept variable | Required | Since |
|:----------------------------------- |:----------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name                                | The WebSocket Request name. Defaults to `websocket_request`                                           | -               | -        | 2026.3 |
| description                         | The WebSocket Request description. Stored in the project, but not editable in the NeoLoad request panel | -             | -        | 2026.3 |
| channel                             | The complete path of the channel to use, see [Referencing a channel](#referencing-a-channel)          | -               | -        | 2026.3 |
| message_type                        | `text`, `binary` or `close` ("Text Data" and "Close" in NeoLoad). Defaults to `text`. `binary` is not offered by NeoLoad's request panel: it is what NeoLoad records for a non-text frame | - | - | 2026.3 |
| synchronous                         | Whether the request waits for its response. Defaults to `false`. A `close` cannot be synchronous       | -               | -        | 2026.3 |
| mapping_id                          | The correlation id of the awaited response. Required when `synchronous` is `true`, and only allowed then | &#x2713;     | -        | 2026.3 |
| status_code                         | The close status code, only for a `close`. Defaults to `1000` (normal closure)                         | &#x2713;        | -        | 2026.3 |
| body                                | The frame content, or the reason of a `close`. Mutually exclusive with `bodybinary`                    | &#x2713;        | -        | 2026.3 |
| bodybinary                          | The frame content as Base64. Mutually exclusive with `body`                                            | -               | -        | 2026.3 |
| extractors                          | Variable extractors applied to the response, only for a synchronous request                           | -               | -        | 2026.3 |
| assertions                          | [Content assertions](assertion.md) on the response, only for a synchronous request                   | -               | -        | 2026.3 |

#### Referencing a channel

`channel` is the complete path of the channel, from where it is declared down to its `name`, joined with `>`:

- it starts with the part of the User Path the channel is in: `init`, `actions` or `end`;
- then comes the `name` of each step on the way, for instance a transaction, a loop or a fork;
- the branches of an `if` are `then` and `else`, those of a `try_catch` are `try` and `catch`, and the default branch of a `switch` is `default`. A `switch` case is designated by its `name`;
- the last part is the channel's `name`.

A request can only use a channel of its own User Path: a virtual user only holds the connections it opened itself. Two channels may share a `name` as long as their paths differ.

Write the path without spaces around `>`: `actions>Login>chat_socket`, not `actions > Login > chat_socket`. The path is only split on `>`, so a space would become part of the step name being looked up. Spaces inside a name are kept as they are: `actions>My Login>chat_socket`. A path must not start with `>`.

```yaml
init:
  steps:
  - websocket_channel:
      name: chat_socket                              # init>chat_socket
      url: wss://host:443/chat
actions:
  steps:
  - if:
      name: has_notifications
      conditions:
      - "'${notifications}' == 'on'"
      then:
        steps:
        - websocket_channel:
            name: notify_socket                      # actions>has_notifications>then>notify_socket
            url: wss://host:443/notify
        - websocket_request:
            channel: actions>has_notifications>then>notify_socket
            body: subscribe
  - transaction:
      name: Chat
      steps:
      - websocket_request:
          channel: init>chat_socket
          body: hello
```

A reference is rejected when it does not start with `init`, `actions` or `end`, when no channel has that path (the message then names the paths of the channels with the same `name`), or when two sibling steps on the path share a name.

#### Example

Sending a message and waiting for its reply, then closing the channel.

```yaml
- websocket_channel:
    name: MyChannel
    url: wss://host:443/socket
    messages_mapping:
      jsonpath: $.correlationId
- websocket_request:
    name: SendHello
    channel: actions>MyChannel
    synchronous: true
    mapping_id: ${correlation_id}
    body: '{"op":"hello","correlationId":"${correlation_id}"}'
    assertions:
    - contains: welcome
- websocket_request:
    name: CloseChannel
    channel: actions>MyChannel
    message_type: close
```
