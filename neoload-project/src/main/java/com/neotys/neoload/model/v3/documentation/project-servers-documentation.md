# NeoLoad Server definition
Definition has several top-level keys.

| Name      | Description        |
| --------- | ------------------ |
| servers   | List of servers    |

You can find below an example of how to define servers when launching a test in command line using the `-project` parameter.

Please read the full [command line documentation](https://www.neotys.com/documents/doc/neoload/latest/en/html/#643.htm).

## Server
The server settings are centralized, which means the target server for the test can be quickly changed. It is possible, for example, to quickly switch from the development to the pre-production server.

**Available settings are:**

| Name        | Description                                                  | Required/Optional |
| ----------- | ------------------------------------------------------------ | ----------------- |
| name        | The name of the server.                                      | Required          |
| host | The host of the server.                                    | Required          |
| scheme | The scheme of the server. The available values are "http" and "https". The default value is "http".    | Optional          |
| port | The port of the server. The default value is "80" (for http scheme) or "443" (for https scheme).                    | Optional          |
| basic-authentication | The Basic Authentication used to authenticate on the server.                    | Optional          |
| ntlm-authentication | The NTLM Authentication used to authenticate on the server.                     | Optional          |
| negotiate-authentication | The Negotiate Authentication used to authenticate on the server.           | Optional          |

### basic-authentication
| Name        | Description                                                     | Required/Optional |
| ----------- | --------------------------------------------------------------- | ----------------- |
| login       | The login of the user account used to authenticate on the server.                                         | Required          |
| password    | The password of the user account used to authenticate on the server.                                                   | Required          |
| realm       | The realm.                                                      | Optional          |

### ntlm-authentication
| Name        | Description                                                     | Required/Optional |
| ----------- | --------------------------------------------------------------- | ----------------- |
| login       | The login of the user account used to authenticate on the server.                                         | Required          |
| password    | The password of the user account used to authenticate on the server.                                                   | Required          |
| domain      | The domain.                                                     | Optional          |

### negotiate-authentication
| Name        | Description                                                     | Required/Optional |
| ----------- | --------------------------------------------------------------- | ----------------- |
| login       | The login of the user account used to authenticate on the server.                                         | Required          |
| password    | The password of the user account used to authenticate on the server.                                                   | Required          |
| domain | The domain.                                                          | Optional          |


**Example:**

Defining 2 servers: one with the host-name only, and an https one with a basic authentication.

```yaml
servers:
- name: my-server
  host: host-server
- name: my-secure-server
  host: host-secure-server
  scheme: https
  port: 443
  basic-authentication:
   login: admin
   password: secret
   realm: realm-value
```