# Web Services Security

## Overview

The `web_services_security` section declares the WS-Security material used by [soap_request](soap_request.md) steps:
keystores, request profiles and response profiles.

This section is available since NeoLoad **2026.3** (schema 3.1).

The username token `password`, the `salt` of a derived `password_type` and the keystore `password` are secrets. In as-code they are authored as plaintext; NeoLoad stores them encrypted when the project is imported, and writes their NeoLoad-encrypted form when it exports a project back to as-code.

## Definition

| Name                         | Description                                          | Accept variable | Required | Since |
|:---------------------------- |:---------------------------------------------------- |:---------------:|:--------:|:-----:|
| [keystores](#keystores)      | The keystores holding the certificates and keys      | -               | -        | 2026.3|
| [request_profiles](#request_profiles)   | The security applied to outgoing requests | -         | -        | 2026.3|
| [response_profiles](#response_profiles) | The security checked on incoming responses | -        | -        | 2026.3|

#### Example

```yaml
web_services_security:
  keystores:
  - path: ./wss-keystores/server.p12
    password: secret
  request_profiles:
  - name: MyRequestProfile
    headers:
    - actor: http://example.com/actor
      must_understand: true
      tokens:
      - username:
          username: user
          password: secret
          password_type: digest
          nonce: true
          created: true
      - timestamp:
          id: TS-1
          time_to_live: 300
          milliseconds: true
  response_profiles:
  - name: MyResponseProfile
    keystore: ./wss-keystores/server.p12
```

## keystores

| Name     | Description                                                                | Accept variable | Required | Since |
|:-------- |:-------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| path     | The keystore file, relative to the project folder. Identifies the keystore in response profiles. Must be unique among keystores | -               | &#x2713; | 2026.3|
| password | The keystore password, shared by every element using the keystore | &#x2713; | - | 2026.3|

## request_profiles

| Name    | Description                                  | Accept variable | Required | Since |
|:------- |:-------------------------------------------- |:---------------:|:--------:|:-----:|
| name    | The profile name. Must be unique among request profiles | - | &#x2713; | 2026.3|
| headers | The list of security headers added to the request | -          | -        | 2026.3|

#### headers

| Name            | Description                                       | Accept variable | Required | Since |
|:--------------- |:------------------------------------------------- |:---------------:|:--------:|:-----:|
| actor           | The SOAP actor/role targeted by the header        | -               | -        | 2026.3|
| must_understand | Whether the receiver must process the header. Defaults to `false` | -               | -        | 2026.3|
| tokens          | The list of tokens of the header. Each token is either a `username` or a `timestamp` | - | - | 2026.3|

#### username

| Name          | Description                                                   | Accept variable | Required | Since |
|:------------- |:------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| username      | The user name                                                 | &#x2713;        | &#x2713; | 2026.3|
| password      | The password                                                  | &#x2713;        | -        | 2026.3|
| [password_type](#password_type) | How the password is put in the token: `plain_text`, `digest` or `derived_key`. Defaults to `digest`. | - | - | 2026.3|
| nonce         | Add a nonce. Defaults to `false`. Ignored with `digest`, which always adds it | -               | -        | 2026.3|
| created       | Add a creation timestamp. Defaults to `false`. Ignored with `digest`, which always adds it | -               | -        | 2026.3|

#### password_type

- `plain_text`: the password is sent as plain text. Only safe over TLS.
- `digest`: only a hash is sent, `Base64(SHA-1(nonce + created + password))`, so the password is never exposed and the nonce lets the server reject a replayed message. A nonce and a creation timestamp are always added with this type: the `nonce` and `created` values are ignored and never written back.
- `derived_key`: no password is sent. A key is derived from the password, `salt` and `iteration` count, and used to sign or encrypt the message.

```yaml
password_type:
  derived_key:
    salt: mysalt
    iteration: 1000
```

| Name      | Description                                | Accept variable | Required | Since |
|:--------- |:------------------------------------------ |:---------------:|:--------:|:-----:|
| derived_key.salt      | The salt used for the derived key          | &#x2713;        | &#x2713; | 2026.3|
| derived_key.iteration | The iteration count used for the derived key | &#x2713;      | &#x2713; | 2026.3|

#### timestamp

| Name         | Description                              | Accept variable | Required | Since |
|:------------ |:---------------------------------------- |:---------------:|:--------:|:-----:|
| id           | The timestamp identifier                 | -               | -        | 2026.3|
| time_to_live | The validity duration, in seconds. Defaults to `300` | -               | -        | 2026.3|
| milliseconds | Use millisecond precision. Defaults to `true`                | -               | -        | 2026.3|

## response_profiles

| Name     | Description                                             | Accept variable | Required | Since |
|:-------- |:------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name     | The profile name. Must be unique among response profiles | -           | &#x2713; | 2026.3|
| keystore | The `path` of the keystore used to process the response   | -               | &#x2713; | 2026.3|
