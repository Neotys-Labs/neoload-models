# User Paths
A User Path simulates the browsing activity of a real visitor.

The User Path is a succession of web pages that may contain logical Actions such as Containers, Loops or Delays to create a more complex behavior.

#### Available settings

| Name         | Description                                                                                                                                                | Accept variable | Required           | Since |
|:------------ |:---------------------------------------------------------------------------------------------------------------------------------------------------------- |:---------------:|:------------------:|:-----:|
| name         | The name of the User Path                                                                                                                                  | -               | &#x2713;           |       |
| description  | The description of the User Path                                                                                                                           | -               | -                  |       |
| user_session | The "user_session" value can be: <ul><li>`reset_on`</li><li>`reset_off`</li><li>`reset_auto`</li></ul></br>The default value is `reset_auto`. | -               | -                  |       |
| sla_profile  | The name of the SLA profile to apply to the User Path (will not be applied to children)                                                                    | -               | -                  | 6.9.0 |
| init         | The init [container](container.md)                                                                                                                         | -               | -                  |       |
| actions      | The actions [container](container.md)                                                                                                                      | -               | &#x2713;           |       |
| end          | The end [container](container.md)                                                                                                                          | -               | -                  |       |

#### Example
Defining a User Path:
```yaml
user_paths:
- name: MyUserPath
  sla_profile: MySlaProfile
  actions:
    steps:
    - request:
        url: http://www.company.com/select?name=book
    - delay: 1s
```
