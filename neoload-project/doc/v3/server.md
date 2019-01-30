# Server
The server settings are centralized, which means the target server for the test can be quickly changed. It is possible, for example, to quickly switch from the development to the pre-production server.

#### Available settings

| Name                                                  | Description                                                                                         | Accept variable | Required | Since |
|:----------------------------------------------------- |:--------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name                                                  | The name of the server                                                                              | -               | &#x2713; |       |
| host                                                  | The host of the server                                                                              | &#x2713;        | &#x2713; |       |
| scheme                                                | The scheme of the server. The available values are "http" and "https". The default value is "http". | -               | -        |       |
| port                                                  | The port of the server. The default value is "80" (for http scheme) or "443" (for https scheme).    | &#x2713;        | -        |       |
| [basic_authentication](#basic_authentication)         | The Basic Authentication used to authenticate on the server.                                        | -               | -        |       |
| [ntlm_authentication](#ntlm_authentication)           | The NTLM Authentication used to authenticate on the server.                                         | -               | -        |       |
| [negotiate_authentication](#negotiate_authentication) | The Negotiate Authentication used to authenticate on the server.                                    | -               | -        |       |

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
| Name        | Description                                                         | Accept variable | Required | Since |
|:----------- |:------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| login       | The login of the user account used to authenticate on the server    | &#x2713;        | &#x2713; |       |
| password    | The password of the user account used to authenticate on the server | &#x2713;        | &#x2713; |       |
| realm       | The realm                                                           | -               | -        |       |

#### Example
Defining a `basic_authentication` for a server.
```yaml
basic_authentication:
  login: admin
  password: secret
  realm: realm-value
```

## ntlm_authentication
| Name        | Description                                                         | Accept variable | Required | Since |
|:----------- |:------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| login       | The login of the user account used to authenticate on the server    | &#x2713;        | &#x2713; |       |
| password    | The password of the user account used to authenticate on the server | &#x2713;        | &#x2713; |       |
| domain      | The domain                                                          | -               | -        |       |

#### Example
Defining a `ntlm_authentication` for a server.
```yaml
ntlm_authentication:
  login: admin
  password: secret
  domain: domain-value
```

## negotiate_authentication
| Name        | Description                                                         | Accept variable | Required | Since |
|:----------- |:------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| login       | The login of the user account used to authenticate on the server    | &#x2713;        | &#x2713; |       |
| password    | The password of the user account used to authenticate on the server | &#x2713;        | &#x2713; |       |
| domain | The domain                                                               | -               | -        |       |

#### Example
Defining a `negotiate_authentication` for a server.
```yaml
negotiate_authentication:
  login: admin
  password: secret
```
