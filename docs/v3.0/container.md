# Container 

A container only contains steps to be executed.

#### Available settings
| Name                                | Description                                                                                         | Accept variable | Required | Since |
|:----------------------------------- |:--------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| sla_profile                         | The name of the SLA profile to apply to the Container (will not be applied to children)             | -               | -        | 6.9   |
| [steps](steps.md)                   | The steps to be executed                                                                            | -               | &#x2713; |       |
| assertions                          | The list of assertions to validate the response content of all requests matching criteria within the Container. See [content assertions](content_assertion.md).                                                                                                                     | -               | -        | 7.6   |

#### Example
Defining an "actions" container with 2 steps: 1 request and 1 Delay.
```yaml
actions:
  sla_profile: MySlaProfile
  steps:
  - request:
      url: http://www.company.com/overview
  - delay: 1s
```
