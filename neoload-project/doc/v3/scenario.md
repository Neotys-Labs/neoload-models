# Scenario
A scenario defines a test configuration. Select an existing SLA profile to verify and a list of existing populations to test from NeoLoad project. Define a load policy for each population.

#### Available settings are:

| Name        | Description                                                  | Accept variable   | Required/Optional |
| ----------- | ------------------------------------------------------------ | ----------------- | ----------------- |
| name        | The name of the scenario.                                    | No                | Required          |
| description | The description of the scenario.                             | No                | Optional          |
| sla_profile | The SLA (Service Level Agreement) profile to verify in this scenario. | No                | Optional          |
| populations | The list of existing populations from NeoLoad project.       | No                | Required          |

#### Example

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