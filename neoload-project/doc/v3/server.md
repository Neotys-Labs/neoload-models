# Server
The server settings are centralized, which means the target server for the test can be quickly changed. It is possible, for example, to quickly switch from the development to the pre-production server.

#### Available settings are:

| Name        | Description                                                  | Accept variable   | Required/Optional | 
| ----------- | ------------------------------------------------------------ | ----------------- | ----------------- |
| name        | The name of the server.                                      |                   | Required          |
| host        | The host of the server.                                      | Yes               | Required          |
| scheme      | The scheme of the server. The available values are "http" and "https". The default value is "http".  | | Optional          |
| port        | The port of the server. The default value is "80" (for http scheme) or "443" (for https scheme).              |   Yes    | Optional          |
| [basic-authentication](#basic-authentication) | The Basic Authentication used to authenticate on the server.                  | | Optional          |
| [ntlm-authentication](#ntlm-authentication) | The NTLM Authentication used to authenticate on the server.                     | | Optional          |
| [negotiate-authentication](#negotiate-authentication) | The Negotiate Authentication used to authenticate on the server.         | | Optional          |

#### Example
Defining 2 servers: one with the host-name only, and an https one with a basic authentication.
```yaml
servers:
- name: my-server
  host: host.intranet.company.com
- name: my-https-server
  host: host-prod.company.com
  scheme: https
  port: 443
  basic-authentication:
   login: admin
   password: secret
   realm: realm-value
```

## basic-authentication
| Name        | Description                                                             | Accept variable   | Required/Optional |
| ----------- | ----------------------------------------------------------------------- | ----------------- | ----------------- |
| login       | The login of the user account used to authenticate on the server.       | Yes               |  Required         |
| password    | The password of the user account used to authenticate on the server.    | Yes               | Required          |
| realm       | The realm.                                                              |                   | Optional          |

#### Example
Defining a `basic-authentication` for a server.
```yaml
basic-authentication:
  login: admin
  password: secret
  realm: realm-value
```

## ntlm-authentication
| Name        | Description                                                             | Accept variable   | Required/Optional |
| ----------- | ----------------------------------------------------------------------- | ----------------- | ----------------- |
| login       | The login of the user account used to authenticate on the server.       | Yes               | Required          |
| password    | The password of the user account used to authenticate on the server.    | Yes               | Required          |
| domain      | The domain.                                                             |                   | Optional          |

#### Example
Defining a `ntlm-authentication` for a server.
```yaml
ntlm-authentication:
  login: admin
  password: secret
  domain: domain-value
```

## negotiate-authentication
| Name        | Description                                                             | Accept variable   | Required/Optional |
| ----------- | ----------------------------------------------------------------------- | ----------------- | ----------------- |
| login       | The login of the user account used to authenticate on the server.       | Yes               | Required          |
| password    | The password of the user account used to authenticate on the server.    | Yes               | Required          |
| domain | The domain.                                                                  |                   | Optional          |

#### Example
Defining a `negotiate-authentication` for a server.
```yaml
negotiate-authentication:
  login: admin
  password: secret
```