# Project

## Overview
... 

## Definition

Definition has several top-level keys:

 Name        | Description                                                  | Required/Optional |
| ----------- | ----------------------------------------------------------- | ----------------- |
| [servers](server.md)        | The servers list.                      | Optional          |
| [variables](variables.md)        | The variables list.                      | Optional          |
| [user_paths](user-paths.md)        | The virtual user paths list.                      | Optional          |
| [populations](population.md)        | The populations list.                      | Optional          |
| [scenarios](scenario.md)        | The scenario list.                      | Optional          |

## Example
Here is an example of a NeoLoad project.

```yaml
name: MyProject
servers:
- name: serverName
  host: mypc.intranet.company.com
variables:
- file:
    name: products_file
    path: data/list_of_products.csv
user_paths:
- name: MyUserPath
  actions:
    do:
    - transaction:
        name: MyTransaction
        do:
        - request:
            url: http://www.company.com/select?name:product
        - think_time: 1s
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