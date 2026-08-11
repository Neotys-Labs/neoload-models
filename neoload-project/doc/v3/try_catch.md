# Try Catch

A logical action that executes a Try container and, if one of the caught exception types occurs while
running it, executes the Catch container.

Try Catch is supported in NeoLoad from version 2026.3 onwards.

#### Available settings

| Name              | Description                                                                                                  | Accept variable | Required | Since |
|:------------------|:-------------------------------------------------------------------------------------------------------------|:----------------:|:--------:|:-----:|
| name              | The name of the Try Catch. Defaults to `try_catch`.                                                          |         -         |    -     |   -   |
| description       | The description of the Try Catch                                                                             |         -         |    -     |   -   |
| caught_exceptions | Exception types to catch: `errors`, `assertions`, `all`. Defaults to `errors`.                               |         -         |    -     |   -   |
| try               | The Try [container](container.md)                                                                            |         -         | &#x2713; |   -   |
| catch             | The Catch [container](container.md)                                                                          |         -         |    -     |   -   |

#### Example
Run two requests in the Try container. If a request assertion fails, or an unhandled error occurs, run the
Catch container instead.
```yaml
  actions:
    steps:
    - try_catch:
        name: My Try Catch
        description: My description
        caught_exceptions: [errors, assertions]
        try:
          steps:
          - request:
              url: https://www.tricentis.com/
          - request:
              url: https://www.tricentis.com/neoload
        catch:
          steps:
          - delay: 1s
```