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
| on_error                            | What the Virtual User does when an error occurs, see [on_error and on_assertion_failure](#on_error-and-on_assertion_failure). When omitted, the NeoLoad default applies. | -               | -                  | 2026.3 |
| on_assertion_failure                | What the Virtual User does when an assertion fails, see [on_error and on_assertion_failure](#on_error-and-on_assertion_failure). When omitted, the NeoLoad default applies. | -               | -                  | 2026.3 |
| think_time                          | Overrides or scales the think times of the User Path, optionally with a random delay, see [think_time](#think_time). When omitted, the think times defined on the web pages and the `think_time` steps apply, with no random delay. | &#x2713;        | -                  | 2026.3 |
| init                                | The init [container](container.md)                                                                                                                         | -               | -                  |       |
| actions                             | The actions [container](container.md)                                                                                                                      | -               | &#x2713;           |       |
| end                                 | The end [container](container.md)                                                                                                                          | -               | -                  |       |
| [assertions](assertion.md)          | The list of assertions to validate the response content of all requests matching criteria within the User Path. By default, the validation is applied only on response with content-type text/html or text/xhtml. List of content-types used for response matching can be customized in the Project Settings / Runtime Parameters from the GUI project (cannot be customized with As-code only project). | -               | -                  | 7.6   |


#### on_error and on_assertion_failure
`on_error` sets what the Virtual User does when an error occurs, and `on_assertion_failure` what it does when an assertion fails. They match the "When an error occurs" and "When an assertion fails" settings of the User Path "Runtime parameters" panel of the NeoLoad designer.

| Value                   | Designer setting                  |
|:----------------------- |:--------------------------------- |
| `do_nothing`            | Do nothing                        |
| `go_to_next_iteration`  | Go to the next iteration          |
| `stop_and_start_new_vu` | Stop and start a new Virtual User |

When omitted, the NeoLoad default applies: `do_nothing`, unless changed with the `vupath.errorPolicy` and `vupath.failedAssertionPolicy` system properties.

#### think_time
The User Path `think_time` matches the "Waiting time" settings of the User Path "Runtime parameters" panel of the NeoLoad designer. It applies to all the think times of the User Path: the `think_time` of the [web pages](web_page.md) and the [think_time](think_time.md) steps. The [delay](delay.md) steps are not affected.

It must not be confused with the [think_time](think_time.md) step, which pauses the Virtual User at a given point of the User Path, nor with the `think_time` of a [web page](web_page.md), which applies before playing that web page.

##### Available settings
At least one of `override`, `factor` and `random` must be set, and `override` cannot be combined with `factor`.

| Name                | Description                                                                                                  | Accept variable | Required | Since  |
|:------------------- |:------------------------------------------------------------------------------------------------------------ |:---------------:|:--------:|:------:|
| think_time.override | Replaces every think time with this duration. Designer setting: "Override think time"                       | &#x2713;        | -        | 2026.3 |
| think_time.factor   | Scales every think time by this percentage. Designer setting: "Apply this factor for each page and delay"   | &#x2713;        | -        | 2026.3 |
| think_time.random   | Adds a random delay of +/- this percentage to the think times. Designer setting: "Add a random delay +/-"   | &#x2713;        | -        | 2026.3 |

Without `override` nor `factor`, the think times defined on the web pages and the `think_time` steps apply (designer setting: "Use think time defined on pages and delays"). `random` is combined with whichever think times apply.

##### Values
* `override` is a duration in the [think_time](think_time.md#duration-value) step format: a plain number of milliseconds (`100`), or hours, minutes, seconds and milliseconds (`5s`, `1m 30s`, `1h 10m 15s 250ms`).
* `factor` and `random` are non-negative integer percentages, the `%` sign being optional (`150%` or `150`).
* All of them accept a variable instead (`${my_think_time}`).

Negative values, malformed durations and malformed percentages are rejected.

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
