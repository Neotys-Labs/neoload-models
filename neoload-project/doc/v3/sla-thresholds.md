# Key Performance Indicator (KPI) Thresholds

A KPI threshold is a set of conditions that applies to a single key performance indicator (average response time, hit rate, and so on) and a scope.

Use the following syntax to define a KPI threshold: [`kpi`](#kpi) [`conditions`](#conditions) [`scope`](#scope).

#### Example
Defining a list of KPI thresholds:
```yaml
thresholds:
- avg-request-resp-time warn >= 200ms fail >= 500ms on test
- perc-transaction-resp-time (p90) warn >= 1s fail >= 2s on test
- error-rate warn >= 2% fail >= 5% on test
- error-rate warn >= 5% on interval
```

## KPI

Select the Key Performance Indicator from a [scope](#scope) to apply a rule on.

[Scope](#scope): `on test`

| Key Performance Indicator      | Description                                  | Accepted units  |
|:------------------------------ |:-------------------------------------------- |:---------------:|
| avg-request-resp-time          | Average request response time                | s or ms         |
| avg-page-resp-time             | Average page response time                   | s or ms         |
| avg-transaction-resp-time      | Average Transaction response time            | s or ms         |
| perc-transaction-resp-time     | Percentile Transaction response time.<br><br>The percentile can be defined in using this syntax: (p`percentile`).<br>The default value is (p`90`).<br><br>Example:<br>perc-transaction-resp-time (p95) warn >= 1 s on test | s or ms         |
| avg-request-per-sec            | Average requests per second                  | /s              |
| avg-throughput-per-sec         | Average throughput per second                | Mbps            |
| errors-count                   | Total errors                                 | -               |
| count                          | Total count                                  | -               |
| throughput                     | Total throughput                             | MB              |
| error-rate                     | Error rate                                   | %               |


[Scope](#scope): `on interval`

| Key Performance Indicator      | Description                                  | Accepted units  |
|:------------------------------ |:-------------------------------------------- |:---------------:|
| avg-resp-time                  | Average response time                        | s or ms         |
| avg-elt-per-sec                | Average elements per second                  | /s              |
| avg-throughput-per-sec         | Average throughput per second                | Mbps            |
| errors-per-sec                 | Errors per second                            | /s              |
| error-rate                     | Error rate                                   | %               |

The Key Performance Indicators have restrictions on the elements to which they apply.

> When a configuration element is linked to an SLA profile that contains incompatible Key Performance Indicators:
> - no alert is triggered for these Key Performance Indicators
> - nothing is written to the report

The table below details the restriction of each Key Performance Indicator.

[Scope](#scope): `on test`

| Key Performance Indicator      | User Path Init\|Actions\|End container | Transaction | Page     | Request  | Scenario |
|:------------------------------ |:--------------------------------------:|:-----------:|:--------:|:--------:|:--------:|
| avg-request-resp-time          | -                                      | -           | -        | &#x2713; | &#x2713; |
| avg-page-resp-time             | -                                      | -           | &#x2713; | -        | &#x2713; |
| avg-transaction-resp-time      | -                                      | &#x2713;    | -        | -        | -        |
| perc-transaction-resp-time     | -                                      | &#x2713;    | -        | -        | -        |
| avg-request-per-sec            | -                                      | -           | -        | &#x2713; | &#x2713; |
| avg-throughput-per-sec         | &#x2713;                               | &#x2713;    | &#x2713; | &#x2713; | &#x2713; |
| errors-count                   | &#x2713;                               | &#x2713;    | &#x2713; | &#x2713; | &#x2713; |
| count                          | &#x2713;                               | &#x2713;    | &#x2713; | &#x2713; | &#x2713; |
| throughput                     | &#x2713;                               | &#x2713;    | &#x2713; | &#x2713; | &#x2713; |
| error-rate                     | &#x2713;                               | &#x2713;    | &#x2713; | &#x2713; | &#x2713; |

[Scope](#scope): `on interval`

| Key Performance Indicator      | User Path Init\|Actions\|End container | Transaction | Page     | Request  | Scenario |
|:------------------------------ |:--------------------------------------:|:-----------:|:--------:|:--------:|:--------:|
| avg-resp-time                  | &#x2713;                               | &#x2713;    | &#x2713; | &#x2713; | -        |
| avg-elt-per-sec                | &#x2713;                               | &#x2713;    | &#x2713; | &#x2713; | -        |
| avg-throughput-per-sec         | &#x2713;                               | &#x2713;    | &#x2713; | &#x2713; | -        |
| errors-per-sec                 | &#x2713;                               | &#x2713;    | &#x2713; | &#x2713; | -        |
| error-rate                     | &#x2713;                               | &#x2713;    | &#x2713; | &#x2713; | -        |

## Conditions

Select the conditions to apply a rule on.

Use the following syntax to define a condition: `severity` `operator` `value` `unit`, where:
* `severity` is the alert severity, one of `warn` or `fail`
* `operator` is is the comparison operator, one of `>=`, `<=`, `==`
* `value` is the numeric value you want this rule to apply to
* `unit` is the value unit, one of `s`, `ms`, `/s`, `Mbps`, `MB`, `%`. `unit` is optional.

## Scope

Select the scope to apply a rule on, one of `on test`, `on interval`, where:
* `on test`: These runtime-based KPI thresholds are evaluated at the end of the test on:
  - the global test statistics; and
  - the global statistics for each element linked to a profile.
* `on interval`: These interval-based KPI thresholds are evaluated each time a result is received during test runtime. When a condition in this scope of KPI thresholds is breached, an alert is triggered in real time.

The scope is optional. The default value is `on test`.
