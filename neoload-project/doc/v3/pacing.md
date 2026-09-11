# Pacing
Pacing enforces a minimum duration for a container: if the container's steps finish before that duration has
elapsed, execution waits for the remainder before moving on.

Pacing can be set on the `init`, `actions` and `end` containers, on a `transaction`, on `if`'s `then`/`else`
branches, on `try_catch`'s `try`/`catch` branches, and on a `switch`'s `case`/`default` branches.

Pacing is either a single constant value, or a random range between a `min` and a `max`.

#### Available settings
| Name    | Description                                                    | Accept variable | Required | Since  |
|:------- |:---------------------------------------------------------------|:----------------:|:--------:|:------:|
| pacing  | A constant duration, or an object with `min`/`max` bounds       | &#x2713;          | -        | 2026.3 |
| min     | The minimum duration of a random pacing range. Defaults to `0`  | &#x2713;          | -        | 2026.3 |
| max     | The maximum duration of a random pacing range                  | &#x2713;          | &#x2713; | 2026.3 |

#### Duration value
The pacing duration is expressed in hours, minutes, seconds, milliseconds, as a plain (optionally decimal)
number, or as a variable. Some valid examples of pacing durations:

| Value       | Duration                    |
| ----------- | ---------------------------- |
| 100         | 100 milliseconds              |
| 1.5s        | 1.5 seconds                   |
| 30s         | 30 seconds                    |
| 1h30m       | 1 hour 30 minutes             |
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
