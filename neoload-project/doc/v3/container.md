# Container 

A container only contains steps to be executed.

#### Available settings are
| Name                  | Description                           | Required/Optional |
| --------------------- | ------------------------------------- | ----------------- |
| [steps](steps.md)     | Steps to be executed                  | Required          |

#### Example
Defining a Container "actions" with 2 steps: 1 request and 1 Delay.
```yaml
actions:
  steps:
  - request:
      url: http://www.company.com/overview
  - delay: 1s
```