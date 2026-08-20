# Project

## Overview

The Project section allows specifying the NeoLoad project definition.
The Project definition can be declared with servers, variables, User Paths, Populations and Scenarios composing the project.

The optional `schemaVersion` attribute at the root of an as-code file declares the schema contract the file is written for. It is available since NeoLoad **2026.3**. When the attribute is omitted, the file is interpreted as **3.0**. Product validation checks that the declared version is supported by the bundled `neoload-models` build. Cross-product compatibility is described in `schemas/compatibility.json` at the repository root.

The JSON Schema for version 3.0 is published as `schemas/v3.0/as-code.schema.json`. The runtime copy of the latest schema also lives in `neoload-project/src/main/resources/as-code.latest.schema.json`.

## Definition

Definition has several top-level keys:

| Name                           | Description                                    | Accept variable | Required | Since |
|:------------------------------ |:---------------------------------------------- |:---------------:|:--------:|:-----:|
| schemaVersion                  | Schema contract version this file is written for. Defaults to `3.0` when absent. | - | - | 2026.3 |
| name                           | The root key defining the name of the project  | -               | &#x2713; |       |
| [includes](include.md)         | The definition of as-code files to be included | -               | -        | 6.10  |
| [sla_profiles](sla-profile.md) | The definition of SLA profiles                 | -               | -        | 6.9   |
| [variables](variables.md)      | The definition of variables                    | -               | -        |       |
| [servers](server.md)           | The definition of servers                      | -               | -        |       |
| [user_paths](user-paths.md)    | The definition of virtual User Paths           | -               | -        |       |
| [populations](population.md)   | The definition of Populations                  | -               | -        |       |
| [scenarios](scenario.md)       | The definition of Scenarios                    | -               | -        |       |
| [project_settings](project_settings.md) | The definition of project preferences | -               | -        |       |

#### Example
Below is an example of a NeoLoad project:

```yaml
schemaVersion: "3.0"
name: MyProject
sla_profiles:
- name: MySlaProfile
  thresholds:
  - avg-request-resp-time warn >= 200ms fail >= 500ms per test
  - perc-transaction-resp-time (p90) warn >= 1s fail >= 2s per test
  - error-rate warn >= 2% fail >= 5% per test
  - error-rate warn >= 5% per interval
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
    sla_profile: MySlaProfile
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
