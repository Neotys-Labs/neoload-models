# Transaction 

The Transaction action is used to group together certain actions, namely those relating to a business transaction, in order to extract statistics.

#### Available settings
| Name                           | Description                  | Accept variable   | Required/Optional |
| ------------------------------ | ---------------------------- | ----------------- | ----------------- |
| name                           | The transaction name         | No                | Required          |
| description                    | The transaction description  | No                | Optional          |
| [steps](steps.md)              | Steps of the transaction     | No                | Required          |

#### Example

Defining MyTransaction that contains only 1 Delay.

```yaml
- transaction:
    name: MyTransaction
    description: My First transaction
    steps:
    - delay: 3s
```