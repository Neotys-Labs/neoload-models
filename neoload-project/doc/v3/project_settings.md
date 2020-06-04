# Project settings
Some preferences of the project can be overwritten.

#### Available settings

| Name                                    | Description                                                     | Accept variable | Required | Since |
|:--------------------------------------- |:--------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| dynatrace.enabled                       | Enable Dynatrace integration (a monitor must be defined)        | -               | -        | 7.5   |
| dynatrace.url                           | The url of dynatrace server                                     | -               | -        | 7.5   |
| dynatrace.token                         | The Api Token of dynatrace server                               | -               | -        | 7.5   |

#### Example
Enable Dynatrace integration monitoring. It is possible, for example, to quickly activate Dynatrace monitoring for the pre-production server.
```yaml
project_settings:
  dynatrace.enabled: true
  dynatrace.url: https://abc12345.live.dynatrace.com
  dynatrace.token: clearTokenOrTokenEncryptedByNeoload
```
