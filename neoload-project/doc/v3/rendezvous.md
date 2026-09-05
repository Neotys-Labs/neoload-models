# Rendezvous

A Rendezvous point synchronizes Virtual Users at a common meeting point so they carry out tasks simultaneously, creating an intense load on the server at a specific point in the application.

When a Virtual User reaches a rendezvous step it waits until the release condition defined in the [Rendezvous Policy](rendezvous_policy.md) is met, then all lined-up users continue at the same time. The release condition and an optional timeout are configured at the scenario level, not in the action itself.

A rendezvous point is identified by its **name**. Multiple rendezvous actions that share the same name all wait at the same rendezvous point.

#### Available settings

| Name        | Description                                                     | Accept variable | Required | Since |
|:----------- |:--------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name        | Name of the rendezvous point. Defaults to `rendezvous`.         | &#x2718;        |          |       |
| description | An optional description, for information purposes only.         | &#x2718;        |          |       |

> **Note:** Several rendezvous actions may use the same name. They all refer to the single rendezvous point identified by that name, and lined-up users are released in arrival order.

#### Examples

Minimal form — uses the default rendezvous name `rendezvous`:
```yaml
steps:
- rendezvous
```

With an explicit name and description:
```yaml
steps:
- rendezvous:
    name: MyRendezVous
    description: Synchronize users before hitting the account-balance endpoint
```

#### Configuring the release behavior

The release policy (condition and timeout) is defined per scenario using [Rendezvous Policy](rendezvous_policy.md). Three release conditions are available:

| `when` value | Meaning |
|:------------ |:------- |
| `XX%`        | Release when XX % of the currently executing eligible users have arrived. |
| `XX`         | Release when exactly XX users have arrived. |
| `manual`     | Hold until explicitly released via a JavaScript `RendezvousManager` action. |

The `timeout` sets the maximum wait time between two consecutive arrivals. When it expires, all users lined up on that Load Generator are released regardless of the condition.

```yaml
rendezvous_policies:
  - name: MyRendezVous
    when: 50%
    timeout: 30s
```

See [Rendezvous Policy](rendezvous_policy.md) for the full reference and more examples.
