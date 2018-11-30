# NeoLoad Server definition
Definition has several top-level keys.

| Name      | Description        |
| --------- | ------------------ |
| Servers   | List of servers    |
You can find bellow an example on how to define servers when launching a test in command line using the `-project` parameter.

Please read the full [command line documentation](https://www.neotys.com/documents/doc/neoload/latest/en/html/#643.htm).

## Server
The server settings are centralized, which means the target server for the test can be quickly changed. It is possible, for example, to quickly switch from the development to the pre-production server.

**Available settings are:**

| Name        | Description                                                  | Required/Optional |
| ----------- | ------------------------------------------------------------ | ----------------- |
| name        | The name of the server.                                      | Required          |
| host | The host of the server.                                    | Required          |
| port | The port of the server. The default value is 80.                    | Optional          |
| scheme | The scheme of the server. The available values are "http" and "https". The default value is http.    | Optional          |
| basic-authentication | The Basic Authentication used to authenticate to the server.                    | Optional          |
| ntlm-authentication | The NTLM Authentication used to authenticate to the server.                     | Optional          |
| negociate-authentication | The Negociate Authentication used to authenticate to the server.           | Optional          |

### basic-authentication
| Name        | Description                                                     | Required/Optional |
| ----------- | --------------------------------------------------------------- | ----------------- |
| login       | The login of user account used to authenticate to the server.                                         | Required          |
| password    | The password of user account used to authenticate to the server.                                                   | Required          |
| realm       | The realm.                                                      | Optional          |

### ntlm-authentication
| Name        | Description                                                     | Required/Optional |
| ----------- | --------------------------------------------------------------- | ----------------- |
| login       | The login of user account used to authenticate to the server.                                         | Required          |
| password    | The password of user account used to authenticate to the server.                                                   | Required          |
| domain      | The domain.                                                     | Optional          |

### negociate-authentication
| Name        | Description                                                     | Required/Optional |
| ----------- | --------------------------------------------------------------- | ----------------- |
| login       | The login of user account used to authenticate to the server.                                         | Required          |
| password    | The password of user account used to authenticate to the server.                                                   | Required          |
| domain | The domain.                                                          | Optional          |


**Example:**

Define 2 servers: a server with only the host-name, a secure server https with a basic authentication.

```yaml
servers:
- name: my-server
  host: host-server
- name: my-secure-server
  host: host-secure-server
  port: 443
  scheme: https
  basic-authentication:
   login: admin
   password: secret
   realm: realm-value
```