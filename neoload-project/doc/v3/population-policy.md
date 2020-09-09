# Population
A population is a group of Virtual User types. Select an existing population to test and defines its load policy.
The simulated load from a population is determined by a duration and a number of Virtual Users.

#### Available settings

| Name          | Description                                                  | Accept variable   | Required | Since |
|:------------- |:------------------------------------------------------------ |:-----------------:|:--------:|:-----:|
| name          | The name of the population from NeoLoad project              | -                 | &#x2713; |       |
| [constant_load](#constant-load-policy) | A load that generates a fixed number of Virtual Users.                                                                                                                          | - | - |  |
| [rampup_load](#ramp-up-load-policy)    | A load that generates a number of Virtual Users that increases throughout the test. Useful for checking the server behavior under an increasing load.                           | - | - |  |
| [peaks_load](#peaks-load-policy)       | A load that generates a fixed number of Virtual Users with periodic phases of high load. Useful for checking whether the server recovers its normal behavior after a load peak. | - | - |  |
| [custom_load](#custom-load-policy)       | A load that generates Virtual Users defined by custom steps. Useful for checking the server behavior in really specific circumstances | - | - | 7.6 |

> A population must contain only one load policy among the constant, ramp-up, peaks and custom load.

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
- name: MyPopulation4
  custom_load:
    steps:
    - when: 1m40s
      users: 100
```

## Constant Load Policy

This load policy generates a load with a fixed number of Virtual Users. 

| Name        | Description                                                  | Accept variable | Required | Since |
|:----------- |:------------------------------------------------------------ |:---------------:|:--------:|:-----:|
| users       | The fixed number of Virtual Users                            | -               | &#x2713; |       |
| duration    | The duration of the load policy: unlimited, [time](#human-readable-time-specifications) or number of [iterations](#human-readable-iteration-specifications). | - | - |  |
| start_after | Define how the population is started: the population starts at the start of the test, after a preset [delay](#human-readable-time-specifications) or after the end of the selected population. | - | - |  |
| rampup      | Define how Virtual Users start: simultaneously or with a preset [delay](#human-readable-time-specifications). | - | - |  |
| stop_after  | Define how the population is stopped: the population immediately stop the executing of the current iteration, give a preset [delay](#human-readable-time-specifications) to finish the current iteration or allow the population to end the current iteration for each Virtual User. | - | - |  |

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

| Name             | Description                                                 | Accept variable   | Required | Since |
|:---------------- |:----------------------------------------------------------- |:-----------------:|:--------:|:-----:|
| min_users        | The initial number of Virtual Users                         | -                 | &#x2713; |       |
| max_users        | The maximum number of Virtual Users                         | -                 | -        |       |
| increment_users  | The number of Virtual Users to increment                    | -                 | &#x2713; |       |
| increment_every  | The duration to increment: [time](#human-readable-time-specifications) or number of [iterations](#human-readable-iteration-specifications). | - | &#x2713; |  |
| duration         | The duration of the load policy: unlimited, [time](#human-readable-time-specifications) or number of [iterations](#human-readable-iteration-specifications). | - | - |  |
| start_after      | Define how the population is started: the population starts at the start of the test, after a preset [delay](#human-readable-time-specifications) or after the end of the selected population. | - | - |   |
| increment_rampup | Define how Virtual Users start: simultaneously or with a preset [delay](#human-readable-time-specifications). This rule is used each time new Virtual Users are created, at each load increase for a ramp-up load policy. | - | - |  |
| stop_after       | Define how the population is stopped: the population immediately stop the executing of the current iteration, give a preset [delay](#human-readable-time-specifications) to finish the current iteration or allow the population to end the current iteration for each Virtual User. | - | - |  |

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

| Name        | Description                                                                               | Accept variable | Required | Since |
|:----------- |:----------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| minimum     | The [phase](#available-settings-for-the-minimum-and-maximum-phases-are) of low load       | -               | &#x2713; |       |
| maximum     | The [phase](#available-settings-for-the-minimum-and-maximum-phases-are) of high load      | -               | &#x2713; |       |
| start       | Select the phase to start                                                                 | -               | &#x2713; |       |
| duration    | The duration of the load policy: unlimited, [time](#human-readable-time-specifications) or number of [iterations](#human-readable-iteration-specifications). | - | - |  |
| start_after | Define how the population is started: the population starts at the start of the test, after a preset [delay](#human-readable-time-specifications) or after the end of the selected population. | - | - |  |
| step_rampup | Define how Virtual Users start: simultaneously or with a preset [delay](#human-readable-time-specifications). This rule is used each time new Virtual Users are created, at each load peak for a peak load policy. | - | - |  |
| stop_after  | Define how the population is stopped: the population immediately stop the executing of the current iteration, give a preset [delay](#human-readable-time-specifications) to finish the current iteration or allow the population to end the current iteration for each Virtual User. | - | - |  |

#### Available settings for the minimum and maximum phases are

| Name     | Description                                                  | Accept variable   | Required | Since |
|:-------- |:------------------------------------------------------------ |:-----------------:|:--------:|:-----:|
| users    | The fixed number of Virtual Users                            | -                 | &#x2713; |       |
| duration | The duration of the phase: [time](#human-readable-time-specifications) or number of [iterations](#human-readable-iteration-specifications). | - | &#x2713; |  |

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

## Custom Load Policy

This load policy generates a load defined by custom steps

| Name        | Description                                                  | Accept variable | Required | Since |
|:----------- |:------------------------------------------------------------ |:---------------:|:--------:|:-----:|
| start_after | Start policy for this population: at the start of the test, after a preset [delay](#human-readable-time-specifications) or after the end of another selected population | - | - |  |
| rampup      | Simultaneously or with a preset [delay](#human-readable-time-specifications).There are different behaviours depending on duration mode (unlimited, [time](#human-readable-time-specifications), [iterations](#human-readable-iteration-specifications)) - unlimited/[time](#human-readable-time-specifications): This rule is used each time Virtual Users count is updated - [iterations](#human-readable-iteration-specifications): This rule is used each time a new step starts| - | - |  |
| stop_after  | Stop policy for this population: immediate (the execution of current iteration is stopped), after a preset [delay](#human-readable-time-specifications) or the population is allowed to end current iteration for each Virtual User. | - | - |  |
| steps       | [Steps](#custom-policy-step) to define the Virtual Users count for a specific period | - | &#x2713; | |

#### Custom policy step

| Name     | Description                                                  | Accept variable   | Required | Since |
|:-------- |:------------------------------------------------------------ |:-----------------:|:--------:|:-----:|
| when  | The moment when Virtual Users count will reach the specified number: [time](#human-readable-time-specifications) or number of [iterations](#human-readable-iteration-specifications). | - | &#x2713; |  |
| users | The fixed number of Virtual Users at the specified moment | - | &#x2713; |  |

The specified moment must have the same duration type as the others steps.
All steps must be ordered chronologically.
The last step is defining the duration of the load policy

#### Example
The population starts 30 seconds after the start of the test.
Each ramp-up or ramp-down between 2 steps will be split with ramp-up/ramp-down of 10 seconds.
The population Virtual users has 30 seconds to finish the current iteration.

Steps: 
 - The population starts with 50 Virtual users
 - There is a ramp-up to 100 users, it takes 1 minute 40 seconds to do it
 - There is a ramp-up to 150 users, it takes 20 seconds to do it
 - There is a ramp-up to 200 users, it takes 0 seconds to do it
 - There is a constant at 200 users, from 2 minute to 3 minute
 - There is a ramp-down to 0 users, it takes 1 minute to do it
 - There is 0 user from 4 minute to 15 minute on the population 

```yaml
populations:
- name: MyPopulation14
  custom_load:
    start_after: 30s
    rampup: 10s
    stop_after: 30s
    steps:
    - when: 0
      users: 50
    - when: 1m40s
      users: 100
    - when: 2m
      users: 150
    - when: 2m
      users: 200
    - when: 3m
      users: 200
    - when: 4m
      users: 0
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