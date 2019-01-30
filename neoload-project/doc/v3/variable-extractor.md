# Extractor
Extractors allow chaining several API calls where the argument of a call can be extracted from a previous call.

#### Available settings

| Name                   | Description                                 | Accept variable | Required | Since |
|:---------------------- | :-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-------:|:-------------:|:-----:|
| name                   | The name of the extractor                                                                                                                                                                                                                                                                                                                                                          | -      | &#x2713;      |       |
| from                   | The response part where to extract value. The available values are "header", "body" and "both".</br>The default value is "body".                                                                                                                                                                                                                                                       | -      | -      |       |
| xpath                  | The XPath expression where to extract value. Only available when "from" value is "body".                                                                                                                                                                                                                                                                                           | -      | -      |       |
| jsonpath               | The JSONPath expression where to extract value. Only available when "from" value is "body".                                                                                                                                                                                                                                                                                        | -      | -      |       |
| regexp                 | The regular expression applies on "from" value. If "Body" and "xpath" or "jsonpath" is specified, then the extraction is done on the result of the xpath or jsonpath.</br>The default value is "(.*)".                                                                                                                                                                                 | -      | -      |       |
| match_number           | If multiple values have matched "regexp", the match number defines which match to use. Value "-1" is to extract all occurrences, "0" to randomly extract one occurence and "N" to extract the Nth occurrence.</br>The default value is "1".                                                                                                                                            | -      | -      |       |
| template               | The template used to construct the value. This is an arbitrary string containing special elements that refer to groups within the regular expression (a group is a dynamic portion of the regular expression set within brackets): $0$ refers to the entire text matching the expression, $1$ refers to group 1, $2$ refers to group 2, and so on.</br>The default value is $1$.       | -      | -      |       |
| decode                 | The decoder to apply on the value. The available values are "html" and "url".                                                                                                                                                                                                                                                                                                      | -      | -      |       |
| extract_once           | This option allows considering only the first value extracted. The available values are "true" and "false".</br>The default value is "false".                                                                                                                                                                                                                                          | -      | -      |       |
| default                | The value to return when not extracted.</br>The default value is "<NOT FOUND>".                                                                                                                                                                                                                                                                                                        | -      | -      |         | 
| throw_assertion_error  | Raises an assertion error when no match found.</br>The default value is "true".                                                                                                                                                                                                                                                                                                        | -      | -      |       |


#### Example

Defining 2 extractors: one  matching any number in header, and another one matching the first occurence of regular expression on JSONPath.

```yaml
extractors:
- name: any-number-on-body
  regexp: ([0-9]*)
- name: first-match-on-jsonpath
  jsonpath: $.features[0].type
  regexp: Fea(.*)
  match_number: 1
  decode: url
  extract_once: true
```