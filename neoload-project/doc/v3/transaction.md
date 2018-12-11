# Transaction 

The Transaction action is used to group together certain actions, namely those relating to a business transaction, in order to extract statistics.

#### Available settings are
| Name                           | Description                  | Required/Optional |
| ------------------------------ | ---------------------------- | ----------------- |
| name                           | The transaction name         | Required          |
| description                    | The transaction description  | Optional          |
| [steps](steps.md)              | Steps of the transaction     | Required          |

#### Example

Defining MyTransaction that contains only 1 Delay.

```yaml
- transaction:
    name: MyTransaction
    description: My First transaction
    steps:
    - delay: 3s
```