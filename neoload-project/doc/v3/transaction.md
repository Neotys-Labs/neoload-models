# Transaction 

The Transaction action is used to group together certain actions, namely those relating to a business transaction, in order to extract statistics.

#### Available settings are
| Name                           | Description                  | Required/Optional |
| ------------------------------ | ---------------------------- | ----------------- |
| name                           | The transaction name         | Required          |
| description                    | The transaction description  | Required          |
| [Transactions](transaction.md) | The transaction list.        | Optional          |
| [Requests](request.md)         | The request list.            | Optional          |
| [Delays](delay.md)             | The delays list.             | Optional          |
| [ThinkTimes](think_time.md)    | The think time list.         | Optional          |

#### Example

```yaml
- transaction:
    name: MyTransaction
    description: My First transaction
    steps:
    - delay: 3s
```