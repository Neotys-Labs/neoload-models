# Populations

| Name        | Description          |
| ------------| ---------------------|
| populations | List of populations. |

## Population
A population is a group of virtual users.

**Available settings are:**

| Name        | Description                                                  | Required/Optional |
| ----------- | ------------------------------------------------------------ | ----------------- |
| name        | The name of the population.                                  | Required          |
| description | The description of the population.                           | Optional          |
| user_paths  | The list of existing user paths                              | Required          |

**Example:**

Define a population using one user path.

```yaml
populations:
- name: population1
  user_paths:
  - name: user_path_1