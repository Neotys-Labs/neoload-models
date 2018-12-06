# Delay and ThinkTime

## Delay
The Delay Action pauses the Virtual User for a specified duration.

#### Available settings are
| Name        | Description                                                     | Required/Optional |
| ----------- | --------------------------------------------------------------- | ----------------- |
| delay       | The delay duration                                          | Required          |

#### Duration value
The Delay duration is expressed in hours, minutes, seconds, milliseconds.
Some valid examples of Delay or Think time durations:

| Value        | Duration                                                     |
| ----------- | ----------------------------------------------------------- |
| 100       | 100 milliseconds                                          |
| 2m  | 2 minutes                                     |
| 2m 100ms  | 2 minutes 100 milliseconds                                     |
| 1h 10m 15s 250ms  | 1 hour 10 minutes 15 seconds 250 milliseconds                                     |

#### Example
Defining a 3 minutes and 100 milliseconds Delay.
```yaml
delay: 3m 100ms
```

## ThinkTime
The Think time Action simulates the time spent by a user on the preceding Page before accessing the current Page.

#### Available settings are
| Name        | Description                                                     | Required/Optional |
| ----------- | --------------------------------------------------------------- | ----------------- |
| think_time  | The Think time duration                                       | Required          |

#### Duration value
The ThinkTime duration format is the same as the Delay duration format.

#### Example
Defining a 100 milliseconds ThinkTime.
```yaml
think_time: 100
```

