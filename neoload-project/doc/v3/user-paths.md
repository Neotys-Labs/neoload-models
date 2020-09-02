# User Paths
A User Path simulates the browsing activity of a real visitor.

The User Path is a succession of web pages that may contain logical Actions such as Containers, Loops or Delays to create a more complex behavior.

#### Available settings

| Name                                | Description                                                                                                                                                | Accept variable | Required           | Since |
|:----------------------------------- |:---------------------------------------------------------------------------------------------------------------------------------------------------------- |:---------------:|:------------------:|:-----:|
| name                                | The name of the User Path                                                                                                                                  | -               | &#x2713;           |       |
| description                         | The description of the User Path                                                                                                                           | -               | -                  |       |
| user_session                        | The "user_session" value can be: <ul><li>`reset_on`</li><li>`reset_off`</li><li>`reset_auto`</li></ul></br>The default value is `reset_auto`.              | -               | -                  |       |
| init                                | The init [container](container.md)                                                                                                                         | -               | -                  |       |
| actions                             | The actions [container](container.md)                                                                                                                      | -               | &#x2713;           |       |
| end                                 | The end [container](container.md)                                                                                                                          | -               | -                  |       |
| [assert_content](assert_content.md) | The list of assertions allowing to check the validity of the responses content within the User Path                                                                                                                                         | -               | -                  | 7.6   |


#### Example
Defining a User Path:
```yaml
user_paths:
- name: MyUserPath
  actions:
    steps:
    - request:
        url: http://www.company.com/select?name=book
    - delay: 1s
```
