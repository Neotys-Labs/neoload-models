# Population
A population is a group of Virtual User types. Select an existing population to test and defines its load policy.
The simulated load from a population is determined by a duration and a number of Virtual Users.

#### Available settings are:

| Name          | Description                                                  | Accept variable   | Required/Optional |
| ------------- | ------------------------------------------------------------ | ----------------- | ----------------- |
| name          | The name of the population from NeoLoad project             | No                | Required          |
| [constant_load](#constant-load-policy) | A load that generates a fixed number of Virtual Users.       | No                | Optional          |
| [rampup_load](#ramp-up-load-policy)   | A load that generates a number of Virtual Users that increases throughout the test. Useful for checking the server behavior under an increasing load. | No                | optional          |
| [peaks_load](#peaks-load-policy)    | A load that generates a fixed number of Virtual Users with periodic phases of high load. Useful for checking whether the server recovers its normal behavior after a load peak. | No                | optional          |

> A population must contain only one load policy among the constant, ramp-up and peaks load.

#### Example
```yaml
populations:
- name: MyPopulation1
  constant_load:
    users: 500
- name: MyPopulation2
  rampup_load:
    min_users: 10
    increment_users: 10
    increment_every: 5s
- name: MyPopulation3
  peaks_load:
    minimum:
      users: 100
      duration: 5m
    maximum:
      users: 500
      duration: 3m
    start: minimum
```

## Constant Load Policy

This load policy generates a load with a fixed number of Virtual Users. 

| Name     | Description                                                  | Accept variable   | Required/Optional |
| -------- | ------------------------------------------------------------ | ----------------- | ----------------- |
| users    | The fixed number of Virtual Users                           | No                | Required          |
| duration | The duration of the load policy: unlimited, [time](#human-readable-time-specifications) or number of [iterations](#human-readable-iteration-specifications). | No                | Optional          |
| start_after | Define how the population is started: the population starts at the start of the test, after a preset [delay](#human-readable-time-specifications) or after the end of the selected population. | No                | Optional          |
| rampup      | Define how Virtual Users start: simultaneously or with a preset [delay](#human-readable-time-specifications). | No                | Optional          |
| stop_after  | Define how the population is stopped: the population immediately stop the executing of the current iteration, give a preset [delay](#human-readable-time-specifications) to finish the current iteration or allow the population to end the current iteration for each Virtual User. | No                | Optional          |

#### Example
1 constant load of 500 Virtual Users during 15 iterations. The population starts after the end of the selected population. The Virtual Users start with 30 seconds. The population wait the executing of the current iteration.

```yaml
populations:
- name: MyPopulation2
  constant_load:
    users: 500
    duration: 15 iterations
    start_after: MyPopulation1
    rampup: 30s
    stop_after: current_iteration
```

## Ramp-up Load Policy

This load policy generates a load with a number of Virtual Users that increases throughout the test. Useful for checking the server behavior under an increasing load.

| Name            | Description                                                  | Accept variable   | Required/Optional |
| --------------- | ------------------------------------------------------------ | ----------------- | ----------------- |
| min_users       | The initial number of Virtual Users                         | No                | Required          |
| max_users       | The maximum number of Virtual Users                         | No                | Optional          |
| increment_users | The number of Virtual Users to increment                    | No                | Required          |
| increment_every | The duration to increment: [time](#human-readable-time-specifications) or number of [iterations](#human-readable-iteration-specifications). | No                | Required          |
| duration        | The duration of the load policy: unlimited, [time](#human-readable-time-specifications) or number of [iterations](#human-readable-iteration-specifications). | No                | Optional          |
| start_after      | Define how the population is started: the population starts at the start of the test, after a preset [delay](#human-readable-time-specifications) or after the end of the selected population. | No                | Optional          |
| increment_rampup | Define how Virtual Users start: simultaneously or with a preset [delay](#human-readable-time-specifications). This rule is used each time new Virtual Users are created, at each load increase for a ramp-up load policy. | No                | Optional          |
| stop_after       | Define how the population is stopped: the population immediately stop the executing of the current iteration, give a preset [delay](#human-readable-time-specifications) to finish the current iteration or allow the population to end the current iteration for each Virtual User. | No                | Optional          |

#### Example
1 ramp-up load of 100 initial Virtual Users in incrementing 50 Virtual Users every iteration during 30 iterations and limited at 1500 maximum Virtual Users. The population starts after the end of the selected population. The Virtual Users start with 5 seconds. The population stops in waiting the executing of the current iteration.

```yaml
populations:
- name: MyPopulation2
  rampup_load:
    min_users: 100
    max_users: 1500
    increment_users: 50
    increment_every: 1 iteration
    duration: 30 iterations
    start_after: MyPopulation1
    increment_rampup: 5s
    stop_after: current_iteration
```

## Peaks Load Policy

This load policy generates a load with a fixed number of Virtual Users with periodic phases of low and high load. Useful for checking whether the server recovers its normal behavior after a load peak. 

| Name     | Description                                                  | Accept variable   | Required/Optional |
| -------- | ------------------------------------------------------------ | ----------------- | ----------------- |
| minimum  | The [phase](#available-settings-for-the-minimum-and-maximum-phases-are) of low load       | No                | Required          |
| maximum  | The [phase](#available-settings-for-the-minimum-and-maximum-phases-are) of high load      | No                | Required          |
| start    | Select the phase to start                                  | No                | Required          |
| duration | The duration of the load policy: unlimited, [time](#human-readable-time-specifications) or number of [iterations](#human-readable-iteration-specifications). | No                | Optional          |
| start_after | Define how the population is started: the population starts at the start of the test, after a preset [delay](#human-readable-time-specifications) or after the end of the selected population. | No                | Optional          |
| step_rampup | Define how Virtual Users start: simultaneously or with a preset [delay](#human-readable-time-specifications). This rule is used each time new Virtual Users are created, at each load peak for a peak load policy. | No                | Optional          |
| stop_after  | Define how the population is stopped: the population immediately stop the executing of the current iteration, give a preset [delay](#human-readable-time-specifications) to finish the current iteration or allow the population to end the current iteration for each Virtual User. | No                | Optional          |

#### Available settings for the minimum and maximum phases are

| Name     | Description                                                  | Accept variable   | Required/Optional |
| -------- | ------------------------------------------------------------ | ----------------- | ----------------- |
| users    | The fixed number of Virtual Users                           | No                | Required          |
| duration | The duration of the phase: [time](#human-readable-time-specifications) or number of [iterations](#human-readable-iteration-specifications). | No                | Required          |

#### Example
1 peaks load: a minimum load of 100 Virtual Users during 5 iterations and a maximum load of 500 Virtual Users during 3 iterations. The test starts with the minimum load. The duration of the test is 80 iterations. The population starts after the end of the selected population. The Virtual Users start with 15 seconds. The population stops in waiting the executing of the current iteration.

```yaml
populations:
- name: MyPopulation2
  peaks_load:
    minimum:
      users: 100
      duration: 5 iterations
    maximum:
      users: 500
      duration: 3 iterations
    start: minimum
    duration: 80 iterations
    start_after: MyPopulation1
    step_rampup: 15s
    stop_after: current_iteration
```

## Human-Readable Time Specifications
All time specifications, including delays and durations, are always expressed in unit of seconds. Use special strings convention to make it human-readable. 

`h` for hours, `m` for minutes, `s` for seconds

#### Examples
`90s`,`5m`,`2h30m30s`

## Human-Readable Iteration Specifications
All iteration specifications, including durations, are always expressed in unit of iterations. Use special strings convention to make it human-readable. 

`iteration` or `iterations`

#### Examples
`1 iteration`, `15 iterations`