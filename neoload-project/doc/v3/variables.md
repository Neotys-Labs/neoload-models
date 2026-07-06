# Variables
Variables are used to add dynamic content to a scenario.

They may be combined with other variables or with static content (e.g. `${product_${productID}}`) and used in a number of places, such as in form parameter values.

:warning: If the project already have a variable with the same name from NeoLoad, the variable will be __replaced__ by the as-code one.
Variables that does not exist in the project will be added.

#### Example
Defining 6 variables: a Constant variable, a File variable, a Counter variable, a RandomNumber variable, a JavaScript variable and a Secret Vault variable.

```yaml
variables:
- constant:
    name: constant_variable
    value: 12345
- file:
    name: cities_file
    column_names: ["City", "Country", "Population", "Longitude", "Latitude"]
    is_first_line_column_names: false
    start_from_line: 1
    delimiter: ";"
    path: data/list_of_cities.csv
    change_policy: each_iteration
    scope: global
    order: any
    out_of_value: cycle
- counter:
    name: counter_variable
    start: 0
    end: 100
    increment: 2
    change_policy: each_iteration
    scope: local
    out_of_value: cycle
- random_number:
    name: random_number_variable
    min: 0
    max: 999
    predictable: false
    change_policy: each_request
- javascript:
    name: My JSVar
    description: This is a js var
    script: "function evaluate() {\n\tlogger.debug(\"Computing value of js variable\");\n\treturn
      new function() {\n\t\tthis.firstField = \"a value\";\n\t\tthis.secondField =
      myLibraryFunction();\n\t};\n}"
    change_policy: each_iteration
- secret_vault:
    name: db_password
    provider_id: 665f1a2b3c4d5e6f7a8b9c0d
    secret_identifier: my-app/db
```

## Constant variable
A string the value of which cannot be modified.

| Name        | Description                   | Accept variable | Required | Since |
|:----------- |:----------------------------- |:---------------:|:--------:|:-----:|
| name        | The variable name             | -               | &#x2713; |       |
| description | The variable description      | -               | -        |       |
| value       | The variable value            | -               | &#x2713; |       |

#### Example
Defining a Constant variable.

```yaml
constant:
  name: constant_variable
  value: 12345
```

## File variable
A list or table of values loaded from a text file.

| Name                       | Description                                                                                        | Accept variable | Required | Since |
|:-------------------------- |:-------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name                       | The variable name                                                                                  | -               | &#x2713; |       |
| description                | The variable description                                                                           | -               | -        |       |
| column_names               | The list of column names. Use `${<variableName>.<columnName>}` to access variable values. | -               | If is_first_line_column_names is false        |  |
| is_first_line_column_names | If `true`, the first line of the file can be used as column headers name.<br>The value of this parameter is ignored if `column_names` parameter is specified.<br>The default value is `false`. | -               | If column_names is not present        |       |
| start_from_line            | The default value is "1". | -               | -        |       |
| delimiter                  | The delimiter is used to separate data columns.</br>The default value is ",". | -               | -        |       |
| path                       | The relative (compared to the NeoLoad project folder) or absolute path of the source file. | -               | &#x2713;        |       |
| change_policy              | The policy when the value must change. The "change_policy" value can be: <ul><li>`each_use`</li><li>`each_request`</li><li>`each_page`</li><li>`each_iteration`</li><li>`each_user`</li></ul></br>The default value is `each_iteration`. | -               | -        |       |
| scope                      | The value scope can be: <ul><li>`local`</li><li>`global`</li><li>`unique`</li></ul></br>The default value is `global`. | -               | -        |       |
| order                      | The values can be distributed in a set order. The value of order can be:<ul><li>`sequential`</li><li>`random`</li><li>`any`</li></ul></br>The default value is `any`. | -               | -        |       |
| out_of_value               | When no values are left, several policies can be applied. The value of "out_of_value" can be:<ul><li>`cycle`</li><li>`stop_test`</li><li>`no_value_code`</li></ul></br>The default value is `cycle`. | -               | -        |       |

#### Example
Defining a File variable.

```yaml
file:
  name: cities_file
  column_names: ["City", "Country", "Population", "Longitude", "Latitude"]
  is_first_line_column_names: false
  start_from_line: 1
  delimiter: ";"
  path: data/list_of_cities.csv
  change_policy: each_iteration
  scope: global
  order: any
  out_of_value: cycle
```

## Counter variable
A numerical variable having a start value, an end value and an incremental value.

| Name         | Description                   | Accept variable | Required | Since |
|:------------ |:----------------------------- |:---------------:|:--------:|:-----:|
| name         | The variable name             | -               | &#x2713; | 6.10  |
| description  | The variable description      | -               | -        | 6.10  |
| start        | The variable start value      | -               | &#x2713; | 6.10  |
| end          | The variable end value        | -               | &#x2713; | 6.10  |
| increment    | The variable increment value  | -               | &#x2713; | 6.10  |
| change_policy| The policy when the value must change. The "change_policy" value can be: <ul><li>`each_use`</li><li>`each_request`</li><li>`each_page`</li><li>`each_iteration`</li><li>`each_user`</li></ul></br>The default value is `each_iteration`. | -               | -        |6.10|
| scope        | The value scope can be: <ul><li>`local`</li><li>`global`</li><li>`unique`</li></ul></br>The default value is `global`. | -               | -        |6.10|
| out_of_value | When no values are left, several policies can be applied. The value of "out_of_value" can be:<ul><li>`cycle`</li><li>`stop_test`</li><li>`no_value_code`</li></ul></br>The default value is `cycle`. | -               | -        |6.10|


#### Example
Defining a Counter variable.

```yaml
counter:
  name: counter_variable
  start: 0
  end: 100
  increment: 2
  change_policy: each_iteration
  scope: local
  out_of_value: cycle
```

## Random Number variable
A random numerical value within a value range.

| Name         | Description                   | Accept variable | Required | Since |
|:------------ |:----------------------------- |:---------------:|:--------:|:-----:|
| name         | The variable name             | -               | &#x2713; | 6.10  |
| description  | The variable description      | -               | -        | 6.10  |
| min          | The variable min value        | -               | &#x2713; | 6.10  |
| max          | The variable max value        | -               | &#x2713; | 6.10  |
| predictable  | When true, randomly generated values will have comparable values for two identical tests.e            | -               | - |6.10|
| change_policy| The policy when the value must change. The "change_policy" value can be: <ul><li>`each_use`</li><li>`each_request`</li><li>`each_page`</li><li>`each_iteration`</li><li>`each_user`</li></ul></br>The default value is `each_iteration`. | -               | -        |6.10|

#### Example
Defining a Random Number variable.

```yaml
random_number:
  name: random_number_variable
  min: 0
  max: 999
  predictable: false
  change_policy: each_request
```

## JavaScript variable
A variable whose value is the result of the execution of a JavaScript script.

| Name         | Description                   | Accept variable | Required | Since |
|:------------ |:----------------------------- |:---------------:|:--------:|:-----:|
| name         | The variable name             | -               | &#x2713; | 7.2   |
| description  | The variable description      | -               | -        | 7.2   |
| script       | The JavaScript script         | -               | &#x2713; | 7.2   |
| change_policy| The policy when the value must change. The "change_policy" value can be: <ul><li>`each_use`</li><li>`each_request`</li><li>`each_page`</li><li>`each_iteration`</li><li>`each_user`</li></ul></br>The default value is `each_iteration`. | -               | -        | 7.2   |

#### Example
Defining a JavaScript variable.

```yaml
- javascript:
    name: My JSVar
    description: This is a js var
    script: "function evaluate() {\n\tlogger.debug(\"Computing value of js variable\");\n\treturn
      new function() {\n\t\tthis.firstField = \"a value\";\n\t\tthis.secondField =
      myLibraryFunction();\n\t};\n}"
    change_policy: each_iteration
```

## Secret Vault variable
A reference to a secret stored in an external vault provider configured in NeoLoad Web (HashiCorp Vault or AWS Secrets Manager). The provider type is resolved server-side from `provider_id`; it is not stored in the as-code file.

| Name               | Description                                                                 | Accept variable | Required | Since |
|:------------------ |:--------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name               | The variable name                                                           | -               | &#x2713; |       |
| description        | The variable description                                                    | -               | -        |       |
| provider_id        | The opaque NeoLoad Web id of the configured vault provider                  | -               | &#x2713; |       |
| secret_identifier  | The location of the secret within the provider (see mapping below). For HashiCorp Vault, this is the path within the mount — the mount itself (e.g. `secret`) is configured on the vault provider in NeoLoad Web, not in this field. | -               | &#x2713; |       |
| change_policy      | The policy when the value must change. The "change_policy" value can be: <ul><li>`each_use`</li><li>`each_request`</li><li>`each_page`</li><li>`each_iteration`</li><li>`each_user`</li></ul></br>The default value is `each_user`. | -               | -        |       |
| scope              | The value scope can be: <ul><li>`local`</li><li>`global`</li><li>`unique`</li></ul></br>The default value is `local`. | -               | -        |       |

`secret_identifier` maps to a provider-specific concept:

| Provider            | Maps to      | Example                          |
|:------------------- |:------------ |:-------------------------------- |
| HashiCorp Vault     | `secretPath` | `my-app/db` (path within the mount; mount configured on the provider) |
| AWS Secrets Manager | `secretName` | Secret name or ARN               |

#### Example
Defining a Secret Vault variable.

```yaml
secret_vault:
  name: db_password
  provider_id: 665f1a2b3c4d5e6f7a8b9c0d
  secret_identifier: my-app/db
```