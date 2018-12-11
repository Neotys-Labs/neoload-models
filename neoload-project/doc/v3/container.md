# Container 

A container only contains steps to be executed.

#### Available settings are
| Name                  | Description                           | Accept variable   | Required/Optional |
| --------------------- | ------------------------------------- | ----------------- | ----------------- |
| [steps](steps.md)     | The steps to be executed              | No                | Required          |

#### Example
Defining an "actions" container with 2 steps: 1 request and 1 Delay.
```yaml
actions:
  steps:
  - request:
      url: http://www.company.com/overview
  - delay: 1s
```