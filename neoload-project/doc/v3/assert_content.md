# Assertion on response content
Assertions allow checking the validity of the responses content returned by the server. Checks whether or not the server response contains a certain content. The server response is considered valid if all the content conditions are satisfied.

> Validation cannot be applied to failed requests (HTTP error, network error, and so on).

#### Available settings

| Name                   | Description                                                                                                                                                                                            | Accept variable | Required | Since |
|:---------------------- | :----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name                   | The name of the assertion.                                                                                                                                                                              | -               | -        | 7.6   |
| xpath                  | The validation check will be carried out on one of the nodes in the response body. This node is defined using XPath expression.                                                                        | &#x2713;        | -        | 7.6   |
| jsonpath               | The validation check will be carried out on one of the nodes in the response body. This node is defined using JSONPath expression.                                                                     | &#x2713;        | -        | 7.6   |
| not                    | Checks whether or not the server response contains a certain content.</br>The default value is "false".                                                                                                | -               | -        | 7.6   | 
| contains               | The validation check will be carried out on the response headers and body. If "xpath" or "jsonpath" is specified, then the validation is done on the result of the XPath or JSONPath exepression.      | &#x2713;        | -        | 7.6   |
| regexp                 | This option allows considering that the "contains" value is an regular expression.</br>The default value is "false".                                                                                   | -               | -        | 7.6   |

> :warning: Response validations on XPath or JSON nodes are resource-consuming operations. Only use these types of validation if absolutely necessary.

> :warning: Using global responses validation (from [container](container.md), [transaction](transaction.md) and [user_paths](user-paths.md) elements) uses a lot of memory so it is therefore recommended to use it sparingly and avoid applying it to large responses.

#### Example 1

Defining a validation to check if the response body contains the specified regular expression.

```yaml
assert_content:
- contains: <span class="welcome">Welcome \w+ \w+ </span>
  regexp: true
```

#### Example 2

Defining a validation to check if the response body contains the specified JSON node.

```yaml
assert_content:
- jsonpath: $.payload.success
```

#### Example 3

Defining a validation to check if the response body doesn't contain the specified text from a specified XML node.

```yaml
assert_content:
- xpath: html/body[1]/script[1]
  not: true
  contains: Error
```