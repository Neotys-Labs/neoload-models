# User Paths

#### Available settings are:

| Name        | Description                                                  | Required/Optional |
| ----------- | ------------------------------------------------------------ | ----------------- |
| name        | The name of the User Path.                                   | Required          |
| description | The description of the User Path.                            | Optional          |
| user_sessions  | The available values are : reset_on, reset_off or reset_auto. The default value is reset_auto.  | Optional |
| init         | The init container.                                         | Optional          |
| actions      | The actions container.                                      | Required          |
| and          | The end container.                                          | Optional          |

#### Available containers are:

| Name        | Description                                                  | Required/Optional |
| ----------- | ------------------------------------------------------------ | ----------------- |
| [Transactions](transaction.md)| The transaction list.                      | Optional          |
| [Requests](request.md)        | The request list.                          | Optional          |
| [Delays](delay.md)            | The delays list.                           | Optional          |
| [ThinkTimes](delay.md)        | The think time list.                       | Optional          |
| [Variable Extractors](variable-extractor.md)            | The variable extractor list.                           | Optional          |

#### Example
Defining a User Path.
```yaml
user_paths:
- name: MyUserPath
  actions:
    steps:
    - transaction:
        name: MyTransaction
        steps:
        - request:
            url: http://www.company.com/select?name:product
        - delay: 1s
```