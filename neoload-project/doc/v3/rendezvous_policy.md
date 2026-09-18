# Rendezvous policy
The Rendezvous policy to be applied on the already defined Rendezvous in the User Path.

> **Note:** A `rendezvous_policy` entry must reference an existing Rendezvous defined in a User Path. Declaring a policy whose `name` does not match any Rendezvous, or declaring two policies with the same `name` in the same scenario, will cause the project import to fail with an error.

#### Available settings

| Name                                                   | Description                                                                                                               | Accept variable | Required | Since |
|:------------------------------------------------------ |:--------------------------------------------------------------------------------------------------------------------------|:---------------:|:--------:|:-----:|
| name                                                   | The name of the Rendezvous.                                                                                               | -               | &#x2713; |  7.6  |
| when                                                   | When to release the Rendezvous. Possible values are: "manual", percentage or positive number. </br>The default value is 100%.            | -               | -               |  7.6  |
| timeout                                                | The timeout between Virtual Users. Timeout duration is expressed in hours (h), minutes (m), seconds (s). </br>The default value is 300s. | -          | -        |  7.6  |

#### Example

Defining the Rendezvous policy for the Scenario:
If "When" is "manual", use a Javascript action to free waiting Virtual Users. Otherwise, release when percentage or positive number of Virtual Users arrive at the Rendezvous point.
```yaml
rendezvous_policies:
  - name: rendezvous1
    when: manual
    timeout: 30s
  - name: rendezvous2
    when: 50%
    timeout: 2m
  - name: rendezvous3
    when: 10
    timeout: 1h
```

#### Complete example

The following shows the full picture: a User Path declares a [Rendezvous](rendezvous.md) action, and the Scenario references it via a `rendezvous_policy`.

```yaml
user_paths:
  - name: MyUserPath
    actions:
      steps:
        - request:
            url: /login
        - rendezvous:
            name: BeforeCheckout
        - request:
            url: /checkout

scenarios:
  - name: MyScenario
    populations:
      - name: MyPopulation
        constant_load:
          users: 50
    rendezvous_policies:
      - name: BeforeCheckout
        when: 50%
        timeout: 30s
```

All 50 Virtual Users will wait at `BeforeCheckout` until at least 50 % of them have arrived (or 30 s have elapsed since the last arrival), then all proceed to `/checkout` simultaneously.
