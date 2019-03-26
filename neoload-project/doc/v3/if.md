# If

A conditional structure "If - Then - Else". If the conditions are true, the Then container is executed, otherwise, the Else container is executed.

#### Available settings
| Name         | Description                                                                                        | Accept variable | Required           | Since |
|:------------ |:-------------------------------------------------------------------------------------------------- |:---------------:|:------------------:|:-----:|
| name         | The name of the If                                                                                 | -               | -                  |       |
| description  | The description of the If                                                                          | -               | -                  |       |
| conditions   | The list of conditions to evaluate                                                                 | -               | &#x2713;           |       |
| match        | The "match" value can be: <ul><li>`any`</li><li>`all`</li></ul></br>The default value is `any`.    | -               | -                  |       |
| then         | The Then [container](container.md)                                                                 | -               | &#x2713;           |       |
| else         | The Else [container](container.md)                                                                 | -               | -                  |       |

Use the following syntax to define conditions: `[(condition)(, condition)*]`
Use the following syntax to define condition: `"'operand1 (operator) (operand2)?"`
Operator value can be: <ul><li>`equals`</li><li>`==`</li><li>`not_equals`</li><li>`!=`</li><li>`contains`</li><li>`not_contains`</li><li>`starts_with`</li><li>`not_starts_with`</li><li>`ends_with`</li><li>`not_ends_with`</li><li>`match_regexp`</li><li>`not_match_regexp`</li><li>`greater`</li><li>`>`</li><li>`greater_equal`</li><li>`>=`</li><li>`less`</li><li>`<`</li><li>`less_equal`</li><li>`<=`</li><li>`exists`</li><li>`not_exists`</li></ul>

#### Example
Defining an "actions" container with 2 steps: 1 request and 1 Delay.
```yaml
actions:
  steps:
    - if:
        name: My If-Then-Else
        description: My description
        conditions: [
          "'operand1' equals 'operand2'",
          "'0' == 'operand2'",
          "'operand1' not_equals '${parameter}'",
          "'${parameter}' != '10'",
          "'${parameter}' contains 'contains'",
          "'operand1' not_contains 'operand2'",
          "'operand1' starts_with '=='",
          "'operand1' not_starts_with 'operand2'",
          "'operand1' ends_with 'operand2'",
          "'operand1' not_ends_with 'operand2'",
          "'operand1' match_regexp 'operand2'",
          "'operand1' not_match_regexp 'operand2'",
          "'operand1' greater 'operand2'",
          "'operand1' > 'operand2'",
          "'operand1' greater_equal 'operand2'",
          "'operand1' >= 'operand2'",
          "'operand1' less 'operand2'",
          "'operand1' < 'operand2'",
          "'operand1' less_equal 'operand2'",
          "'operand1' <= 'operand2'",
          "'operand1' exists 'operand2'",
          "'operand1' not_exists 'operand2'"
        ]
        match: any
        then:
          description: My then description
          sla_profile: MySLAProfile
          steps:
            - request:
                url: http://www.neotys.com/select
        else:
          description: My else description
          sla_profile: MySLAProfile
          steps:
            - delay: 3m 200ms
```