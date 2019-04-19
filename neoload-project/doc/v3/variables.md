# Variables
Variables are used to add dynamic content to a scenario.

They may be combined with other variables or with static content (e.g. ${product_${productID}}) and used in a number of places, such as in form parameter values.


#### Example
Defining 4 variables: a Constant variable, a File variable, a Counter variable and a RandomNumber variable.

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
| name         | The variable name             | -               | &#x2713; |6.10|
| description  | The variable description      | -               | -        |6.10|
| start        | The variable start value      | -               | &#x2713; |6.10|
| end          | The variable end value        | -               | &#x2713; |6.10|
| increment    | The variable increment value  | -               | &#x2713; |6.10|
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
| name         | The variable name             | -               | &#x2713; |6.10|
| description  | The variable description      | -               | -        |6.10|
| min          | The variable min value        | -               | &#x2713; |6.10|
| max          | The variable max value        | -               | &#x2713; |6.10|
| predictable  | When true, randomly generated values will have comparable values for two identical tests.e            | -               | &#x2713; |6.10|
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