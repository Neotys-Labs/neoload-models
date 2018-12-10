# Populations

| Name        | Description          |
| ------------| ---------------------|
| populations | List of Populations. |

## Population
A Population is a group of virtual users.

**Available settings are:**

| Name        | Description                                                  | Required/Optional |
| ----------- | ------------------------------------------------------------ | ----------------- |
| name        | The name of the Population.                                  | Required          |
| description | The description of the Population.                           | Optional          |
| user_paths  | The list of existing User Paths                              | Required          |

**Example:**

Define a population using one user path.

```yaml
populations:
- name: population1
  user_paths:
  - name: user_path_1