# Project settings
Some preferences of the project can be overwritten.

#### Available settings

| Name                                    | Description                                                                                       | Accept variable | Required | Since |
|:--------------------------------------- |:------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| dynatrace.enabled                       | Enable Dynatrace integration (a monitor must be defined)                                          | -               | -        | 7.5   |
| dynatrace.url                           | The URL of the Dynatrace server                                                                   | -               | -        | 7.5   |
| dynatrace.token                         | The API token of the Dynatrace server as clear text or encrypted by Neoload                       | -               | -        | 7.5   |
| qtest.enabled                           | Enable qTest integration                                                                          | -               | -        | 7.10   |
| qtest.url                               | The URL of the qTest server                                                                       | -               | -        | 7.10   |
| qtest.token                             | The API token of the qTest server as clear text or encrypted by Neoload                           | -               | -        | 7.10   |
| qtest.username                          | The username to log into qTest                                                                    | -               | -        | 7.10   |
| qtest.password                          | The password to log into qTest as clear text or encrypted by Neoload                              | -               | -        | 7.10   |
| qtest.auth_method                       | The way to authenticate to the qTest server. Allowed values are "TOKEN" and "LOGIN_PWD"           | -               | -        | 7.10   |
| qtest.project_id                        | The qTest unique identifier of the qTest project in which the result will be transferred          | -               | -        | 7.10   |
| qtest.cycle_id                          | The qTest unique identifier of the test cycle in which the result will be transferred. This cannot be a release identifier.  | -               | -        | 7.10   |
| qtest.create_defects                    | Enable automatic creation of Defects on failed and warning SLAs                                   | -               | -        | 7.11   |

#### Example
- Enable Dynatrace integration monitoring. It is possible, for example, to quickly activate Dynatrace monitoring for the pre-production server.
- Enable qTest integration. The results will be transferred to qTest at the end of the Neoload test.
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
  qtest.create_defects: false
```
