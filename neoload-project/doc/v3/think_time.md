# Think time
Think time is the time the user normally takes to make a decision to do the next task.

For example, it is the simulation of the time taken by a real user to read one page before clicking to the next one.
Thinktime values may be overriden for the entire VU Path. If the delay must not be overriden when the thinktime is overriden, use a 'delay' instead.

## Two ways to define a think time
A `think_time` can be defined in two ways:
* a **fixed duration** — the think time always lasts the same duration;
* a **random duration between two values** — the think time is drawn between a `min` and a `max`.

Both forms accept an optional `name` and `description`.

## Fixed duration think time
A fixed think time is expressed as a single duration. It has a **simplified** form (a scalar) and a **complete** form (an object carrying `name` / `description`).

#### Duration value
The think time duration is expressed in hours, minutes, seconds, milliseconds.

We recommend using the duration format consisting of `hours (h), minutes (m), seconds (s) and milliseconds (ms)` (for example `2m 100ms`) rather than a plain integer representing a duration in milliseconds. The integer format (a duration in milliseconds) is nonetheless still accepted. A NeoLoad variable can also be used.

Some valid examples of think time durations:

| Value             | Duration                                      |
| ----------------- | --------------------------------------------- |
| 2m                | 2 minutes                                     |
| 2m 100ms          | 2 minutes 100 milliseconds                    |
| 1h 10m 15s 250ms  | 1 hour 10 minutes 15 seconds 250 milliseconds |
| 100               | 100 milliseconds                              |
| ${my_think_time}  | The value of the `my_think_time` variable     |

### Simplified form
| Name        | Description             | Accept variable | Required | Since |
|:----------- |:----------------------- |:---------------:|:--------:|:-----:|
| think_time  | The think time duration | &#x2713;        | &#x2713; |       |

Defining a 2 minutes and 100 milliseconds think time.
```yaml
think_time: 2m 100ms
```

Using variables:
```yaml
think_time: ${myThinkTimeVariable}
```

### Complete form
| Name        | Description                                                    | Accept variable | Required | Since  |
|:----------- |:------------------------------------------------------------- |:---------------:|:--------:|:------:|
| name        | The name of the think time. The default value is `think_time`. | -        | -        | 2026.3 |
| description | The description of the think time                             | -        | -        | 2026.3 |
| value       | The think time duration                                       | &#x2713; | &#x2713; | 2026.3 |

```yaml
think_time:
  name: MyThinkTime
  description: Read the page
  value: 2m 100ms
```

Using variables:
```yaml
think_time:
  name: MyThinkTime
  description: Read the page
  value: ${myThinkTimeVariable}
```

## Random duration think time
A random think time is drawn between a `min` and a `max`.

#### Available settings
| Name        | Description                                       | Accept variable | Required | Since  |
|:----------- |:------------------------------------------------- |:---------------:|:--------:|:------:|
| name        | The name of the think time. The default value is `think_time`. | -        | -        | 2026.3 |
| description | The description of the think time                             | -        | -        | 2026.3 |
| min         | The minimum think time duration. The default value is `0`.    | &#x2713; | -        | 2026.3 |
| max         | The maximum think time duration                              | &#x2713; | &#x2713; | 2026.3 |

`min` and `max` use the same duration format as the fixed think time (`hours (h), minutes (m), seconds (s) and milliseconds (ms)`, a plain integer in milliseconds, or a NeoLoad variable).

#### Example — simplified syntax
Drawing a think time between 1 second and 3 seconds.
```yaml
think_time:
  min: 1s
  max: 3s
```

Using variables:
```yaml
think_time:
  min: ${min_think_time}
  max: ${max_think_time}
```

#### Example — complete syntax
```yaml
think_time:
  name: MyRandomThinkTime
  description: Random reflection
  min: 1s
  max: 3s
```
