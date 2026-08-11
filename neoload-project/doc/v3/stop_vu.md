# Stop Virtual User

The Stop Virtual User action stops the current virtual user. By default, a new virtual user is started to replace the one that has been stopped, so that the number of running virtual users remains stable. Set `start_new_vu` to `false` to stop the current virtual user without starting a replacement.

Stop Virtual User is supported in NeoLoad from version 2026.3 onwards.

#### Available settings
| Name         | Description                                                                                    | Accept variable | Required | Since  |
|:------------ |:---------------------------------------------------------------------------------------------- |:---------------:|:--------:|:------:|
| start_new_vu | Whether a new virtual user is started to replace the stopped one. The default value is `true`. | -               | -        | 2026.3 |

#### Example
Stop the current virtual user and start a new one to replace it (default behavior).
```yaml
  actions:
    steps:
    - stop_vu
```

Stop the current virtual user without starting a replacement.
```yaml
  actions:
    steps:
    - stop_vu:
        start_new_vu: false
```
