# User Paths

#### Available settings are:

| Name        | Description                                                  | Required/Optional |
| ----------- | ------------------------------------------------------------ | ----------------- |
| name        | The name of the User Path.                                   | Required          |
| description | The description of the User Path.                            | Optional          |
| user_sessions  | The "user_sessions" value can be: <ul><li>`reset_on`</li><li>`reset_off`</li><li>`reset_auto`</li></ul></br>The default value is `reset_auto`. | Optional |
| init         | The init container.                                         | Optional          |
| actions      | The actions container.                                      | Required          |
| end          | The end container.                                          | Optional          |

#### Available containers are:

| Name        | Description                                                  | Required/Optional |
| ----------- | ------------------------------------------------------------ | ----------------- |
| [Transactions](transaction.md)| The transaction list.                      | Optional          |
| [Requests](request.md)        | The request list.                          | Optional          |
| [Delays](delay.md)            | The delays list.                           | Optional          |
| [ThinkTimes](think_time.md)   | The think times list.                      | Optional          |

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