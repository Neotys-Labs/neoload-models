# Server
The server settings are centralized, which means the target server for the test can be quickly changed. It is possible, for example, to quickly switch from the development to the pre-production server.

:warning: If the project already have a server with the same name from NeoLoad, the whole server will be __replaced__ by the as-code one.

#### Available settings

| Name                                                  | Description                                                                                         | Accept variable | Required | Since |
|:----------------------------------------------------- |:--------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name                                                  | The name of the server                                                                              | -               | &#x2713; |       |
| type                                                  | The type of server: "http" (the GUI "Http Server") or "tcp" (the GUI regular "Server", used by non-HTTP requests). The default value is "http". See [tcp server](#tcp-server). | -               | -        | 2027.1 |
| host                                                  | The host of the server                                                                              | &#x2713;        | &#x2713; |       |
| scheme                                                | The scheme of the server. The available values are "http" and "https". The default value is "http". | -               | -        |       |
| port                                                  | The port of the server: a whole number from 1 to 65535, or a variable. The default value is "80" (for http scheme) or "443" (for https scheme). Required for a tcp server. | &#x2713;        | -        |       |
| [basic_authentication](#basic_authentication)         | The Basic Authentication used to authenticate on the server.                                        | -               | -        |       |
| [ntlm_authentication](#ntlm_authentication)           | The NTLM Authentication used to authenticate on the server.                                         | -               | -        |       |
| [negotiate_authentication](#negotiate_authentication) | The Negotiate Authentication used to authenticate on the server.                                    | -               | -        |       |
| [authentications](#authentications)                   | The authentications of the server, one per realm, as the rows of the GUI "Authorization" table.     | -               | -        | 2027.1 |
| [url_rewriting](#url_rewriting)                       | The HTTP URL rewriting of the server.                                                               | -               | -        | 2027.1 |

A server accepts at most one of `basic_authentication`, `ntlm_authentication`, `negotiate_authentication` and `authentications`.

:warning: Servers are read strictly: an unknown key (for example a misspelled `prot:`) is rejected. `type`, `authentications` and `url_rewriting` need `schemaVersion: "3.2"`.

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

## tcp server
A tcp server is the GUI regular "Server": the host and port of a non-HTTP request. It accepts only `name`, `type`, `host` and `port`, and `port` is required. An HTTP request can't use a tcp server.

#### Example
```yaml
schemaVersion: "3.2"
servers:
- name: media
  type: tcp
  host: media.intranet.company.com
  port: 1935
```

## authentications
The authentications of the server, one per row of the GUI "Authorization" table. NeoLoad answers the challenge the server sends (Basic, Digest, NTLM or Negotiate) with the credentials of the matching realm.

| Name               | Description                                                                                                       | Accept variable | Required | Since  |
|:------------------ |:----------------------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:------:|
| login              | The login of the user account used to authenticate on the server. It can be empty (`""`), as in the GUI table.     | &#x2713;        | &#x2713; | 2027.1 |
| password           | The password of the user account used to authenticate on the server. It can be empty (`""`), as in the GUI table.  | &#x2713;        | &#x2713; | 2027.1 |
| realm              | The realm (Basic, Digest) or the domain (NTLM, Negotiate). Without it, the credentials answer any realm. Two authentications of a server can't share a realm. | -               | -        | 2027.1 |
| preferred_protocol | The protocol used when the server sends a Negotiate challenge: "kerberos" or "ntlm". Without it, NeoLoad picks the protocol itself (the GUI "Default"). | -               | -        | 2027.1 |

When NeoLoad saves a project to YAML, a server with a single authentication, no preferred protocol and a login and a password that are not empty is written with `basic_authentication`, `ntlm_authentication` or `negotiate_authentication`, which require both; any other server is written with `authentications`. When several rows of the GUI table share a realm, NeoLoad only uses the first one: only that one is written.

#### Example
```yaml
schemaVersion: "3.2"
servers:
- name: intranet
  host: intranet.company.com
  authentications:
  - login: ${user}
    password: ${password}
    realm: CORP
    preferred_protocol: kerberos
  - login: ${admin_login}
    password: ${admin_password}
    realm: admin-realm
```

## url_rewriting
The GUI "HTTP URL rewriting": NeoLoad carries the session id the server puts in the URLs (for example `jsessionid`) to the next requests. A server with this block has URL rewriting enabled.

| Name             | Description                                                                                                       | Accept variable | Required | Since  |
|:---------------- |:----------------------------------------------------------------------------------------------------------------- |:---------------:|:--------:|:------:|
| session_argument | The name of the session argument, for example "jsessionid"                                                        | -               | &#x2713; | 2027.1 |
| in_path          | "true" when the session id is in the path (`/a;jsessionid=X`, the GUI "Parameter is a path extension"), "false" when it is a parameter. The default value is "false". | -               | -        | 2027.1 |
| path_separator   | The separator in front of the session argument in the path. The default value is ";". Only with `in_path: true`.  | -               | -        | 2027.1 |
| equals_sign      | "false" when there is no "=" between the session argument and its value (the GUI "Put equals"). The default value is "true". Only with `in_path: true`. | -               | -        | 2027.1 |

#### Example
```yaml
schemaVersion: "3.2"
servers:
- name: shop
  host: shop.company.com
  url_rewriting:
    session_argument: jsessionid
    in_path: true
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
