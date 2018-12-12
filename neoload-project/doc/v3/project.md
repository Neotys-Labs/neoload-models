# Project

## Overview

The Project section allows specifying the NeoLoad project definition.
The Project definition can be declared with servers, variables, User Paths, Populations and Scenarios composing the project.

## Definition

Definition has several top-level keys:

 Name                          | Description                                   | Accept variable    | Required/Optional |
| ---------------------------- | --------------------------------------------- | ------------------ | ----------------- |
| name                         | The root key defining the name of the project | No                 | Required          |
| [variables](variables.md)    | The variables list                            | No                 | Optional          | 
| [servers](server.md)         | The servers list                              | No                 | Optional          |
| [user_paths](user-paths.md)  | The virtual User Paths list                   | No                 | Optional          |
| [populations](population.md) | The Populations list                          | No                 | Optional          |
| [scenarios](scenario.md)     | The Scenario list                             | No                 | Optional          |

#### Example
Here is an example of a NeoLoad project.

```yaml
name: MyProject
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
  populations:
  - name: MyPopulation
    constant_load:
      users: 500
```