# Scenario
A scenario defines a test configuration. Select an existing SLA profile to verify and a list of existing populations to test from NeoLoad project. Define a load policy for each population.

#### Available settings

| Name                                | Description                                                                            | Accept variable | Required | Since |
|:----------------------------------- |:-------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name                                | The name of the scenario                                                               | -               | &#x2713; |       |
| description                         | The description of the scenario                                                        | -               | -        |       |
| sla_profile                         | The name of the SLA profile to apply to the Scenario (will not be applied to children) | -               | -        |       |
| [populations](population-policy.md) | The list of name of the existing populations                                           | -               | &#x2713; |       |
| [apm_configuration](apm_configuration.md) | The APM configuration for the monitoring with Dynatrace                          | -               | -        |  7.5  |
| excluded_urls                       | The regular expressions to ignore matching Requests from the scenario during the test playback | -       | -        |  7.6  |
| [rendezvous_policies](rendezvous_policy.md) | The list of the Rendezvous                                                     | -               | -        |  7.6  |
| monitoring(monitoring.md)           | The monitoring parameters                                                              | -               | -        |  7.6  |
| store_variables_for_raw_data        | Is variables for raw data stored                                                       | -               | &#x2713;        |  7.6  |


#### Example

Defining a scenario with 1 SLA profile, 1 population, with Dynatrace monitoring and excluded URLs :

```yaml
scenarios:
- name: MyScenario
  description: My scenario with 1 SLA profile and 1 population
  sla_profile: MySlaProfile
  store_variables_for_raw_data: true
  monitoring:
    before: 30s
    after: 1m30s
  populations:
  - name: MyPopulation
    constant_load:
      users: 500
  rendezvous_policies:
  - name: rendezvous
    when: 50%
    timeout: 50s
  apm_configuration:
    dynatrace_tags:
    - myDynatraceTag
  excluded_urls:
  - .*\.jpg
  - .*\.jpeg
  - .*\.gif
  - .*\.png
```
