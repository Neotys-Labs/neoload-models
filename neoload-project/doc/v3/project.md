# Project

## Overview

The Project section allows specifying the NeoLoad project definition.
The Project definition can be declared with servers, variables, User Paths, Populations and Scenarios composing the project.

## Definition

Definition has several top-level keys:

 Name                          | Description                                   | Required/Optional |
| ---------------------------- | ----------------------------------------------| ----------------- |
| name                         | The root key defining the name of the project | Required          |
| description                  | The description of the project                | Optional          |
| [servers](server.md)         | The servers list                              | Optional          |
| [variables](variables.md)    | The variables list                            | Optional          |
| [user_paths](user-paths.md)  | The virtual User Paths list                   | Optional          |
| [populations](population.md) | The Populations list                          | Optional          |
| [scenarios](scenario.md)     | The Scenario list                             | Optional          |

#### Example
Here is an example of a NeoLoad project.

```yaml
name: MyProject
servers:
- name: mypc
  host: mypc.intranet.company.com
variables:
- file:
    name: products
    path: data/list_of_products.csv
user_paths:
- name: MyUserPath
  actions:
    steps:
    - transaction:
        name: MyTransaction
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
  populations:
  - name: MyPopulation
    constant_load:
      users: 500
```