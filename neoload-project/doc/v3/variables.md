# Variables
Variables are used to add dynamic content to a scenario.

They may be combined with other variables or with static content (e.g. ${product_${productID}}) and used in a number of places, such as in form parameter values.


#### Example
Defining 2 variables: a Constant variable, and a File variable.

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
| column_names               | The list of column names. Use `${<variableName>.<columnName>}` to access variable values. | -               | -        |       |
| is_first_line_column_names | If `true`, the first line of the file can be used as column headers name.<br>The value of this parameter is ignored if `column_names` parameter is specified.<br>The default value is `false`. | -               | -        |       |
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

