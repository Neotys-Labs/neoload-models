# Population
A Population is a group of Virtual Users.

:warning: If the project already have a Population with the same name from NeoLoad, the whole Population will be __replaced__ by the as-code one.

#### Available settings are:

| Name                                  | Description                          | Accept variable  | Required | Since |
|:------------------------------------- |:------------------------------------ |:----------------:|:--------:|:-----:|
| name                                  | The name of the Population           | -                | &#x2713; |       |
| description                           | The description of the Population    | -                | -        |       |
| [user_paths](#user-path-distribution) | The list of User Path distributions  | -                | &#x2713; |       |

#### Example

Defining a population with 1 User Path.

```yaml
populations:
- name: population1
  user_paths:
  - name: user_path_1
```
  
## User Path distribution

| Name         | Description                                                  | Accept variable  | Required | Since |
|:------------ |:------------------------------------------------------------ |:----------------:|:--------:|:-----:|
| name         | The name of the existing User Path                           | -                | &#x2713; |       |
| distribution | The percentage of the User Path in Population. The value is a percentage and may be set within precision of 0.1%.<br>If the distribution is not present, its value will be proportionally calculated. | - | - |  |
| [browser](#browser) | The browser emulated by the Virtual Users of the User Path. By default, the recorded browser. | - | - | 2027.1 |
| cache        | How the Virtual Users handle the browser cache: `as_recorded` (the default value), `new_user` (empty cache, as for a first visit) or `returning_user` (cache already filled). | - | - | 2027.1 |
| [wan_emulation](#wan-emulation) | The WAN emulation of the Virtual Users of the User Path: the bandwidth, latency and packet loss of the download and of the upload. By default, none. | - | - | 2027.1 |

`browser`, `cache` and `wan_emulation` need `schemaVersion: "3.2"`.

#### Example 1

Defining a population with 2 User Paths: `user_path_1` takes 30.3% and `user_path_2` takes 66.7%.

```yaml
populations:
- name: population1
  user_paths:
  - name: user_path_1
    distribution: 33.3%
  - name: user_path_2
    distribution: 66.7%
```

#### Example 2

Defining a population with 2 User Paths: `user_path_1` takes 50% and `user_path_2` takes 50%.

```yaml
populations:
- name: population1
  user_paths:
  - name: user_path_1
  - name: user_path_2
```

## Browser

The browser emulated by the Virtual Users of a User Path, as the "Browser" and "HTTP/2" columns of the Population table in NeoLoad. The profile is defined by its values, like in a NeoLoad project: `name` is only the label shown in NeoLoad. Every setting is optional and takes its default value when left out.

| Name            | Description                                                  | Accept variable  | Required | Since  |
|:--------------- |:------------------------------------------------------------ |:----------------:|:--------:|:------:|
| name            | The label of the browser profile shown in NeoLoad. Without name, the profile is shown as customized. | - | - | 2027.1 |
| user_agent      | The User-Agent sent by the browser. Without user agent, the browser is the recorded one, with the other settings applied to it. | - | - | 2027.1 |
| connections     | The maximum number of connections the browser opens at the same time to a server. It must be greater than or equal to 1. The default value is 6. | - | - | 2027.1 |
| cookies         | Whether the browser stores and sends the cookies. The default value is `true`. | - | - | 2027.1 |
| http2           | Whether the browser uses HTTP/2 when the server supports it. The default value is `true`. | - | - | 2027.1 |
| accept_encoding | The content encodings the browser accepts, among `gzip`, `xgzip`, `zip`, `deflate`, `deflate_rfc1951`, `exi`, `compress`, `pack200_gzip`, `bzip2`, `lzma`, `xz`, `brotli` and `identity`. The NeoLoad browser profile editor only offers `gzip`, `deflate`, `brotli`, `exi`, `bzip2`, `lzma` and `xz`; the others are only available in as-code. NeoLoad decodes the responses in these encodings, and sends them as the value of the `Accept-Encoding` header of the requests that have one (as recorded requests do); it does not add the header to a request without it. The default value is `[gzip, deflate, brotli]`; an empty list is the default list. | - | - | 2027.1 |

#### Example 1

`user_path_1` emulates Chrome 124 with an empty cache, as a first visit.

```yaml
populations:
- name: population1
  user_paths:
  - name: user_path_1
    browser:
      name: Chrome 124.0
      user_agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
    cache: new_user
```

#### Example 2

`user_path_1` keeps the recorded browser without HTTP/2, and its cache is already filled.

```yaml
populations:
- name: population1
  user_paths:
  - name: user_path_1
    browser:
      http2: false
    cache: returning_user
```

## WAN emulation

The WAN emulation of the Virtual Users of a User Path, as the "WAN Emulation" columns of the Population table in NeoLoad: it limits the network between the Virtual Users and the servers. The profile is defined by its values, like in a NeoLoad project: `name` is only the label shown in NeoLoad.

| Name     | Description                                                  | Accept variable  | Required | Since  |
|:-------- |:------------------------------------------------------------ |:----------------:|:--------:|:------:|
| name     | The label of the WAN emulation profile shown in NeoLoad. Without name, the profile is shown as customized. | - | - | 2027.1 |
| download | The network conditions of the responses, from the servers to the Virtual Users, see [WAN emulation direction](#wan-emulation-direction). By default, no limit. | - | - | 2027.1 |
| upload   | The network conditions of the requests, from the Virtual Users to the servers, see [WAN emulation direction](#wan-emulation-direction). By default, no limit. | - | - | 2027.1 |

A WAN emulation without any limit is the same as no WAN emulation, and is not written back.

:warning: The Load Generator applies the WAN emulation only when it supports it (WAN emulation enabled on a Linux or Windows Load Generator). Otherwise, the Test Run continues without WAN emulation and the Load Generator logs it, as for a project designed in NeoLoad.

The mobile profiles of NeoLoad (3G, 4G...) have other values for an average and a poor signal. In as-code, a profile has one set of values: a mobile profile saved from NeoLoad is written with the values of its selected signal strength.

### WAN emulation direction

| Name        | Description                                                  | Accept variable  | Required | Since  |
|:----------- |:------------------------------------------------------------ |:----------------:|:--------:|:------:|
| bandwidth   | The bandwidth: a positive rate with up to 3 decimals in `bps`, `Kbps`, `Mbps` or `Gbps` (for example `2560Kbps` or `1.5Mbps`), or `unlimited`. The default value is `unlimited`. | - | - | 2027.1 |
| latency     | The latency added to each packet, as a whole number of milliseconds from 0 to 1000 (for example `40ms`). The default value is `0ms`. | - | - | 2027.1 |
| packet_loss | The percentage of packets lost, from 0 to 10% with up to 3 decimals (for example `1.5%`). The default value is `0%`. | - | - | 2027.1 |

#### Example 1

`user_path_1` emulates an ADSL line of 2560 Kbps down and 256 Kbps up.

```yaml
populations:
- name: population1
  user_paths:
  - name: user_path_1
    wan_emulation:
      name: ADSL 2048
      download:
        bandwidth: 2560Kbps
      upload:
        bandwidth: 256Kbps
```

#### Example 2

`user_path_1` has an unlimited bandwidth, with 100 ms of latency and 1% of lost packets on the download.

```yaml
populations:
- name: population1
  user_paths:
  - name: user_path_1
    wan_emulation:
      download:
        latency: 100ms
        packet_loss: 1%
```
