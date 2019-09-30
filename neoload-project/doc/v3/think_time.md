# Think time
Think time is the time the user normally takes to make a decision to do the next task.

For example, it is the simulation of the time taken by a real user to read one page before clicking to the next one.
Thinktime values may be overriden for the entire VU Path. If the delay must not be overriden when the thinktime is overriden, use a 'delay' instead.

#### Available settings
| Name        | Description                                 | Accept variable  | Required | Since |
|:----------- |:------------------------------------------- |:----------------:|:--------:|:-----:|
| think_time  | The Think time duration                     | &#x2713;         | &#x2713; |       |

#### Duration value
The Think time duration is expressed in hours, minutes, seconds, milliseconds.
Some valid examples of Think time durations:

| Value             | Duration                                      |
| ----------------- | --------------------------------------------- |
| 100               | 100 milliseconds                              |
| 2m                | 2 minutes                                     |
| 2m 100ms          | 2 minutes 100 milliseconds                    |
| 1h 10m 15s 250ms  | 1 hour 10 minutes 15 seconds 250 milliseconds |

#### Example
Defining a 100 milliseconds Think time.
```yaml
think_time: 100
```
