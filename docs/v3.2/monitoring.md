# Monitoring
The monitoring configuration to apply to the Scenario.

#### Available settings

| Name                                                   | Description                                   | Accept variable | Required | Since |
|:------------------------------------------------------ |:--------------------------------------------- |:---------------:|:--------:|:-----:|
| before                                                 | The monitoring time before the first Virtual User starts. Monitoring time is expressed in hours (h), minutes (m), seconds (s). | -               | -        |  7.6  |
| after                                                  | The monitoring time after the last Virtual User stops. Monitoring time is expressed in hours (h), minutes (m), seconds (s).  | -               | -        |  7.6  |

#### Example

Defining the monitoring configuration for the Scenario:

```yaml
monitoring:
  before: 20s
  after: 2m
```
