# Population
A Population is a group of virtual users.

#### Available settings are:

| Name        | Description                                                  | Accept variable  | Required/Optional |
| ----------- | ------------------------------------------------------------ | ---------------- | ----------------- |
| name        | The name of the Population                                   | No               | Required          |
| description | The description of the Population                            | No               | Optional          |
| user_paths  | The list of existing User Paths                              | No               | Required          |

#### Example

Define a population using one user path.

```yaml
populations:
- name: population1
  user_paths:
  - name: user_path_1