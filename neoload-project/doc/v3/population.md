# Population
A Population is a group of virtual users.

#### Available settings are:

| Name        | Description                                                  | Accept variable  | Required/Optional |
| ----------- | ------------------------------------------------------------ | ---------------- | ----------------- |
| name        | The name of the Population                                   | No               | Required          |
| description | The description of the Population                            | No               | Optional          |
| [user_paths](#user-path-distribution)  | The list of User Path distributions                              | No               | Required          |

#### Example

Defining a population with 1 User Path.

```yaml
populations:
- name: population1
  user_paths:
  - name: user_path_1
```
  
## User Path distribution

| Name        | Description                                                  | Accept variable  | Required/Optional |
| ----------- | ------------------------------------------------------------ | ---------------- | ----------------- |
| name        | The name of the existing User Path                           | No               | Required          |
| distribution| The percentage of the User Path in Population. The value is a percentage and may be set within precision of 0.1%.<br>If the distribution is not present, its value will be proportionally calculated.              | No               | Optional          |

#### Example 1

Defining a population with 2 User Paths: `user_path_1` takes 30.3% and `user_path_2` takes 66.7%.

```yaml
populations:
- name: population1
  user_paths:
  - name: user_path_1
    distribution: 33.3%
  - name: user_path_2
    distribution: 66.7%
```

#### Example 2

Defining a population with 2 User Paths: `user_path_1` takes 50% and `user_path_2` takes 50%.

```yaml
populations:
- name: population1
  user_paths:
  - name: user_path_1
  - name: user_path_2
```
