# Container 

A container only contains steps to be executed.

#### Available settings
| Name                                | Description                                                                                         | Accept variable | Required | Since |
|:----------------------------------- |:--------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| sla_profile                         | The name of the SLA profile to apply to the Container (will not be applied to children)             | -               | -        | 6.9   |
| [steps](steps.md)                   | The steps to be executed                                                                            | -               | &#x2713; |       |
| [assert_content](assert_content.md) | The list of assertions to validate the response content of all requests matching a criteria within the Container (text/html and text/xhtml content types by default. Cannot be set with as-code only. See Project Settings / Runtime Parameters when as-code overloads a GUI project). | -               | -        | 7.6   |


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