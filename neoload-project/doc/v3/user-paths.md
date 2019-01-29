# User Paths
A User Path simulates the browsing activity of a real visitor.

The User Path is a succession of web pages that may contain logical Actions such as Containers, Loops or Delays to create a more complex behavior.

#### Available settings

| Name         | Description                                                                  | Accept variable | Required/Optional |
| ------------ | ---------------------------------------------------------------------------- | --------------- | ----------------- |
| name         | The name of the User Path                                                    | No              | Required          |
| description  | The description of the User Path                                             | No              | Optional          |
| user_session | The "user_session" value can be: <ul><li>`reset_on`</li><li>`reset_off`</li><li>`reset_auto`</li></ul></br>The default value is `reset_auto`. | No  | Optional |
| sla_profile  | The SLA (Service Level Agreement) profile applied to the 'User Path' element | No              | Optional          |
| init         | The init [container](container.md)                                           | No              | Optional          |
| actions      | The actions [container](container.md)                                        | No              | Required          |
| end          | The end [container](container.md)                                            | No              | Optional          |

#### Example
Defining a User Path:
```yaml
user_paths:
- name: MyUserPath
  sla_profile: MySlaProfileForUserPath
  actions:
    steps:
    - request:
        url: http://www.company.com/select?name=book
    - delay: 1s
```
