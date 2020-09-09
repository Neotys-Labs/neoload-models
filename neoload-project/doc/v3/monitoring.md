# Monitoring
The monitoring configuration to apply to the Scenario.

#### Available settings

| Name                                                   | Description                                   | Accept variable | Required | Since |
|:------------------------------------------------------ |:--------------------------------------------- |:---------------:|:--------:|:-----:|
| before                                                 | The monitor time before the first Virtual user start.            | -               | -        |  7.6  |
| after                                                  | The monitor time after the last Virtual user stop.            | -               | -        |  7.6  |

#### Example

Defining the monitoring configuration for the Scenario:

```yaml
monitoring:
  before: 20s
  after: 2m
```
