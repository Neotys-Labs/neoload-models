# Variable Modifier

A Variable Modifier action makes it possible to change the value of a variable separately from its value changing policy. For example, the value of a variable may be modified in a loop for a User Path.

It is necessary to specify the name of the variable on which the action must be applied, for example `myVar`, but not the expression such as `${myVar}`. However, the variable name may be composed, for example `${data_for_${login}}` for the variable `data_for_jsmith`.

#### Available settings

| Name          | Description                                                                                                                                                                                                                                                                                                  | Accept variable | Required | Default       | Since |
|:------------- |:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------------:|:--------:|:-------------:|:-----:|
| name          | The Variable Modifier name                                                                                                                                                                                                                                                                                   | -               |          |               |       |
| description   | The Variable Modifier description                                                                                                                                                                                                                                                                            | -               |          |               |       |
| category      | The category of the variable. The available values are `predefined` and `shared_queue`.                                                                                                                                                                                                                      | -               |          | `predefined`  |       |
| mode          | The operation to apply. Available values depend on `category`:</br>- `predefined`: `next_value` (take next value), `init_value` (reset to first value)</br>- `shared_queue`: `add_shared_queue_value` (add a value to the queue), `poll_shared_queue` (consume a value from the queue into the variable) | -               |          | `next_value`  |       |
| variable_name | The raw name of the target: the variable name when `category` is `predefined`, or the shared queue name when `category` is `shared_queue`. Do not use the `${...}` expression syntax.                                                                                                                        | -               | &#x2713; |               |       |
| value         | The value to add or the variable name to create when consuming. Required when `category` is `shared_queue`. Accepts plain strings and variable references (`${...}`) for `add_shared_queue_value`. Accepts a raw variable name for `poll_shared_queue`.                                                       | &#x2713;        |          |               |       |

#### Constraints

- `value` must **not** be set when `category` is `predefined`.
- `value` is **required** when `category` is `shared_queue`.
- When `category` is `predefined`, `mode` must be `next_value` or `init_value`.
- When `category` is `shared_queue`, `mode` must be `add_shared_queue_value` or `poll_shared_queue`.

#### Examples

Only required — predefined variable, default mode (next value):
```yaml
- variable_modifier:
    variable_name: MyVariableToModify
```

Predefined variable, reset to first value:
```yaml
- variable_modifier:
    name: MyVariableModifier
    description: MyVariableModifierDescription
    category: predefined
    mode: init_value
    variable_name: MyVariableToModify
```

Shared queue — add a plain string value:
```yaml
- variable_modifier:
    name: MyVariableModifier
    category: shared_queue
    mode: add_shared_queue_value
    variable_name: MySharedQueue   # name of the shared queue
    value: HelloWorld
```

Shared queue — add a variable reference:
```yaml
- variable_modifier:
    name: MyVariableModifier
    category: shared_queue
    mode: add_shared_queue_value
    variable_name: MySharedQueue   # name of the shared queue
    value: ${MyVariable}
```

Shared queue — consume a value from the queue into a variable (`value` is the raw name of the destination variable; if it does not exist at runtime, it will be created automatically):
```yaml
- variable_modifier:
    name: MyVariableModifier
    category: shared_queue
    mode: poll_shared_queue
    variable_name: MySharedQueue   # name of the shared queue
    value: MyDestinationVariable
```
