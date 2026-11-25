# Dynatrace anomaly rule
The Dynatrace anomaly rule defines a rule created in Dynatrace that helps Dynatrace detect Problems.

#### Available settings

| Name                                | Description                                                                                      | Accept variable | Required | Since |
|:----------------------------------- |:------------------------------------------------------------------------------------------------ |:---------------:|:--------:|:-----:|
| metric_id                           | The identifier of the metric.                                                                    | -               | &#x2713; |  7.5  |
| operator                            | The operator. Two possible values: <ul><li>BELOW</li><li>ABOVE</li>                              | -               | &#x2713; |  7.5  |
| value                               | The value of the threshold.                                                                      | -               | &#x2713; |  7.5  |
| severity                            | The severity of the problem. Six possible values: <ul><li>AVAILABILITY</li><li>CUSTOM_ALERT</li><li>ERROR</li><li>INFO</li><li>PERFORMANCE</li><li>RESOURCE_CONTENTION</li></ul>    | -               | &#x2713; |  7.5  |

#### Example

Define an anomaly rule to watch the CPU usage on hosts. \
A PERFORMANCE Problem is raised if the value is ABOVE 90%. \
Watched hosts are those linked to services that have the specified tags (see dynatrace_tags in [monitoring](apm_configuration.md)). 

```yaml
metric_id: builtin:host.cpu.usage
operator: ABOVE
value: 90
severity: PERFORMANCE
```
