# Web Services Security

## Overview

The `web_services_security` section declares the WS-Security material used by [soap_request](soap_request.md) steps:
keystores, request profiles and response profiles. A SOAP request selects a profile by name with
`request_security_profile` and `response_security_profile`.

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
| name    | The profile name, referenced by `request_security_profile`. Must be unique among request profiles | - | &#x2713; | 2026.3|
| headers | The list of security headers added to the request | -          | -        | 2026.3|

#### headers

| Name            | Description                                       | Accept variable | Required | Since |
|:--------------- |:------------------------------------------------- |:---------------:|:--------:|:-----:|
| actor           | The SOAP actor/role targeted by the header        | -               | -        | 2026.3|
| must_understand | Whether the receiver must process the header. Defaults to `false` | -               | -        | 2026.3|
| [tokens](#tokens) | The list of tokens of the header. Each token is a `username`, `timestamp`, `signature` or `encrypt` | - | - | 2026.3|

## tokens

Each item of a header `tokens` list holds exactly one token, keyed by its type.

| Type | Description | Since |
|:---- |:----------- |:-----:|
| [username](#username) | Identifies the user with a user name and an optional password | 2026.3|
| [timestamp](#timestamp) | Adds the creation and expiry times of the message | 2026.3|
| [signature](#signature) | Signs the message with a key of a declared keystore | 2026.3|
| [encrypt](#encrypt) | Encrypts the message with a key of a declared keystore | 2026.3|

### username

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

### timestamp

| Name         | Description                              | Accept variable | Required | Since |
|:------------ |:---------------------------------------- |:---------------:|:--------:|:-----:|
| id           | The timestamp identifier                 | -               | -        | 2026.3|
| time_to_live | The validity duration, in seconds. Defaults to `300` | -               | -        | 2026.3|
| milliseconds | Use millisecond precision. Defaults to `true`                | -               | -        | 2026.3|

### signature

Signs the message with a key of a keystore declared in `keystores`.

| Name | Description | Accept variable | Required | Since |
|:---- |:----------- |:---------------:|:--------:|:-----:|
| keystore | The `path` of a keystore declared in `keystores` | - | &#x2713; | 2026.3|
| alias | The alias of the key in the keystore | &#x2713; | &#x2713; | 2026.3|
| key_identifier_type | How the key is referenced in the message: `direct_reference`, `direct_reference_first_certificate`, `issuer_reference`, `x509_key_identifier`, `subject_key_identifier`, `thumbprint_sha1` or `encrypted_key_sha1`. Defaults to `issuer_reference` | - | - | 2026.3|
| signature_algorithm | `rsa_sha1`, `rsa_sha256`, `rsa_sha384`, `rsa_sha512`, `rsa_ripemd160`, `rsa_md5`, `hmac_md5`, `hmac_ripemd160`, `hmac_sha1`, `hmac_sha256`, `hmac_sha384`, `hmac_sha512`, `dsa_sha1`, `ecdsa_sha1`, `ecdsa_sha256`, `ecdsa_sha384` or `ecdsa_sha512`. Defaults to `rsa_sha1` | - | - | 2026.3|
| digest_algorithm | `sha1`, `sha256`, `sha384`, `sha512` or `ripemd160`. Defaults to `sha1` | - | - | 2026.3|
| canonicalization_algorithm | `canonical_xml_omit_comments`, `canonical_xml_with_comments`, `exclusive_canonical_xml_omit_comments` or `exclusive_canonical_xml_with_comments`. Defaults to `exclusive_canonical_xml_omit_comments` | - | - | 2026.3|
| [parts](#parts) | The parts of the message to sign. Defaults to an empty list | - | - | 2026.3|

### encrypt

Encrypts the message with the key of a keystore declared in `keystores`.

| Name | Description | Accept variable | Required | Since |
|:---- |:----------- |:---------------:|:--------:|:-----:|
| keystore | The `path` of a keystore declared in `keystores` | - | &#x2713; | 2026.3|
| alias | The alias of the key in the keystore | &#x2713; | &#x2713; | 2026.3|
| key_identifier_type | Same values as `signature`, except `direct_reference_first_certificate`. Defaults to `issuer_reference` | - | - | 2026.3|
| symmetric_encryption_algorithm | `triple_des`, `aes_128`, `aes_192` or `aes_256`. Defaults to `aes_128` | - | - | 2026.3|
| asymmetric_encryption_algorithm | `rsa_v1_5`, `rsa_oaep` or `none` to send the symmetric key unencrypted. Defaults to `rsa_v1_5` | - | - | 2026.3|
| [parts](#parts) | The parts of the message to encrypt. Defaults to an empty list | - | - | 2026.3|

#### parts

A part is identified either by `id`, or by `name` and an optional `namespace`.

| Name | Description | Accept variable | Required | Since |
|:---- |:----------- |:---------------:|:--------:|:-----:|
| id | The identifier of the part. Exclusive with `name` and `namespace` | - | - | 2026.3|
| name | The element name of the part. Required when `id` is not set | - | - | 2026.3|
| namespace | The namespace of the element | - | - | 2026.3|
| encoding | `element` or `content`. Defaults to `element` | - | - | 2026.3|

```yaml
tokens:
- signature:
    keystore: ./wss-keystores/client.p12
    alias: ${alias}
    key_identifier_type: direct_reference_first_certificate
    signature_algorithm: rsa_sha256
    digest_algorithm: sha256
    parts:
    - id: Body-1
- encrypt:
    keystore: ./wss-keystores/client.p12
    alias: server
    symmetric_encryption_algorithm: aes_256
    asymmetric_encryption_algorithm: rsa_oaep
    parts:
    - name: Body
      namespace: http://schemas.xmlsoap.org/soap/envelope/
      encoding: content
```

## response_profiles

| Name     | Description                                             | Accept variable | Required | Since |
|:-------- |:------------------------------------------------------- |:---------------:|:--------:|:-----:|
| name     | The profile name, referenced by `response_security_profile`. Must be unique among response profiles | -           | &#x2713; | 2026.3|
| keystore | The `path` of the keystore used to process the response   | -               | &#x2713; | 2026.3|
