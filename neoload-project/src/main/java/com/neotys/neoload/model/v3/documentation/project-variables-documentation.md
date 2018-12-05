# NeoLoad Variable definition
Definition has several top-level keys.

| Name      | Description        |
| --------- | ------------------ |
| variables   | List of variables    |
You can find below an example of how to define variables when launching a test in command line using the `-project` parameter.

Please read the full [command line documentation](https://www.neotys.com/documents/doc/neoload/latest/en/html/#643.htm).

See more details about [Variables in NeoLoad](https://www.neotys.com/documents/doc/neoload/latest/en/html/#1057.htm).

### Constant variable
A string which value cannot be modified.

| Name        | Description                   | Required/Optional |
| ----------- | ----------------------------- | ----------------- |
| name        | The variable name.            | Required          |
| description | The variable description.     | Optional          |
| value       | The variable value.           | Required          |


### File variable
A list or table of values loaded from a text file.

| Name          | Description                                       | Required/Optional |
| ------------- | ------------------------------------------------- | ----------------- |
| name          | The variable name.                                | Required          |
| description   | The variable description.                         | Optional          |
| column_names  | The list of column names. Use `${\<variableName>.\<columnName>}` to access to variable values. | Optional          |
| is_first_line_column_names | If `true`, the first line of the file can be used as columns headers name.<br>The value of this parameter is ignored if `column_names` parameter is specified.<br>The default value is `false`.| Optional          |
| start_from_line | The default value is 1.  | Optional          |
| delimiter     | The delimiter is used to separate data columns.</br>The default value is ",".| Optional          |
| path          | The relative (compared to the NeoLoad project folder) or absolute path of the source file.| Required          |
| change_policy | The policy when the value must change. The change_policy value can be: <ul><li>`each_use`</li><li>`each_request`</li><li>`each_page`</li><li>`each_iteration`</li><li>`each_user`</li></ul></br>The default value is `each_iteration`.| Optional          |
| scope         | The value scope can be: <ul><li>`local`</li><li>`global`</li><li>`unique`</li></ul></br>The default value is `global`.| Optional          |
| order         | The values can be distributed in a set order. The value of order can be:<ul><li>`sequential`</li><li>`random`</li><li>`any`</li></ul></br>The default value is `any`.| Optional          |
| out_of_value  | When no values are left, several policies can be applied. The value of out_of_value can be:<ul><li>`cycle`</li><li>`stop_test`</li><li>`no_value_code`</li></ul></br>The default value is `cycle`.| Optional          |

**Example:**

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