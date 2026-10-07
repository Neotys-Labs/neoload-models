# Assertion on response size

A size assertion checks the length of the response content, in bytes. It mirrors the "Content length" validation panel of the NeoLoad designer: either an exact size, or a range built from a lower bound and/or an upper bound.

Unlike [assertions](assertion.md), which is a list of content checks, `size_assertion` is a single object: a request or a custom action carries at most one size assertion.

> Validation cannot be applied to failed requests (HTTP errors, network errors, and so on).

#### Available settings

| Name         | Description                                                       | Accept variable | Required | Since  |
|:------------ |:----------------------------------------------------------------- |:---------------:|:--------:|:------:|
| equals       | The response size must be exactly this number of bytes            | -               | -        | 2026.3 |
| greater_than | The response size must be strictly greater than this size, in bytes | -               | -        | 2026.3 |
| less_than    | The response size must be strictly lower than this size, in bytes | -               | -        | 2026.3 |

All settings are optional but the following rules apply:
* At least one of `equals`, `greater_than` or `less_than` must be specified.
* `equals` cannot be combined with `greater_than` or `less_than`.
* `greater_than` and `less_than` can be combined to define a range.
* Sizes are positive integers, expressed in bytes.

#### Example 1

Defining a validation to check that the response is exactly 1024 bytes long.

```yaml
size_assertion:
  equals: 1024
```

#### Example 2

Defining a validation to check that the response size is within a range.

```yaml
size_assertion:
  greater_than: 1024
  less_than: 2048
```

#### Example 3

Defining a validation to check that the response is smaller than 1 MB, on a request.

```yaml
request:
  url: http://petstore.swagger.io:80/v2/pet/findByStatus?status=available
  size_assertion:
    less_than: 1048576
```
