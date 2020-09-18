# Transaction 

The Transaction Action is used to group together certain actions, namely those relating to a business transaction, in order to extract statistics.

#### Available settings
| Name                                | Description                                                                                           | Accept variable | Required | Since |
|:----------------------------------- |:----------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name                                | The Transaction name                                                                                  | -               | &#x2713; |       |
| description                         | The Transaction description                                                                           | -               | -        |       |
| sla_profile                         | The name of the SLA profile to apply to the Transaction (will not be applied to children)             | -               | -        | 6.9   |
| [steps](steps.md)                   | Steps of the Transaction                                                                              | -               | &#x2713; |       |
| [assert_content](assert_content.md) | The list of assertions to validate the response content of all requests matching a criteria within the Transaction. By default, the validation is applied only on response with content-type text/html or text/xhtml. List of content-types used for response matching can be customized in the GUI project, in the Project Settings / Runtime Parameters (cannot be customized with As-code only project). | -               | -        | 7.6   |

#### Example

Defining MyTransaction that contains only 1 Delay.

```yaml
- transaction:
    name: MyTransaction
    description: My First transaction
    sla_profile: MySlaProfile
    steps:
    - delay: 3s
```
