# Project settings
Some preferences of the project can be overwritten.

#### Available settings

| Name                                    | Description                                                     | Accept variable | Required | Since |
|:--------------------------------------- |:--------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| dynatrace.enabled                       | Enable Dynatrace integration (a monitor must be defined)        | -               | -        | 7.5   |
| dynatrace.url                           | The url of dynatrace server                                     | -               | -        | 7.5   |
| dynatrace.token                         | The Api Token of dynatrace server as clear text or encrypted by Neoload                           | -               | -        | 7.5   |
| qtest.enabled                           | Enable qTest integration                                        | -               | -        | 7.10   |
| qtest.url                               | The url of qTest server                                         | -               | -        | 7.10   |
| qtest.token                             | The Api Token of qTest as clear text or encrypted by Neoload    | -               | -        | 7.10   |
| qtest.username                          | The username to login to qTest                                  | -               | -        | 7.10   |
| qtest.password                          | The password to login to qTest as clear text or encrypted by Neoload                              | -               | -        | 7.10   |
| qtest.auth_method                       | The way to authenticate to qTest server. Allowed values are "TOKEN" and "LOGIN_PWD"               | -               | -        | 7.10   |
| qtest.project_id                        | The qTest unique identifier of qTest project in which the result will be transferred              | -               | -        | 7.10   |
| qtest.cycle_id                          | The qTest unique identifier of the release or the cycle in which the result will be transferred   | -               | -        | 7.10   |

#### Example
- Enable Dynatrace integration monitoring. It is possible, for example, to quickly activate Dynatrace monitoring for the pre-production server.
- Enable qTest integration. The results will be transferred to qTest at the end of Neoload test.
```yaml
project_settings:
  dynatrace.enabled: true
  dynatrace.url: https://abc12345.live.dynatrace.com
  dynatrace.token: clearTokenOrTokenEncryptedByNeoload

  qtest.enabled: true
  qtest.url: https://myCompany.qtestnet.com
  qtest.token: clearTokenOrTokenEncryptedByNeoload
  qtest.auth_method: TOKEN
  qtest.project_id: 101762
  qtest.cycle_id: 3761630
```
