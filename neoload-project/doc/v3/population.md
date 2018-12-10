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

Define three scenarios: a test with a *constant* load, a test with a *ramp-up* load or a test with *peaks* load with the same population.

```yaml
populations:
- name: # required: string
  description: # optional: string
  user_paths:
  - name: # required: string
    distribution: # optional: string (1 decimal place) - example: 10% or 9.9%

```

## basic-authentication
| Name        | Description                                                     | Required/Optional |
| ----------- | --------------------------------------------------------------- | ----------------- |
| login       | The login of the user account used to authenticate on the server.                                         | Required          |
| password    | The password of the user account used to authenticate on the server.                                                   | Required          |
| realm       | The realm.                                                      | Optional          |

#### Example
Defining a `basic-authentication` for a server.
```yaml
basic-authentication:
  login: admin
  password: secret
  realm: realm-value
```