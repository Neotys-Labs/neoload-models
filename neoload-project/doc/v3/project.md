# Project

## Overview
... 

## Definition

Definition has several top-level keys:
             
+ [Scenarios](scenario.md)
+ [Populations](population.md)
+ [Servers](server.md)
+ [Variables](variable.md)
+ [Virtual User Paths](user-path.md)
  + [Transactions](transaction.md)
  + [Requests](request.md)
  + [Delays](delay.md)
  + [ThinkTimes](delay.md)
  + [Variable Extractors](variable-extractor.md)

## About Overriding

When the YAML file is loaded in addition of a classic nlp Project, the legacy Project can already contain data/objects.
For the following objects, the Object is added to the Project while keeping the loaded Objects.

If the same name is used in YAML, override the existing Object with the YAML version: Scenarios, Populations, Servers, Variables, VUPath

The following objects make sense only in the context of a VUPath, so there's no override policy at this level: Transactions, requests, Extractors, Delay, Thinktime

You can find below an example of how to override a NeoLoad project when launching a test in command line using the `-project` parameter.

Please read the full [command line documentation](https://www.neotys.com/documents/doc/neoload/latest/en/html/#643.htm).


## Example
Here is an example of a NeoLoad project that contains:
+ 2 servers: one http server, one https server
+ 2 variables: one constant variable, one file variable
+ 2 user paths containing some http requests and delays
+ 1 population
+ 1 scenario

```yaml
name: MyProject
servers:
- name: serverName
  host: mypc.intranet.neotys.com
  port: 443
  scheme: https
  basic_authentication:
    login: neotysuser
    password: admin@admin
    realm: realm-value
- name: serverName2
  host: mypc2.intranet.neotys.com
  port: 81
  scheme: http
variables:
- constant:
    name: constant_variable
    value: 118218
- file:
    name: cities_file
    description: cities variable file description
    column_names: ["City", "Country", "Population", "Longitude", "Latitude"]
    is_first_line_column_names: false
    start_from_line: 5
    delimiter: ";"
    path: data/list_of_cities.csv
    change_policy: each_user
    scope: unique
    order: sequential
    out_of_value: stop_test
user_paths:
- name: MyUserPath11
  actions:
    description: some transactions with http requests and delays
    do:
    - transaction:
        name: MyTransaction
        do:
        - request:
            url: http://www.neotys.com/select?name:neoload
        - delay: 1s
        - request:
            url: /select?name=neoload
            server: neotys
            method: POST
            headers:
            - Content-Type: text/html; charset=utf-8
            - Accept-Encoding: gzip, compress, br
            body: |
              My Body
              line 1
              line 2
            extractors:
            - name: MyVariable1
              jsonpath: MyJsonPath
- name: MyUserPath12
  description: my user path 12 for integration test
  init:
    description: init data
    do:
    - delay: 5
  actions:
    description: some transactions with http requests and delays
    do:
    - transaction:
        name: MyTransaction
        do:
        - delay: 1s
  end:
    description: end user path 12
    do:
    - delay: 10ms
populations:
- name: MyPopulation1
  description: My population 1 with 2 user paths
  user_paths:
  - name: MyUserPath11
    distribution: 75%
  - name: MyUserPath12
    distribution: 25%
scenarios:
- name: MyScenario1
  populations:
  - name: MyPopulation11
    constant_load:
      users: 500
```