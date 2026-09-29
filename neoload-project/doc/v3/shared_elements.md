# Shared elements

`shared_elements` is a top-level, project-wide list of named, reusable steps. A `shared_element`
step, usable anywhere a step is valid — in a User Path or nested inside another shared element —
resolves to one of these steps by name instead of inlining it.

Each `shared_elements` entry is an ordinary [transaction](transaction.md), [loop](loop.md),
[while](while.md) or [fork](fork.md) step; that `name` is what a `shared_element` step references.
Any other step type is rejected.

Available since NeoLoad 2026.3 (schema 3.1).

#### Available settings — `shared_elements` entry
| Name            | Description                                              | Accept variable | Required | Since |
|:-----------------|:----------------------------------------------------------|:---------------:|:--------:|:-----:|
| [transaction](transaction.md) / [loop](loop.md) / [while](while.md) / [fork](fork.md) | The shared element itself: exactly one of these four | - | &#x2713; | 2026.3 |

#### Available settings — `shared_element` step
| Name            | Description                                              | Accept variable | Required | Since |
|:-----------------|:----------------------------------------------------------|:---------------:|:--------:|:-----:|
| shared_element   | The `name` of the `shared_elements` entry to run here      | -               | &#x2713; | 2026.3 |

#### Example
Declare a `Login` transaction once and run it from a User Path and from a retry loop.
```yaml
shared_elements:
- transaction:
    name: Login
    steps:
    - request:
        url: https://www.tricentis.com/login
- loop:
    name: RetryLogin
    count: 3
    steps:
    - shared_element: Login
user_paths:
- name: MyUserPath
  actions:
    steps:
    - shared_element: RetryLogin
```

#### Rules

- `shared_elements` entry names must be unique across the project.
- A `shared_elements` entry must be a `transaction`, `loop`, `while` or `fork` step.
- A `shared_element` step must reference a name declared in `shared_elements`.
- A `shared_elements` entry cannot reference itself, directly or through a chain of other shared
  elements (reference cycles are rejected).
- Writing a project never inlines a shared element's definition at the reference site: the full
  definition appears once, under `shared_elements`.
