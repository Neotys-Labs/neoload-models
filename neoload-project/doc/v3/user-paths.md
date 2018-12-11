# User Paths

#### Available settings are:

| Name        | Description                                                     | Required/Optional |
| ----------- | --------------------------------------------------------------- | ----------------- |
| name        | The name of the User Path.                                      | Required          |
| description | The description of the User Path                                | Optional          |
| user_sessions  | The "user_sessions" value can be: <ul><li>`reset_on`</li><li>`reset_off`</li><li>`reset_auto`</li></ul></br>The default value is `reset_auto`. | Optional |
| init         | The init [container](container.md)                             | Optional          |
| actions      | The actions [container](container.md)                          | Required          |
| end          | The end [container](container.md)                              | Optional          |

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