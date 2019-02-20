# Transaction 

The Transaction Action is used to group together certain actions, namely those relating to a business transaction, in order to extract statistics.

#### Available settings
| Name              | Description                                                                               | Accept variable | Required | Since |
|:----------------- |:----------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name              | The Transaction name                                                                      | -               | &#x2713; |       |
| description       | The Transaction description                                                               | -               | -        |       |
| sla_profile       | The name of the SLA profile to apply to the Transaction (will not be applied to children) | -               | -        | 6.9   |
| [steps](steps.md) | Steps of the Transaction                                                                  | -               | &#x2713; |       |

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
