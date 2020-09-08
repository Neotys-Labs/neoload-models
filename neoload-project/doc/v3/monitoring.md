# Monitoring
The monitoring configuration to apply to the scenario.

#### Available settings

| Name                                                   | Description                                   | Accept variable | Required | Since |
|:------------------------------------------------------ |:--------------------------------------------- |:---------------:|:--------:|:-----:|
| before                                                 | The name of the rendezvous.            | -               | -        |  7.6  |
| after                                                  | The timeout between virtual users.            | -               | -        |  7.6  |

#### Example

Defining the monitoring configuration for the scenario:

```yaml
monitoring:
  before: 20s
  after: 2m
```
