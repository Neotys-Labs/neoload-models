# APM Configuration
The apm_configuration defines the APM (Application Performance Management) configuration. For Dynatrace, define the tags of services to monitor and the anomaly rules.

#### Available settings

| Name                                                   | Description                                                                                      | Accept variable | Required | Since |
|:------------------------------------------------------ |:------------------------------------------------------------------------------------------------ |:---------------:|:--------:|:-----:|
| dynatrace_tags                                         | The list of Dynatrace services tags to monitor                                                   | -               | -        |  7.5  |
| [dynatrace_anomaly_rules](dynatrace-anomaly-rules.md)  | The list of rules watched by Dyatrace. A Dynatrace Problem is raised if the value is reached.    | -               | -        |  7.5  |

#### Example

Defining the configuration for the APM Dynatrace:

```yaml
apm_configuration:
  dynatrace_tags:
  - myDynatraceTag
  dynatrace_anomaly_rules:
  - metric_id: builtin:host.cpu.usage
    operator: ABOVE
    value: 90
    severity: PERFORMANCE
```
