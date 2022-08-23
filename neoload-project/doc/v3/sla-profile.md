# Service Level Agreement (SLA) Profile
Service Level Agreement (SLA) profiles allow defining objectives regarding quality of service. For example, they can be used to check that the average response time for a request does not exceed a given value.

An SLA profile is a collection of [KPI thresholds](sla-thresholds.md). SLA profiles may be used by the following elements:
* User Path init [container](container.md)
* User Path actions [container](container.md)
* User Path end [container](container.md)
* [Transaction](transaction.md)
* [Request](request.md)
* [Scenario](scenario.md)

:warning: If the project already have an SLA profile with the same name from NeoLoad, the whole SLA profile will be __replaced__ by the as-code one.

#### Available settings

| Name                            | Description                                                  | Accept variable | Required           | Since |
|:------------------------------- |:------------------------------------------------------------ |:---------------:|:------------------:|:-----:|
| name                            | The name of the SLA Profile                                  | -               | &#x2713;           | 6.9   |
| description                     | The description of the SLA Profile                           | -               | -                  | 6.9   |
| [thresholds](sla-thresholds.md) | The definition of KPI thresholds                             | -               | &#x2713;           | 6.9   |

#### Example
Defining a SLA Profile:
```yaml
sla_profiles:
- name: MySlaProfile
  thresholds:
  - avg-request-resp-time warn >= 200ms fail >= 500ms per test
  - perc-transaction-resp-time (p90) warn >= 1s fail >= 2s per test
  - error-rate warn >= 2% fail >= 5% per test
  - error-rate warn >= 5% per interval
```
