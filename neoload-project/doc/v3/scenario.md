# NeoLoad Project definition
Definition has several top-level keys.

| Name      | Description        |
| --------- | ------------------ |
| Scenarios | List of scenarios. |

You can find bellow an example on how to define your scenario when launching a test in command line using the `-project` parameter.

Please read the full [command line documentation](https://www.neotys.com/documents/doc/neoload/latest/en/html/#643.htm).

**Example:**

```yaml
scenarios:
- name: MyScenario
  description: My scenario with 1 SLA profile and 1 population
  sla_profile: MySlaProfile
  populations:
  - name: MyPopulation
    rampup_load:
      min_users: 10
      max_users: 1500
      increment_users: 10
      increment_every: 5s
      duration: 15m
```


# Scenarios Settings
You can find bellow 2 examples on how to define your scenario containing a *constant* load, a *ramp-up* load and a *peaks* load.

**Example 1:**

Define a scenario containing a *constant* load, a *ramp-up* load and a *peaks* load in using 3 different populations. The duration of the test is unlimited (stop manually). 

```yaml
scenarios:
- name: MyScenario
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

**Example 2:**

Define a scenario containing a *constant* load, a *ramp-up* load and a *peaks* load* in using 3 different populations including some advanced settings. 

```yaml
scenarios:
- name: MyScenario
  description: My scenario with 1 SLA profile and 3 populations
  sla_profile: MySlaProfile
  populations:
  - name: MyPopulation1
    constant_load:
      users: 500
      duration: 15m
      start_after: 30s
      rampup: 1m
      stop_after: 30s
  - name: MyPopulation2
    rampup_load:
      min_users: 10
      max_users: 500
      increment_users: 10
      increment_every: 1 iteration
      duration: 15 iterations
      start_after: MyPopulation1
      increment_rampup: 1m30s
      stop_after: current_iteration
  - name: MyPopulation3
    peaks_load:
      minimum:
        users: 100
        duration: 5 iterations
      maximum:
        users: 500
        duration: 2 iteration
      start: maximum
      duration: 70 iterations
      start_after: 1m30s
      step_rampup: 15s
      stop_after: 1m30s
```

## Scenario
A scenario defines a test configuration. Select an existing SLA profile to verify and a list of existing populations to test from NeoLoad project. Define a load policy for each population.

**Available settings are:**

| Name        | Description                                                  | Required/Optional |
| ----------- | ------------------------------------------------------------ | ----------------- |
| name        | The name of the scenario.                                    | Required          |
| description | The description of the scenario.                             | Optional          |
| sla_profile | The SLA (Service Level Agreement) profile to verify in this scenario. | Optional          |
| populations | The list of existing populations from NeoLoad project.       | Required          |

**Example:**

Define three scenarios: a test with a *constant* load, a test with a *ramp-up* load or a test with *peaks* load with the same population.

```yaml
scenarios:
- name: MyScenario1
  populations:
  - name: MyPopulation
    constant_load:
    users: 500
- name: MyScenario2
  populations:
  - name: MyPopulation
    rampup_load:
    min_users: 10
    increment_users: 10
    increment_every: 5s
- name: MyScenario3
  populations:
  - name: MyPopulation
    peaks_load:
      minimum:
        users: 100
        duration: 2m
      maximum:
        users: 500
        duration: 2m
      start: minimum
```

## Population
A population is a group of Virtual User types. Select an existing population to test and defines its load policy.
The simulated load from a population is determined by a duration and a number of Virtual Users.

**Available settings are:**

| Name          | Description                                                  | Required/Optional |
| ------------- | ------------------------------------------------------------ | ----------------- |
| name          | The name of the population from NeoLoad project.             | Required          |
| constant_load | A load that generates a fixed number of Virtual Users.       | Optional          |
| rampup_load   | A load that generates a number of Virtual Users that increases throughout the test. Useful for checking the server behavior under an increasing load. | optional          |
| peaks_load    | A load that generates a fixed number of Virtual Users with periodic phases of high load. Useful for checking whether the server recovers its normal behavior after a load peak. | optional          |

> A population must contain only one load policy among the constant, ramp-up and peaks load.

#### Constant Load Policy

This load policy generates a load with a fixed number of Virtual Users. 

**Available settings are:**

| Name     | Description                                                  | Required/Optional |
| -------- | ------------------------------------------------------------ | ----------------- |
| users    | The fixed number of Virtual Users.                           | Required          |
| duration | The duration of the load policy: unlimited, [time](#human-readable-time-specifications) or number of [iterations](#human-readable-iteration-specifications). | Optional          |

**Advanced settings are:**

| Name        | Description                                                  | Required/Optional |
| ----------- | ------------------------------------------------------------ | ----------------- |
| start_after | Define how the population is started: the population starts at the start of the test, after a preset [delay](#human-readable-time-specifications) or after the end of the selected population. | Optional          |
| rampup      | Define how Virtual Users start: simultaneously or with a preset [delay](#human-readable-time-specifications). | Optional          |
| stop_after  | Define how the population is stopped: the population immediately stop the executing of the current iteration, give a preset [delay](#human-readable-time-specifications) to finish the current iteration or allow the population to end the current iteration for each Virtual User. | Optional          |

**Example 1:**

A test with a constant load of 500 Virtual Users. The duration of the test is unlimited (stop manually). The population starts at the start of the test. The Virtual Users simultaneously start. The population immediately stop.

```yaml
scenarios:
- name: MyScenario
  populations:
  - name: MyPopulation
    constant_load:
      users: 500
```

**Example 2:**

A test with a constant load of 500 Virtual Users during 15 minutes. The population starts after 45 seconds. The Virtual Users simultaneously start. The population stops after 90 seconds.

```yaml
scenarios:
- name: MyScenario
  populations:
  - name: MyPopulation
    constant_load:
      users: 500
      duration: 15m
      start_after: 45s
      stop_after: 1m30s	  
```

**Example 3:**

A test including 2 constant loads:

1 constant load of 100 Virtual Users during 5 iterations. The population starts at the start of the test. The Virtual Users simultaneously start. The population immediately stop.

1 constant load of 500 Virtual Users during 15 iterations. The population starts after the end of the selected population. The Virtual Users start with 30 seconds. The population wait the executing of the current iteration.

```yaml
scenarios:
- name: MyScenario
  populations:
  - name: MyPopulation1
    constant_load:
      users: 100
      duration: 5 iterations
  - name: MyPopulation2
    constant_load:
      users: 500
      duration: 15 iterations
      start_after: MyPopulation1
      rampup: 30s
      stop_after: current_iteration
```

#### Ramp-up Load Policy

This load policy generates a load with a number of Virtual Users that increases throughout the test. Useful for checking the server behavior under an increasing load.

**Available settings are:**

| Name            | Description                                                  | Required/Optional |
| --------------- | ------------------------------------------------------------ | ----------------- |
| min_users       | The initial number of Virtual Users.                         | Required          |
| max_users       | The maximum number of Virtual Users.                         | Optional          |
| increment_users | The number of Virtual Users to increment.                    | Required          |
| increment_every | The duration to increment: [time](#human-readable-time-specifications) or number of [iterations](#human-readable-iteration-specifications). | Required          |
| duration        | The duration of the load policy: unlimited, [time](#human-readable-time-specifications) or number of [iterations](#human-readable-iteration-specifications). | Optional          |

**Advanced settings are:**

| Name             | Description                                                  | Required/Optional |
| ---------------- | ------------------------------------------------------------ | ----------------- |
| start_after      | Define how the population is started: the population starts at the start of the test, after a preset [delay](#human-readable-time-specifications) or after the end of the selected population. | Optional          |
| increment_rampup | Define how Virtual Users start: simultaneously or with a preset [delay](#human-readable-time-specifications). This rule is used each time new Virtual Users are created, at each load increase for a ramp-up load policy. | Optional          |
| stop_after       | Define how the population is stopped: the population immediately stop the executing of the current iteration, give a preset [delay](#human-readable-time-specifications) to finish the current iteration or allow the population to end the current iteration for each Virtual User. | Optional          |

**Example 1:**

A test with a ramp-up load of 10 initial Virtual Users in incrementing 5 Virtual Users every 2 seconds. The duration of the test is unlimited (stop manually). The population starts at the start of the test. The Virtual Users simultaneously start. The population immediately stop.

```yaml
scenarios:
- name: MyScenario
  populations:
  - name: MyPopulation
    rampup_load:
      min_users: 10
      increment_users: 5
      increment_every: 2s
```

**Example 2:**

A test with a ramp-up load of 10 initial Virtual Users in incrementing 5 Virtual Users every 2 seconds during 15 minutes and limited at 1500 maximum Virtual Users. The population starts after 45 seconds. The Virtual Users simultaneously start. The population stops after 90 seconds.

```yaml
scenarios:
- name: MyScenario
  populations:
  - name: MyPopulation
    rampup_load:
      min_users: 10
      max_users: 1500
      increment_users: 5
      increment_every: 2s
      duration: 15m
      start_after: 45s
      stop_after: 1m30s  
```

**Example 3:**

A test including 2 load policies:

1 constant load of 100 Virtual Users during 5 iterations. The population starts at the start of the test. The Virtual Users simultaneously start. The population immediately stop.

1 ramp-up load of 100 initial Virtual Users in incrementing 50 Virtual Users every iteration during 30 iterations and limited at 1500 maximum Virtual Users. The population starts after the end of the selected population. The Virtual Users start with 5 seconds. The population stops in waiting the executing of the current iteration.

```yaml
scenarios:
- name: MyScenario
  populations:
  - name: MyPopulation1
    constant_load:
      users: 100
      duration: 5 iterations
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

#### Peaks Load Policy

This load policy generates a load with a fixed number of Virtual Users with periodic phases of low and high load. Useful for checking whether the server recovers its normal behavior after a load peak. 

**Available settings are:**

| Name     | Description                                                  | Required/Optional |
| -------- | ------------------------------------------------------------ | ----------------- |
| minimum  | The phase of low load.                                       | Required          |
| maximum  | The phase of high load.                                      | Required          |
| start    | Select the phase to start.                                   | Required          |
| duration | The duration of the load policy: unlimited, [time](#human-readable-time-specifications) or number of [iterations](#human-readable-iteration-specifications). | Optional          |

**Available settings for the minimum and maximum phases are:**

| Name     | Description                                                  | Required/Optional |
| -------- | ------------------------------------------------------------ | ----------------- |
| users    | The fixed number of Virtual Users.                           | Required          |
| duration | The duration of the phase: [time](#human-readable-time-specifications) or number of [iterations](#human-readable-iteration-specifications). | Required          |

**Advanced settings are:**

| Name        | Description                                                  | Required/Optional |
| ----------- | ------------------------------------------------------------ | ----------------- |
| start_after | Define how the population is started: the population starts at the start of the test, after a preset [delay](#human-readable-time-specifications) or after the end of the selected population. | Optional          |
| step_rampup | Define how Virtual Users start: simultaneously or with a preset [delay](#human-readable-time-specifications). This rule is used each time new Virtual Users are created, at each load peak for a peak load policy. | Optional          |
| stop_after  | Define how the population is stopped: the population immediately stop the executing of the current iteration, give a preset [delay](#human-readable-time-specifications) to finish the current iteration or allow the population to end the current iteration for each Virtual User. | Optional          |

**Example 1:**

A test with a peaks load: a minimum load of 100 Virtual Users during 5 minutes and a maximum load of 500 Virtual Users during 3 minutes. The test starts with the minimum load. The duration of the test is unlimited (stop manually). The population starts at the start of the test. The Virtual Users simultaneously start. The population immediately stop.

```yaml
scenarios:
- name: MyScenario
  populations:
  - name: MyPopulation
    peaks_load:
      minimum:
        users: 100
        duration: 5m
      maximum:
        users: 500
        duration: 3m
      start: minimum
```

**Example 2:**

A test with a peaks load: a minimum load of 100 Virtual Users during 5 minutes and a maximum load of 500 Virtual Users during 3 minutes. The test starts with the maximum load. The duration of the test is 80 minutes. The population starts after 45 seconds. The Virtual Users simultaneously start. The population stops after 90 seconds.

```yaml
scenarios:
- name: MyScenario
  populations:
  - name: MyPopulation
    peaks_load:
      minimum:
        users: 100
        duration: 5m
      maximum:
        users: 500
        duration: 3m
      start: maximum
      duration: 1h20m
      start_after: 45s
      stop_after: 1m30s  
```

**Example 3:**

A test including 2 load policies:

1 constant load of 100 Virtual Users during 5 iterations. The population starts at the start of the test. The Virtual Users simultaneously start. The population immediately stop.

1 peaks load: a minimum load of 100 Virtual Users during 5 iterations and a maximum load of 500 Virtual Users during 3 iterations. The test starts with the minimum load. The duration of the test is 80 iterations. The population starts after the end of the selected population. The Virtual Users start with 15 seconds. The population stops in waiting the executing of the current iteration.

```yaml
scenarios:
- name: MyScenario
  populations:
  - name: MyPopulation1
    constant_load:
      users: 100
      duration: 5 iterations
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

# Human-Readable Time Specifications
All time specifications, including delays and durations, are always expressed in unit of seconds. Use special strings convention to make it human-readable. 

`h` for hours, `m` for minutes, `s` for seconds

**Examples:**
`90s`,`5m`,`2h30m30s`


# Human-Readable Iteration Specifications
All iteration specifications, including durations, are always expressed in unit of iterations. Use special strings convention to make it human-readable. 

`iteration` or `iterations`

**Examples:**
`1 iteration`, `15 iterations`