# Web Services Security

## Overview

The `web_services_security` section declares the WS-Security material used by [soap_request](soap_request.md) steps:
keystores, request profiles and response profiles. A SOAP request selects a profile by name with
`request_security_profile` and `response_security_profile`.

This section is available since NeoLoad **2026.3** (schema 3.1).

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
  - name: ServerKeystore
    path: ./wss-keystores/server.p12
    password: secret
  request_profiles:
  - name: MyRequestProfile
    headers:
    - actor: http://example.com/actor
      must_understand: true
      tokens:
      - username_token:
          username: user
          password: secret
          password_type: digest
          nonce: true
          created: true
          salt: mysalt
          iteration: 1000
      - timestamp:
          id: TS-1
          time_to_live: 300
          milliseconds: true
  response_profiles:
  - name: MyResponseProfile
    keystore: ServerKeystore
```

## keystores

| Name     | Description                                                                | Accept variable | Required | Since |
|:-------- |:-------------------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name     | The keystore name, referenced by response profiles                         | -               | &#x2713; | 2026.3|
| path     | The keystore file, relative to the project folder                          | -               | &#x2713; | 2026.3|
| password | The keystore password. Written in clear text; encoded when the project is rewritten | -      | -        | 2026.3|

## request_profiles

| Name    | Description                                  | Accept variable | Required | Since |
|:------- |:-------------------------------------------- |:---------------:|:--------:|:-----:|
| name    | The profile name, referenced by `request_security_profile` | - | &#x2713; | 2026.3|
| headers | The list of security headers added to the request | -          | -        | 2026.3|

#### headers

| Name            | Description                                       | Accept variable | Required | Since |
|:--------------- |:------------------------------------------------- |:---------------:|:--------:|:-----:|
| actor           | The SOAP actor/role targeted by the header        | -               | -        | 2026.3|
| must_understand | Whether the receiver must process the header      | -               | -        | 2026.3|
| tokens          | The tokens of the header: `username_token` or `timestamp`, one per list item | - | - | 2026.3|

#### username_token

| Name          | Description                                                   | Accept variable | Required | Since |
|:------------- |:------------------------------------------------------------- |:---------------:|:--------:|:-----:|
| username      | The user name                                                 | -               | &#x2713; | 2026.3|
| password      | The password                                                  | -               | -        | 2026.3|
| password_type | `text`, `digest` or `derived`                                 | -               | -        | 2026.3|
| nonce         | Add a nonce                                                   | -               | -        | 2026.3|
| created       | Add a creation timestamp                                      | -               | -        | 2026.3|
| salt          | The salt used for a derived key                               | -               | -        | 2026.3|
| iteration     | The iteration count used for a derived key                    | -               | -        | 2026.3|

#### timestamp

| Name         | Description                              | Accept variable | Required | Since |
|:------------ |:---------------------------------------- |:---------------:|:--------:|:-----:|
| id           | The timestamp identifier                 | -               | -        | 2026.3|
| time_to_live | The validity duration, in seconds        | -               | -        | 2026.3|
| milliseconds | Use millisecond precision                | -               | -        | 2026.3|

## response_profiles

| Name     | Description                                             | Accept variable | Required | Since |
|:-------- |:------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name     | The profile name, referenced by `response_security_profile` | -           | &#x2713; | 2026.3|
| keystore | The name of the keystore used to process the response   | -               | -        | 2026.3|
