# User Paths
A User Path simulates the browsing activity of a real visitor.

The User Path is a succession of web pages that may contain logical Actions such as Containers, Loops or Delays to create a more complex behavior.

:warning: If the project already have a User Path with the same name from NeoLoad, the whole User Path will be __replaced__ by the as-code one.

#### Available settings

| Name                                | Description                                                                                                                                                | Accept variable | Required           | Since |
|:----------------------------------- |:---------------------------------------------------------------------------------------------------------------------------------------------------------- |:---------------:|:------------------:|:-----:|
| name                                | The name of the User Path                                                                                                                                  | -               | &#x2713;           |       |
| description                         | The description of the User Path                                                                                                                           | -               | -                  |       |
| user_session                        | The "user_session" value can be: <ul><li>`reset_on`</li><li>`reset_off`</li><li>`reset_auto`</li></ul></br>The default value is `reset_auto`.              | -               | -                  |       |
| on_error                            | What the Virtual User does when an error occurs: `go_to_next_iteration` or `stop_and_start_new_vu`, see [on_error and on_assertion_failure](#on_error-and-on_assertion_failure). By default, it does nothing. | -               | -                  | 2026.3 |
| on_assertion_failure                | What the Virtual User does when an assertion fails: `go_to_next_iteration` or `stop_and_start_new_vu`, see [on_error and on_assertion_failure](#on_error-and-on_assertion_failure). By default, it does nothing. | -               | -                  | 2026.3 |
| think_time                          | Replaces or scales all the think times of the User Path, see [think_time](#think_time). By default, the think times are kept as defined. | &#x2713;        | -                  | 2026.3 |
| init                                | The init [container](container.md)                                                                                                                         | -               | -                  |       |
| actions                             | The actions [container](container.md)                                                                                                                      | -               | &#x2713;           |       |
| end                                 | The end [container](container.md)                                                                                                                          | -               | -                  |       |
| [assertions](assertion.md)          | The list of assertions to validate the response content of all requests matching criteria within the User Path. By default, the validation is applied only on response with content-type text/html or text/xhtml. List of content-types used for response matching can be customized in the Project Settings / Runtime Parameters from the GUI project (cannot be customized with As-code only project). | -               | -                  | 7.6   |


#### on_error and on_assertion_failure
What the Virtual User does when an error occurs (`on_error`) or when an assertion fails (`on_assertion_failure`), as in the User Path "Runtime parameters" panel of the NeoLoad designer:

| Value                   | Designer setting                  |
|:----------------------- |:--------------------------------- |
| `go_to_next_iteration`  | Go to the next iteration          |
| `stop_and_start_new_vu` | Stop and start a new Virtual User |

By default, the Virtual User does nothing (designer setting "Do nothing"): to get this behavior, omit the setting.

#### think_time
Changes all the think times of the User Path at once: the `think_time` of its [web pages](web_page.md) and its [think_time](think_time.md) steps. The [delay](delay.md) steps are not changed. It matches the "Waiting time" settings of the User Path "Runtime parameters" panel of the NeoLoad designer.

| Name                | Description                                                                                        | Designer setting                          | Accept variable | Required | Since  |
|:------------------- |:-------------------------------------------------------------------------------------------------- |:----------------------------------------- |:---------------:|:--------:|:------:|
| think_time.override | Replaces each think time with this duration, in the [pacing](pacing.md#duration-value) format (`100` for 100 milliseconds, `5s`, `1m30s`) | Override think time | &#x2713; | - | 2026.3 |
| think_time.factor   | Multiplies each think time by this percentage: an integer, `0` or more (`150%`, `%` optional)      | Apply this factor for each page and delay | &#x2713;        | -        | 2026.3 |
| think_time.random   | Adds a random delay of +/- this percentage to each think time: an integer, `1` or more (`10%`, `%` optional). By default, no random delay is added | Add a random delay +/- | &#x2713; | - | 2026.3 |

Use `override` or `factor`, not both. `random` can be used alone or with either of them. Without `override` nor `factor`, the think times are kept as defined (designer setting "Use think time defined on pages and delays").

#### Example
Defining a User Path:
```yaml
user_paths:
- name: MyUserPath
  actions:
    steps:
    - request:
        url: http://www.company.com/select?name=book
    - delay: 1s
```

Defining a User Path that goes to the next iteration when an error occurs, stops and starts a new Virtual User when an assertion fails, and replaces its think times with 5 seconds +/- 10%:
```yaml
user_paths:
- name: MyUserPath
  on_error: go_to_next_iteration
  on_assertion_failure: stop_and_start_new_vu
  think_time:
    override: 5s
    random: 10%
  actions:
    steps:
    - request:
        url: http://www.company.com/select?name=book
    - think_time: 2s
```

Scaling the think times of the User Path by 150% instead of replacing them:
```yaml
user_paths:
- name: MyUserPath
  think_time:
    factor: 150%
  actions:
    steps:
    - request:
        url: http://www.company.com/select?name=book
    - think_time: 2s
```
