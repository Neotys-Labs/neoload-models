# Duration assertion
A duration assertion checks that a [request](request.md) or [custom_action](custom_action.md) completed within a given duration. The step is considered failed if its execution time is greater than or equal to `less_than`.

#### Available settings

| Name      | Description                                          | Accept variable | Required | Since |
|:--------- | :---------------------------------------------------- |:---------------:|:--------:|:-----:|
| less_than | The maximum allowed duration, in milliseconds.        | -               | &#x2713; | 3.1   |

#### Example 1

Defining a duration assertion on a request.

```yaml
- request:
    url: https://www.tricentis.com/
    duration_assertion:
      less_than: 2048
```

#### Example 2

Defining a duration assertion on a custom action.

```yaml
- custom_action:
    name: sql action
    type: SQL
    duration_assertion:
      less_than: 500
```
