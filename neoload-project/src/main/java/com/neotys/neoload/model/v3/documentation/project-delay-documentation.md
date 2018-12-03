## Delay
The Delay action pauses the Virtual User for a specified duration.

The Think time action simulates the time spent by a user on the preceding Page before accessing the current Page.

**Available settings are:**

| Name        | Description                                                     | Required/Optional |
| ----------- | --------------------------------------------------------------- | ----------------- |
| delay       | The delay duration.                                             | Required          |
| think_time  | The think time duration.                                        | Required          |


The delay or think time duration is expressed in hours, minutes, seconds, milliseconds.
Some valid examples of delay or think time duration:

| Duration        | Value                                                     |
| ----------- | ----------------------------------------------------------- |
| 100ms       | 100 milliseconds                                          |
| 2m  | 2 minutes                                     |
| 2m  | 2 minutes                                     |
| 2m 100ms  | 2 minutes 100 milliseconds                                     |
| 1h 10m 15s 250ms  | 1 hour 10 minutes 15 seconds 250 milliseconds                                     |


**Example:**

Define a delay lasting 3 minutes and 100 milliseconds.

```yaml
name: MyProject
user_paths:
- name: user_path_1
  actions:
    do:
    - delay: 3m 100ms
```