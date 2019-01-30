# Population
A Population is a group of Virtual Users.

#### Available settings are:

| Name                                  | Description                          | Accept variable  | Required | Since |
|:------------------------------------- |:------------------------------------ |:----------------:|:--------:|:-----:|
| name                                  | The name of the Population           | -                | &#x2713; |       |
| description                           | The description of the Population    | -                | -        |       |
| [user_paths](#user-path-distribution) | The list of User Path distributions  | -                | &#x2713; |       |

#### Example

Defining a population with 1 User Path.

```yaml
populations:
- name: population1
  user_paths:
  - name: user_path_1
```
  
## User Path distribution

| Name         | Description                                                  | Accept variable  | Required | Since |
|:------------ |:------------------------------------------------------------ |:----------------:|:--------:|:-----:|
| name         | The name of the existing User Path                           | -                | &#x2713; |       |
| distribution | The percentage of the User Path in Population. The value is a percentage and may be set within precision of 0.1%.<br>If the distribution is not present, its value will be proportionally calculated. | - | - |  |

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
