# Project

## Overview

The Project section allows specifying the NeoLoad project definition.
The Project definition can be declared with servers, variables, User Paths, Populations and Scenarios composing the project.

## Definition

Definition has several top-level keys:

| Name                           | Description                                   | Accept variable | Required | Since |
|:------------------------------ |:--------------------------------------------- |:---------------:|:--------:|:-----:|
| name                           | The root key defining the name of the project | -               | &#x2713; |       |
| [sla_profiles](sla-profile.md) | The definition of SLA profiles                | -               | -        | 6.9   |
| [variables](variables.md)      | The definition of variables                   | -               | -        |       |
| [servers](server.md)           | The definition of servers                     | -               | -        |       |
| [user_paths](user-paths.md)    | The definition of virtual User Paths          | -               | -        |       |
| [populations](population.md)   | The definition of Populations                 | -               | -        |       |
| [scenarios](scenario.md)       | The definition of Scenarios                   | -               | -        |       |

#### Example
Below is an example of a NeoLoad project:

```yaml
name: MyProject
sla_profiles:
- name: MySlaProfile
  thresholds:
  - avg-request-resp-time warn >= 200 ms fail >= 500 ms on test
  - perc-transaction-resp-time (p90) warn >= 1 s fail >= 2 s on test
  - error-rate warn >= 5 % on test
  - error-rate warn >= 1 % fail >= 2 % on interval
variables:
- file:
    name: products
    path: data/list_of_products.csv
servers:
- name: mypc
  host: mypc.intranet.company.com
user_paths:
- name: MyUserPath
  actions:
    steps:
    - request:
        url: http://www.company.com/select?name=${products.col_0}
    - think_time: 1s
    - request:
        server: mypc
        url: /valide
populations:
- name: MyPopulation
  user_paths:
  - name: MyUserPath
    distribution: 100%
scenarios:
- name: MyScenario
  sla_profile: MySlaProfile
  populations:
  - name: MyPopulation
    constant_load:
      users: 500
```
