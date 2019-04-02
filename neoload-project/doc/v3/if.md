# If

A conditional structure "If - Then - Else". If the conditions are true, the Then container is executed, otherwise, the Else container is executed.

#### Available settings
| Name         | Description                                                                                        | Accept variable | Required           | Since |
|:------------ |:-------------------------------------------------------------------------------------------------- |:---------------:|:------------------:|:-----:|
| name         | The name of the If                                                                                 | -               | -                  |       |
| description  | The description of the If                                                                          | -               | -                  |       |
| conditions   | The list of conditions to evaluate                                                                 | -               | &#x2713;           |       |
| match        | The "match" value can be: <ul><li>`any`</li><li>`all`</li></ul>The default value is `any`.         | -               | -                  |       |
| then         | The Then [container](container.md)                                                                 | -               | &#x2713;           |       |
| else         | The Else [container](container.md)                                                                 | -               | -                  |       |

Use the following syntax to define conditions: `[(condition)(, condition)*]`

Use the following syntax to define condition: `"'operand1 (operator) (operand2)?"`

Operator value can be: <ul><li>`equals`</li><li>`==`</li><li>`not_equals`</li><li>`!=`</li><li>`contains`</li><li>`not_contains`</li><li>`starts_with`</li><li>`not_starts_with`</li><li>`ends_with`</li><li>`not_ends_with`</li><li>`match_regexp`</li><li>`not_match_regexp`</li><li>`greater`</li><li>`>`</li><li>`greater_equal`</li><li>`>=`</li><li>`less`</li><li>`<`</li><li>`less_equal`</li><li>`<=`</li><li>`exists`</li><li>`not_exists`</li></ul>

#### Example
If at least one condition of the three listed evaluate to true, then execute a request, else execute a delay.
```yaml
actions:
  steps:
    - if:
        name: My If-Then-Else
        description: My description
        conditions: [
          "'${myVariable1}' equals 'value'",
          "'${myVariable1}' == 'value'",
          "'${myVariable1}' != '5'",
          "'${myVariable2}' not_exist"
          "'${myVariable3}' exist",
          "'${myVariable4}' == ''",
          "'${myVariable6}' equals \"value'with'simple'quote\"",
          "'${myVariable7}' equals 'value\"with\"double\"quote'",
          "'${myVariable7}' equals 'value\'with"simple\'and"double\'quote'"
        ]
        match: any
        then:
          description: My then description
          sla_profile: MySLAProfile1
          steps:
            - request:
                url: http://www.neotys.com/
        else:
          description: My else description
          sla_profile: MySLAProfile2
          steps:
            - delay: 3m 200ms
```