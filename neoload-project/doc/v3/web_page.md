# Web Page

The Web Page Action is used to group together the requests and resources that make up a single page load, along with options controlling how its resources are played back.

#### Available settings
| Name                                | Description                                                                                           | Accept variable | Required | Since |
|:----------------------------------- |:----------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name                                | The Web Page name. Defaults to `web_page`                                                            | -               | -        | 2026.3 |
| description                         | The Web Page description                                                                              | -               | -        | 2026.3 |
| sla_profile                         | The name of the SLA profile to apply to the Web Page (will not be applied to children)                | -               | -        | 2026.3 |
| think_time                          | The think time to apply before playing the Web Page. Either a constant value (`value`), or a random value between `min` (defaults to `0`) and `max` | &#x2713; | -        | 2026.3 |
| playback                            | How the Web Page's steps are played: `parallel` (default) or `sequential`                             | -               | -        | 2026.3 |
| execute_resources                   | How the Web Page's resources are executed: `static` (default), `dynamic` or `dynamic_forced_encoding` | -               | -        | 2026.3 |
| steps                               | Steps of the Web Page. Only [request](request.md) and nested `web_page` steps are supported            | -               | &#x2713; | 2026.3 |

#### think_time

Either a constant value:
```yaml
think_time:
  value: 2s
```

Or a random value between `min` and `max`:
```yaml
think_time:
  min: 1s
  max: 5s
```

`min` defaults to `0` when omitted. Values can be a plain number of seconds (`2`, `2.5`), a time literal with optional `h`/`m`/`s`/`ms` suffixed components (`500ms`, `2s`, `1h30m`), or a variable reference (`${max_think_time}`).

#### Example

Defining MyWebPage that contains one request and a nested web page.

```yaml
- web_page:
    name: MyWebPage
    description: My First web page
    sla_profile: MySlaProfile
    think_time:
      value: 2s
    playback: sequential
    execute_resources: dynamic
    steps:
    - request:
        url: http://host:80/
    - web_page:
        steps:
        - request:
            url: http://host:80/resource
```
