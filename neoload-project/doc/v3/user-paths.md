# User Paths
A User Path simulates the browsing activity of a real visitor.

The User Path is a succession of web pages that may contain logical Actions such as Containers, Loops or Delays to create a more complex behavior.

:warning: If the project already have a User Path with the same name from NeoLoad, the whole User Path will be __replaced__ by the as-code one.

#### Available settings

| Name                                | Description                                                                                                                                                | Accept variable | Required           | Since |
|:----------------------------------- |:---------------------------------------------------------------------------------------------------------------------------------------------------------- |:---------------:|:------------------:|:-----:|
| name                                | The name of the User Path                                                                                                                                  | -               | &#x2713;           |       |
| description                         | The description of the User Path                                                                                                                           | -               | -                  |       |
| user_session                        | The "user_session" value can be: <ul><li>`reset_on`</li><li>`reset_off`</li><li>`reset_auto`</li></ul></br>The default value is `reset_auto`.              | -               | -                  |       |
| init                                | The init [container](container.md)                                                                                                                         | -               | -                  |       |
| actions                             | The actions [container](container.md)                                                                                                                      | -               | &#x2713;           |       |
| end                                 | The end [container](container.md)                                                                                                                          | -               | -                  |       |
| [assertions](assertion.md)          | The list of assertions to validate the response content of all requests matching criteria within the User Path. By default, the validation is applied only on response with content-type text/html or text/xhtml. List of content-types used for response matching can be customized in the Project Settings / Runtime Parameters from the GUI project (cannot be customized with As-code only project). | -               | -                  | 7.6   |


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
