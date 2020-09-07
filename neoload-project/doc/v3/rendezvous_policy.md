# Rendezvous Policy
The apm_configuration defines the APM (Application Performance Management) configuration. For Dynatrace, define the tags of services to monitor and the anomaly rules.

#### Available settings

| Name                                                   | Description                                   | Accept variable | Required | Since |
|:------------------------------------------------------ |:--------------------------------------------- |:---------------:|:--------:|:-----:|
| name                                                   | The name of the rendezvous policy.            | -               | -        |  7.6  |
| [when](when.md)                                        | When to release the rendezvous.               | -               | -        |  7.6  |
| timeout                                                | The timeout between virtual users.            | -               | -        |  7.6  |

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
