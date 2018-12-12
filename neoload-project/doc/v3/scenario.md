# Scenario
A scenario defines a test configuration. Select an existing SLA profile to verify and a list of existing populations to test from NeoLoad project. Define a load policy for each population.

#### Available settings

| Name        | Description                                                  | Accept variable   | Required/Optional |
| ----------- | ------------------------------------------------------------ | ----------------- | ----------------- |
| name        | The name of the scenario                                     | No                | Required          |
| description | The description of the scenario                              | No                | Optional          |
| sla_profile | The SLA (Service Level Agreement) profile to verify in this scenario | No                | Optional          |
| [populations](population.md) | The list of existing populations from NeoLoad project       | No                | Required          |

#### Example

Defining a scenario with 1 SLA profile and 1 population with a *ramp-up* load.

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