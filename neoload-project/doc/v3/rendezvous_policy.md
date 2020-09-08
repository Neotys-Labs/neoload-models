# Rendezvous Policy
The rendezvous policy to be applied on the already defined rendezvous in user path.

#### Available settings

| Name                                                   | Description                                   | Accept variable | Required | Since |
|:------------------------------------------------------ |:--------------------------------------------- |:---------------:|:--------:|:-----:|
| name                                                   | The name of the rendezvous.            | -               | -        |  7.6  |
| [when](when.md)                                        | When to release the rendezvous.               | -               | -        |  7.6  |
| timeout                                                | The timeout between virtual users.            | -               | -        |  7.6  |

#### Example

Defining the configuration for the APM Dynatrace:

```yaml
rendezvous_policies:
  - name: rendezvous
    when: manual
    timeout: 30s
```
