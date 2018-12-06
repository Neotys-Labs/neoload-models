# User Paths

#### Available settings are:

| Name        | Description                                                  | Required/Optional |
| ----------- | ------------------------------------------------------------ | ----------------- |
| name        | The name of User Path.                                      | Required          |
...
  + [Transactions](transaction.md)
  + [Requests](request.md)
  + [Delays](delay.md)
  + [ThinkTimes](delay.md)
  + [Variable Extractors](variable-extractor.md)

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