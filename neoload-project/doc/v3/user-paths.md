# User Paths

#### Available settings

| Name        | Description                                                     | Accept variable   | Required/Optional |
| ----------- | --------------------------------------------------------------- | ----------------- | ----------------- |
| name        | The name of the User Path.                                      | No                | Required          |
| description | The description of the User Path                                | No                | Optional          |
| user_session  | The "user_session" value can be: <ul><li>`reset_on`</li><li>`reset_off`</li><li>`reset_auto`</li></ul></br>The default value is `reset_auto`. | No  | Optional |
| init         | The init [container](container.md)                             | No                | Optional          |
| actions      | The actions [container](container.md)                          | No                | Required          |
| end          | The end [container](container.md)                              | No                | Optional          |

#### Example
Defining a User Path.
```yaml
user_paths:
- name: MyUserPath
  actions:
    steps:
    - request:
        url: http://www.company.com/select?name=book
    - delay: 1s
```