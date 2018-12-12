# Server
The server settings are centralized, which means the target server for the test can be quickly changed. It is possible, for example, to quickly switch from the development to the pre-production server.

#### Available settings

| Name        | Description                                                  | Accept variable   | Required/Optional | 
| ----------- | ------------------------------------------------------------ | ----------------- | ----------------- |
| name        | The name of the server                                      | No                | Required          |
| host        | The host of the server                                      | Yes               | Required          |
| scheme      | The scheme of the server. The available values are "http" and "https". The default value is "http".  | No | Optional          |
| port        | The port of the server. The default value is "80" (for http scheme) or "443" (for https scheme).              | Yes    | Optional          |
| [basic_authentication](#basic_authentication) | The Basic Authentication used to authenticate on the server.                  | No | Optional          |
| [ntlm_authentication](#ntlm_authentication) | The NTLM Authentication used to authenticate on the server.                     | No | Optional          |
| [negotiate_authentication](#negotiate_authentication) | The Negotiate Authentication used to authenticate on the server.         | No | Optional          |

#### Example
Defining 2 servers: one with the host name only, and an https one with a basic authentication.
```yaml
servers:
- name: my-server
  host: host.intranet.company.com
- name: my-prod-server
  host: ${prod_host}
  scheme: https
  port: 443
  basic_authentication:
   login: ${prod_login}
   password: ${prod_password}
   realm: realm-value
```

## basic_authentication
| Name        | Description                                                             | Accept variable   | Required/Optional |
| ----------- | ----------------------------------------------------------------------- | ----------------- | ----------------- |
| login       | The login of the user account used to authenticate on the server       | Yes               | Required          |
| password    | The password of the user account used to authenticate on the server    | Yes               | Required          |
| realm       | The realm                                                              | No                | Optional          |

#### Example
Defining a `basic_authentication` for a server.
```yaml
basic_authentication:
  login: admin
  password: secret
  realm: realm-value
```

## ntlm_authentication
| Name        | Description                                                             | Accept variable   | Required/Optional |
| ----------- | ----------------------------------------------------------------------- | ----------------- | ----------------- |
| login       | The login of the user account used to authenticate on the server       | Yes               | Required          |
| password    | The password of the user account used to authenticate on the server    | Yes               | Required          |
| domain      | The domain                                                             | No                | Optional          |

#### Example
Defining a `ntlm_authentication` for a server.
```yaml
ntlm_authentication:
  login: admin
  password: secret
  domain: domain-value
```

## negotiate_authentication
| Name        | Description                                                             | Accept variable   | Required/Optional |
| ----------- | ----------------------------------------------------------------------- | ----------------- | ----------------- |
| login       | The login of the user account used to authenticate on the server       | Yes               | Required          |
| password    | The password of the user account used to authenticate on the server    | Yes               | Required          |
| domain | The domain                                                                  | No                | Optional          |

#### Example
Defining a `negotiate_authentication` for a server.
```yaml
negotiate_authentication:
  login: admin
  password: secret
```
