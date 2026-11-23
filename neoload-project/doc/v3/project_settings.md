# Project settings
Some preferences of the project can be overwritten. Each of these properties can be overwritten one by one,
the other properties will keep the value defined from NeoLoad.

The settings are nested maps (`qtest:` / `enabled: true`), which requires `schemaVersion: "3.2"` (2027.1).

Before schema 3.2 (2027.1), the Dynatrace and qTest settings were written as dotted keys (`qtest.enabled: true`).
They are still read, but deprecated: see the [3.1 documentation](https://github.com/Neotys-Labs/neoload-models/blob/stable/v3.1/neoload-project/doc/v3/project_settings.md).

#### Available settings

| Name                                    | Description                                                                                       | Accept variable | Required | Since |
|:--------------------------------------- |:------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| [dynatrace](#dynatrace)                 | The Dynatrace integration                                                                         | -               | -        | 7.5   |
| [qtest](#qtest)                         | The qTest integration                                                                             | -               | -        | 7.10  |
| [runtime](#runtime)                     | The Runtime parameters of the project                                                             | -               | -        | 2027.1 |

#### dynatrace

| Name                                    | Description                                                                                       | Accept variable | Required | Since |
|:--------------------------------------- |:------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| enabled                                 | Enable Dynatrace integration (a monitor must be defined)                                          | -               | -        | 7.5   |
| url                                     | The URL of the Dynatrace server                                                                   | -               | -        | 7.5   |
| token                                   | The API token of the Dynatrace server as clear text or encrypted by Neoload                       | -               | -        | 7.5   |

#### qtest

| Name                                    | Description                                                                                       | Accept variable | Required | Since |
|:--------------------------------------- |:------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| enabled                                 | Enable qTest integration                                                                          | -               | -        | 7.10  |
| url                                     | The URL of the qTest server                                                                       | -               | -        | 7.10  |
| token                                   | The API token of the qTest server as clear text or encrypted by Neoload                           | -               | -        | 7.10  |
| username                                | The username to log into qTest                                                                    | -               | -        | 7.10  |
| password                                | The password to log into qTest as clear text or encrypted by Neoload                              | -               | -        | 7.10  |
| auth_method                             | The way to authenticate to the qTest server. Allowed values are "TOKEN" and "LOGIN_PWD"           | -               | -        | 7.10  |
| mapping_mode                            | How the results are mapped to qTest test cases. Allowed values are "PER_SCENARIO" and "PER_USER_PATH" | -               | -        | 2026.3 |
| project_id                              | The qTest unique identifier of the qTest project in which the result will be transferred          | -               | -        | 7.10  |
| cycle_id                                | The qTest unique identifier of the test cycle in which the result will be transferred. This cannot be a release identifier.  | -               | -        | 7.10  |
| create_defects                          | Enable automatic creation of Defects on failed and warning SLAs                                   | -               | -        | 7.11  |

#### runtime

| Name                                    | Description                                                                                       | Accept variable | Required | Since |
|:--------------------------------------- |:------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| [responses](#responses)                 | The response settings of the Runtime parameters                                                   | -               | -        | 2027.1 |

#### responses

| Name                                    | Description                                                                                       | Accept variable | Required | Since |
|:--------------------------------------- |:------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| [content_assertions](#content_assertions) | The responses and actions the content assertions of a User Path, Container or Transaction are applied to | -               | -        | 2027.1 |

#### content_assertions

The [content assertions](content_assertion.md) of a User Path, Container or Transaction are applied to all its requests
whose response matches these settings, the ones of the Runtime parameters of the project settings in the GUI.

| Name                                    | Description                                                                                       | Accept variable | Required | Since |
|:--------------------------------------- |:------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| content_types                           | Comma-separated list of the response content types the content assertions are applied to. Default: `text/html,text/xhtml` | -               | -        | 2027.1 |
| apply_on_custom_actions                 | Also apply the content assertions to custom actions. Default: `false`                             | -               | -        | 2027.1 |

#### Example
- Enable Dynatrace integration monitoring. It is possible, for example, to quickly activate Dynatrace monitoring for the pre-production server.
- Enable qTest integration. The results will be transferred to qTest at the end of the Neoload test.
```yaml
project_settings:
  dynatrace:
    enabled: true
    url: https://abc12345.live.dynatrace.com
    token: clearTokenOrTokenEncryptedByNeoload
  qtest:
    enabled: true
    url: https://myCompany.qtestnet.com
    token: clearTokenOrTokenEncryptedByNeoload
    auth_method: TOKEN
    project_id: 101762
    cycle_id: 3761630
    create_defects: false
```

- Apply the content assertions to JSON responses and to custom actions as well.
```yaml
project_settings:
  runtime:
    responses:
      content_assertions:
        content_types: text/html,text/xhtml,application/json
        apply_on_custom_actions: true
```

#### Other Example
This configuration will override the Dynatrace token but keep the URL and enabled defined in the project from NeoLoad.
```yaml
project_settings:
  dynatrace:
    token: xxxxxxxxxx
```
