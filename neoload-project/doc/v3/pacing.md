# Pacing
Pacing enforces a minimum duration for a container: if the container's steps finish before that duration has
elapsed, execution waits for the remainder before moving on.

Pacing can be set on the `init`, `actions`, `end` containers and `transaction`, `then`/`else` (from `if`), `try`/`catch` (from `try_catch`), `case`/`default` (from switch) transactions .

Pacing is either a single constant value, or a random range between a `min` and a `max`. It uses the same duration syntax as `think_time` and `delay`.

#### Available settings
`pacing` is either absent (no pacing), a constant duration (a scalar, or an object with `value`), or an object
with `min`/`max` bounds. `value` cannot be combined with `min` or `max`.

##### Constant pacing
| Name    | Description       | Accept variable | Required | Since  |
|:------- |:-------------------|:----------------:|:--------:|:------:|
| pacing  | A constant duration | &#x2713;          | -        | 2026.3 |
| pacing.value | The constant duration, when `pacing` is written as an object. Equivalent to the scalar form | &#x2713;          | -        | 2026.3 |

##### Random pacing (range)
| Name       | Description                                                    | Accept variable | Required | Since  |
|:---------- |:---------------------------------------------------------------|:----------------:|:--------:|:------:|
| pacing.min | The minimum duration of a random pacing range. Optional, `0` when absent  | &#x2713;          | -        | 2026.3 |
| pacing.max | The maximum duration of a random pacing range. Must be strictly greater than `pacing.min` | &#x2713;          | &#x2713; | 2026.3 |

#### Duration value
The pacing duration is expressed in hours, minutes, seconds and milliseconds, as a plain number, or as a
variable. A plain number without a unit is interpreted as milliseconds. Units can be separated by spaces (`1h 30m`). Some valid examples of pacing durations:

| Value       | Duration                    |
| ----------- | ---------------------------- |
| 100         | 100 milliseconds                    |
| 500ms       | 500 milliseconds              |
| 30s         | 30 seconds                    |
| 1h30m       | 1 hour 30 minutes             |
| 1h 30m      | 1 hour 30 minutes             |
| ${my_pacing}| The value of `my_pacing`      |

#### Example
Defining a constant 30 seconds pacing on a transaction.
```yaml
transaction:
  name: MyTransaction
  pacing: 30s
  steps:
  - request:
      url: http://www.company.com/overview
```

Defining a random pacing between 1 and 3 seconds on the "actions" container.
```yaml
actions:
  pacing:
    min: 1s
    max: 3s
  steps:
  - request:
      url: http://www.company.com/overview
```
