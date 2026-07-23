# Wait Until

The Wait Until action pauses the current execution thread until certain conditions have been verified. You may also set a maximum timeout delay: once this delay is timed out, the thread is restarted and the following action is executed, even if the conditions have not been verified.

Wait Until is supported in NeoLoad from version 2026.3 onwards.

#### Available settings
| Name         | Description                                                                                        | Accept variable | Required | Since  |
|:------------ |:-------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:------:|
| name         | The name of the Wait Until. The default value is `wait_until`.                                     | -               | -        | 2026.3 |
| description  | The description of the Wait Until                                                                  | -               | -        | 2026.3 |
| conditions   | The list of conditions to evaluate                                                                 | -               | &#x2713; | 2026.3 |
| match        | The "match" value can be: <ul><li>`any`</li><li>`all`</li></ul>The default value is `any`.         | -               | -        | 2026.3 |
| timeout      | The maximum time to wait for the conditions to be verified. The default value is `1m` (60000 ms).  | &#x2713;        | -        | 2026.3 |

Use the following syntax to define conditions: `[(condition)(, condition)*]`

Use the following syntax to define condition: `"'operand1 (operator) (operand2)?"`

Operator value can be: <ul><li>`equals`</li><li>`==`</li><li>`not_equals`</li><li>`!=`</li><li>`contains`</li><li>`not_contains`</li><li>`starts_with`</li><li>`not_starts_with`</li><li>`ends_with`</li><li>`not_ends_with`</li><li>`match_regexp`</li><li>`not_match_regexp`</li><li>`greater`</li><li>`>`</li><li>`greater_equal`</li><li>`>=`</li><li>`less`</li><li>`<`</li><li>`less_equal`</li><li>`<=`</li><li>`exists`</li><li>`not_exists`</li></ul>

#### Timeout value
The timeout duration format is the same as the [Think time](think_time.md) duration format (expressed in hours, minutes, seconds, milliseconds). A NeoLoad variable can also be used.

We recommend using the `h m s ms` format (for example `15m 500ms`) rather than a plain integer representing a duration in milliseconds. The integer format (a duration in milliseconds) is nonetheless still accepted.

Some valid examples of timeout durations:

| Value             | Duration                                      |
| ----------------- | --------------------------------------------- |
| 15m 500ms         | 15 minutes 500 milliseconds                   |
| 30s               | 30 seconds                                    |
| 2m 100ms          | 2 minutes 100 milliseconds                    |
| 900500            | 900500 milliseconds (i.e. `15m 500ms`)        |
| ${timeout}        | The value of the `timeout` variable           |

#### Example
Wait until all of the following conditions evaluate to true, for a maximum of 30 seconds.
```yaml
  actions:
    steps:
    - wait_until:
        name: My Wait Until
        description: My description
        conditions:
        - "'${variable1}' equals 'value'"
        - "'${variable2}' equals '2'"
        - "'${variable3}' == 'string with space'"
        - "'${variable4}' != stringwithoutspace"
        - "'variable5' not_exists"
        - "'variable6' exists"
        - "'${variable7}' == ''"
        - "'${status_code}' == '200'"
        match: all
        timeout: 30s
```
