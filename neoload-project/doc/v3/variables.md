# Variables
Variables are used to add dynamic content to a scenario.

They may be combined with other variables or with static content (e.g. `${product_${productID}}`) and used in a number of places, such as in form parameter values.

:warning: If the project already have a variable with the same name from NeoLoad, the variable will be __replaced__ by the as-code one.
Variables that does not exist in the project will be added.

#### Example
Defining 9 variables: a Constant variable, a File variable, a List variable, a Counter variable, a RandomNumber variable, a RandomString variable, a RandomUUID variable, a SQL variable and a JavaScript variable.

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
- list:
    name: cities_list
    column_names: ["City", "Country"]
    values:
    - ["Paris", "France"]
    - ["London", "UK"]
    start_from_line: 1
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
- random_string:
    name: random_string_variable
    min_length: 5
    max_length: 10
    predictable: false
    change_policy: each_use
- random_uuid:
    name: random_uuid_variable
    upper_case: false
    predictable: false
    change_policy: each_use
- sql:
    name: sql_variable
    driver: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/mydb
    login: login_admin
    password: password_admin
    query: "SELECT username, email FROM users"
    column_names: ["username", "email"]
    change_policy: each_iteration
    scope: global
    order: any
    out_of_value: cycle
- javascript:
    name: My JSVar
    description: This is a js var
    script: "function evaluate() {\n\tlogger.debug(\"Computing value of js variable\");\n\treturn
      new function() {\n\t\tthis.firstField = \"a value\";\n\t\tthis.secondField =
      myLibraryFunction();\n\t};\n}"
    change_policy: each_iteration
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

## List variable
A list or table of values defined inline in the YAML file.

| Name          | Description                   | Accept variable | Required | Since |
|:------------- |:----------------------------- |:---------------:|:--------:|:-----:|
| name          | The variable name             | -               | &#x2713; |       |
| description   | The variable description      | -               | -        |       |
| column_names  | The list of column names. Use `${<variableName>.<columnName>}` to access variable values. | -               | &#x2713; |       |
| values        | The list of rows. Each row is a list of values, one per column of `column_names`. | -               | &#x2713; |       |
| start_from_line | The first row of `values` to be used. The default value is "1". | -               | -        |       |
| change_policy | The policy when the value must change. The "change_policy" value can be: <ul><li>`each_use`</li><li>`each_request`</li><li>`each_page`</li><li>`each_iteration`</li><li>`each_user`</li></ul></br>The default value is `each_iteration`. | -               | -        |       |
| scope         | The value scope can be: <ul><li>`local`</li><li>`global`</li><li>`unique`</li></ul></br>The default value is `global`. | -               | -        |       |
| order         | The values can be distributed in a set order. The value of order can be:<ul><li>`sequential`</li><li>`random`</li><li>`any`</li></ul></br>The default value is `any`. | -               | -        |       |
| out_of_value  | When no values are left, several policies can be applied. The value of "out_of_value" can be:<ul><li>`cycle`</li><li>`stop_test`</li><li>`no_value_code`</li></ul></br>The default value is `cycle`. | -               | -        |       |

#### Example
Defining a List variable.

```yaml
list:
  name: cities_list
  column_names: ["City", "Country"]
  values:
  - ["Paris", "France"]
  - ["London", "UK"]
  start_from_line: 1
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

## Random String variable
A random alphanumeric string the length of which is within a length range.

| Name         | Description                   | Accept variable | Required | Since |
|:------------ |:----------------------------- |:---------------:|:--------:|:-----:|
| name         | The variable name             | -               | &#x2713; | 2026.3|
| description  | The variable description      | -               | -        | 2026.3|
| min_length   | The minimum number of characters of the generated value.</br>The default value is `5`. | -               | -        | 2026.3|
| max_length   | The maximum number of characters of the generated value.</br>The default value is `10`. | -               | -        | 2026.3|
| predictable  | When true, randomly generated values will have comparable values for two identical tests.</br>The default value is `false`. | -               | -        | 2026.3|
| change_policy| The policy when the value must change. The "change_policy" value can be: <ul><li>`each_use`</li><li>`each_request`</li><li>`each_page`</li><li>`each_iteration`</li><li>`each_user`</li></ul></br>The default value is `each_iteration`. | -               | -        | 2026.3|

#### Example
Defining a Random String variable.

```yaml
random_string:
  name: random_string_variable
  description: MyRandomStringDescription
  min_length: 10
  max_length: 20
  predictable: true
  change_policy: each_use
```

## Random UUID variable
A random UUID value.

| Name         | Description                   | Accept variable | Required | Since |
|:------------ |:----------------------------- |:---------------:|:--------:|:-----:|
| name         | The variable name             | -               | &#x2713; | 2026.3|
| description  | The variable description      | -               | -        | 2026.3|
| upper_case   | When true, the generated UUID is uppercase.</br>The default value is `false`. | -               | -        | 2026.3|
| predictable  | When true, randomly generated values will have comparable values for two identical tests.</br>The default value is `false`. | -               | -        | 2026.3|
| change_policy| The policy when the value must change. The "change_policy" value can be: <ul><li>`each_use`</li><li>`each_request`</li><li>`each_page`</li><li>`each_iteration`</li><li>`each_user`</li></ul></br>The default value is `each_use`. | -               | -        | 2026.3|

#### Example
Defining a Random UUID variable.

```yaml
random_uuid:
  name: random_uuid_variable
  description: MyRandomUUIDDescription
  upper_case: true
  predictable: true
  change_policy: each_use
```

## SQL variable
A list or table of values loaded from the result of a SQL query executed on a database.

| Name         | Description                   | Accept variable | Required | Since |
|:------------ |:----------------------------- |:---------------:|:--------:|:-----:|
| name         | The variable name             | -               | &#x2713; | 2026.3|
| description  | The variable description      | -               | -        | 2026.3|
| driver       | The JDBC driver class name of the database.</br>When not specified, the driver is derived from `url`. | -               | -        | 2026.3|
| url          | The JDBC connection url of the database. | -               | &#x2713; | 2026.3|
| login        | The login used to connect to the database. | -               | -        | 2026.3|
| password     | The password used to connect to the database. | -               | -        | 2026.3|
| query        | The SQL query returning the variable values. | -               | &#x2713; | 2026.3|
| column_names | The list of column names. Use `${<variableName>.<columnName>}` to access variable values. | -               | -        | 2026.3|
| change_policy| The policy when the value must change. The "change_policy" value can be: <ul><li>`each_use`</li><li>`each_request`</li><li>`each_page`</li><li>`each_iteration`</li><li>`each_user`</li></ul></br>The default value is `each_iteration`. | -               | -        | 2026.3|
| scope        | The value scope can be: <ul><li>`local`</li><li>`global`</li><li>`unique`</li></ul></br>The default value is `global`. | -               | -        | 2026.3|
| order        | The values can be distributed in a set order. The value of order can be:<ul><li>`sequential`</li><li>`random`</li><li>`any`</li></ul></br>The default value is `any`. | -               | -        | 2026.3|
| out_of_value | When no values are left, several policies can be applied. The value of "out_of_value" can be:<ul><li>`cycle`</li><li>`stop_test`</li><li>`no_value_code`</li></ul></br>The default value is `cycle`. | -               | -        | 2026.3|

#### Example
Defining a SQL variable.

```yaml
sql:
  name: sql_variable
  driver: com.mysql.jdbc.Driver
  url: jdbc:mysql://localhost:3306/mydb
  login: login_admin
  password: password_admin
  query: "SELECT username, email FROM users"
  column_names: ["username", "email"]
  change_policy: each_iteration
  scope: global
  order: any
  out_of_value: cycle
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

## Shared Queue variable
A queue shared between virtual users, usable as a producer/consumer channel, with optional persistence to a file.

| Name                    | Description                                                                 | Accept variable | Required | Since |
|:----------------------- |:--------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name                    | The variable name                                                           | -               | &#x2713; | 2026.3|
| description             | The variable description                                                    | -               | -        | 2026.3|
| queue_size              | The maximum number of elements the queue can hold.</br>The default value is `10000`. | -    | -        | 2026.3|
| consumer_timeout        | The time, in milliseconds, a consumer waits for a value before giving up.</br>The default value is `5000`. | - | -   | 2026.3|
| swap_file               | The file used to persist the queue content. See below. When absent, no file swap is used. | -   | -        | 2026.3|
| swap_file.path          | The relative (compared to the NeoLoad project folder) or absolute path of the swap file. | -    | &#x2713; | 2026.3|
| swap_file.delimiter     | The delimiter used to separate data columns in the swap file.</br>The default value is `;`. | -    | -        | 2026.3|
| swap_file.load_from_file| If `true`, the queue is populated from the swap file at test start.</br>The default value is `false`. | -    | -        | 2026.3|
| swap_file.save_to_file  | If `true`, the queue content is written to the swap file at test end.</br>The default value is `true`. | -    | -        | 2026.3|

#### Example
Defining a Shared Queue variable.

```yaml
shared_queue:
  name: MySharedQueue
  description: MySharedQueueDescription
  queue_size: 5000
  consumer_timeout: 2000
  swap_file:
    path: data/my_queue.csv
    delimiter: ","
    load_from_file: true
    save_to_file: false
```