# Rendezvous Policy
The Rendezvous policy to be applied on the already defined Rendezvous in the User Path.

#### Available settings

| Name                                                   | Description                                   | Accept variable | Required | Since |
|:------------------------------------------------------ |:--------------------------------------------- |:---------------:|:--------:|:-----:|
| name                                                   | The name of the rendezvous.            | -               | -        |  7.6  |
| when                                                   | When to release the rendezvous. Possible values are: manual, percentage or positive number.              | -               | -        |  7.6  |
| timeout                                                | The timeout between virtual users. Timeout duration is expressed in hours, minutes, seconds. | -               | -        |  7.6  |

#### Example

Defining the Rendezvous policy for the Scenario:

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
