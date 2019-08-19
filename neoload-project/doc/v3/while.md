# While

A repetition structure "While - do". The steps contained in the structure are executed in sequence until the condition is not verified any more.

#### Available settings
| Name              | Description                                                                                        | Accept variable | Required           | Since |
|:----------------- |:-------------------------------------------------------------------------------------------------- |:---------------:|:------------------:|:-----:|
| name              | The name of the While                                                                              | -               | -                  | 7.2   |
| description       | The description of the While                                                                       | -               | -                  | 7.2   |
| conditions        | The list of conditions to evaluate                                                                 | -               | &#x2713;           | 7.2   |
| match             | The "match" value can be: <ul><li>`any`</li><li>`all`</li></ul>The default value is `any`.         | -               | -                  | 7.2   |
| [steps](steps.md) | The steps to be executed                                                                           | -               | &#x2713;           | 7.2   |

Use the following syntax to define conditions: `[(condition)(, condition)*]`

Use the following syntax to define condition: `"'operand1 (operator) (operand2)?"`

Operator value can be: <ul><li>`equals`</li><li>`==`</li><li>`not_equals`</li><li>`!=`</li><li>`contains`</li><li>`not_contains`</li><li>`starts_with`</li><li>`not_starts_with`</li><li>`ends_with`</li><li>`not_ends_with`</li><li>`match_regexp`</li><li>`not_match_regexp`</li><li>`greater`</li><li>`>`</li><li>`greater_equal`</li><li>`>=`</li><li>`less`</li><li>`<`</li><li>`less_equal`</li><li>`<=`</li><li>`exists`</li><li>`not_exists`</li></ul>

#### Example
Execute a request while all of the following conditions evaluate to true.
```yaml
actions:
  steps:
    - while:
        name: My While
        description: My description
        conditions:
        - "'${variable1}' equals 'value'"
        - "'${variable2}' equals '2'"
        - "'${variable3}' == 'string with space'"
        - "'${variable4}' != stringwithoutspace"
        - "'${variable5}' not_exist"
        - "'${variable6}' exist"
        - "'${variable7}' == ''"
        - "'${variable8}' equals \"value'with'simple'quote\""
        - "'${variable9}' equals 'value\"with\"double\"quote'"
        - "'${variable10}' equals 'value\\'with"simple\\'and"double\\'quote'"
        - "'' == ''"
        match: all
        steps:
        - request:
          url: http://www.neotys.com/
```