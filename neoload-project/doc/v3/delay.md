# Delay

The Delay Action pauses the Virtual User for a specified duration. This duration always applies and is not impacted by thinktime overriding.

## Two ways to define a delay
A `delay` can be defined in two ways:
* a **fixed duration** — the delay always lasts the same duration;
* a **random duration between two values** — the delay is drawn between a `min` and a `max`.

Both forms accept an optional `name` and `description`.

## Fixed duration delay
A fixed delay is expressed as a single duration. It has a **simplified** form (a scalar) and a **complete** form (an object carrying `name` / `description`).

#### Duration value
The delay duration is expressed in hours, minutes, seconds, milliseconds.

We recommend using the duration format consisting of `hours (h), minutes (m), seconds (s) and milliseconds (ms)` (for example `3m 100ms`) rather than a plain integer representing a duration in milliseconds. The integer format (a duration in milliseconds) is nonetheless still accepted. A NeoLoad variable can also be used.

Some valid examples of delay durations:

| Value             | Duration                                      |
| ----------------- | --------------------------------------------- |
| 3m                | 3 minutes                                     |
| 3m 100ms          | 3 minutes 100 milliseconds                    |
| 1h 10m 15s 250ms  | 1 hour 10 minutes 15 seconds 250 milliseconds |
| 100               | 100 milliseconds                              |
| ${my_delay}       | The value of the `my_delay` variable          |

### Simplified form
| Name        | Description        | Accept variable | Required | Since |
|:----------- |:------------------ |:---------------:|:--------:|:-----:|
| delay       | The delay duration | &#x2713;        | &#x2713; |       |

Defining a 3 minutes and 100 milliseconds delay.
```yaml
delay: 3m 100ms
```

Using variables:
```yaml
delay: ${myDelayVariable}
```

### Complete form
| Name        | Description                                              | Accept variable | Required | Since  |
|:----------- |:------------------------------------------------------- |:---------------:|:--------:|:------:|
| name        | The name of the delay. The default value is `delay`.    | -        | -        | 2026.3 |
| description | The description of the delay                           | -        | -        | 2026.3 |
| value       | The delay duration                                     | &#x2713; | &#x2713; | 2026.3 |

```yaml
delay:
  name: MyDelay
  description: Wait before checkout
  value: 3m 100ms
```

Using variables:
```yaml
delay:
  name: MyDelay
  description: Wait before checkout
  value: ${myDelayVariable}
```

## Random duration delay
A random delay is drawn between a `min` and a `max`.

#### Available settings
| Name        | Description                                          | Accept variable | Required | Since  |
|:----------- |:--------------------------------------------------- |:---------------:|:--------:|:------:|
| name        | The name of the delay. The default value is `delay`. | -        | -        | 2026.3 |
| description | The description of the delay                        | -        | -        | 2026.3 |
| min         | The minimum delay duration. The default value is `0`. | &#x2713; | -        | 2026.3 |
| max         | The maximum delay duration                          | &#x2713; | &#x2713; | 2026.3 |

`min` and `max` use the same duration format as the fixed delay (`hours (h), minutes (m), seconds (s) and milliseconds (ms)`, a plain integer in milliseconds, or a NeoLoad variable).

#### Example — simplified syntax
Drawing a delay between 1 second and 3 seconds.
```yaml
delay:
  min: 1s
  max: 3s
```

Using variables:
```yaml
delay:
  min: ${min_delay}
  max: ${max_delay}
```

#### Example — complete syntax
```yaml
delay:
  name: MyRandomDelay
  description: Random pause
  min: 1s
  max: 3s
```
