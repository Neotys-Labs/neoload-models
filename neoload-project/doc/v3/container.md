# Container 

A container only contains steps to be executed.

#### Available settings
| Name                                | Description                                                                                         | Accept variable | Required | Since |
|:----------------------------------- |:--------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| sla_profile                         | The name of the SLA profile to apply to the Container (will not be applied to children)             | -               | -        | 6.9   |
| [steps](steps.md)                   | The steps to be executed                                                                            | -               | &#x2713; |       |
| [assertions](assertion.md)          | The list of assertions to validate the response content of all requests matching criteria within the Container. By default, the validation is applied only on response with content-type text/html or text/xhtml. List of content-types used for response matching can be customized in the Project Settings / Runtime Parameters from the GUI project (cannot be customized with As-code only project). | -               | -        | 7.6   |


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