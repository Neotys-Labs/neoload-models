# User Paths

#### Available settings are:

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
    do:
    - transaction:
        name: MyTransaction
        do:
        - request:
            url: http://www.company.com/select?name:product
        - delay: 1s
```