# NeoLoad as-code — feature coverage

This document compares the NeoLoad as-code (YAML/JSON) DSL with the NeoLoad GUI project format
(`.nlp` XML): what a GUI project can express that as-code **cannot** express yet, grouped by
functional area. It is the reference for scoping the next as-code version.

For what is new in each released as-code version, see [CHANGELOG.md](CHANGELOG.md).

Keep it up to date with every DSL change: a gap that is not listed here is invisible to the people
who plan the next schema version.

## Reference points

| Name | Meaning | Supported by | Reference |
|:-----|:--------|:-------------|:----------|
| as-code **3.0** | DSL as shipped up to NeoLoad 2026.2 | all NLG and all NLW versions | `neoload-models` release **3.3.7** (`dbf642b9`) |
| as-code **3.1** | DSL as shipped with NeoLoad 2026.3 | NLG `>= 2026.3`, NLW `>= 2026.3` | `neoload-models` release **3.4.6** (`4493c0f3`), branch `v3` |
| **gap closure** | Merged in neither of the above, in flight | not released | branch `featuregroup/as-code-gap-closure` |
| **NLP** | NeoLoad GUI project, default XML format | - | `neoload-root`, branch `develop` |

The **Supported by** column mirrors `schemas/compatibility.json`, which is the source of truth and
also covers the CheckVU CLI.

Scope for the NLP comparison: the **Design** and **Runtime** tabs of NeoLoad GUI. The **Results**
tab is out of scope.

`schemaVersion` declares which contract a file targets. It does **not** gate the parser: the model
accepts every construct it knows about regardless of the declared version, and only checks that the
declared version is one this build knows (`supported-schemas.json`). Version gating happens in the
published JSON Schemas (`schemas/v3.0/`, `schemas/v3.1/`), which editors use.

## In flight on `featuregroup/as-code-gap-closure`

Implemented in the model but not merged into `v3`, therefore in neither 3.0 nor 3.1. Candidates
for 3.2. None of them is documented in `neoload-project/doc/v3/` yet.

**Steps**

- `http_page` — the GUI HTTP Page container: `think_time`, `think_time_range` (`min` / `max`),
  `think_time_mode`, `screenshot`, `dynamic_action`, `force_encoding_for_dynamic_resources`,
  `steps`.
- `wait_until` — waits for `conditions`, with a `timeout` defaulting to 60 s.
- `rendezvous` — the Rendezvous action inside a User Path.

**Variables** — `date`, `current_date`, `sql`, `password`, `secret_vault`. With these five, the
as-code variable catalogue matches the fourteen types offered by the GUI.

**Assertions** — the `assertions` list becomes a `{content|size|duration}` discriminated union:
`size` (with an `operator`) and `duration` join the existing content assertion.

**Frameworks** — a `framework` model (`enabled`, `parameters`) with dynamic parameters
(`enabled`, `extraction_source`, `xpath`, `jsonpath`, `regexp`, `template`), covering the GUI
Frameworks / dynamic parameters feature, in a builtin and a custom flavour.

**JSON Schema completeness** — request `name`, `followRedirects`, custom method, `bodybinary`,
multipart `parts`; `assertions` on request, transaction and container; scenario `sla_profile`,
`store_variables_for_raw_data`, `excluded_urls`, `apm_configuration`, `monitoring`,
`rendezvous_policies`; `custom_load` policy.

**Binding fix** — `Part` bound to `ImmutablePart` so multipart bodies can be deserialised from YAML.

## Reverted before release

- **`rendezvous` step** — implemented under LOAD-38588 and reverted in `4239a72c` before 3.4.6.
  It is **not** part of 3.1. The scenario-level
  [`rendezvous_policies`](neoload-project/doc/v3/rendezvous_policy.md), which already existed in
  3.0, is unaffected.
- **Request `name` unbound from the YAML binding and defaulted to the URL** — implemented under
  LOAD-39283 and reverted in `4ff8ee31` before 3.4.6. `name` behaves as it did in 3.0.

---

The tables below list, per functional area, what a `.nlp` project can express and where as-code
stands. Legend: **3.0** / **3.1** = available in that contract; **gap** = implemented on
`featuregroup/as-code-gap-closure`; **—** = not implemented anywhere.

## Project and settings

| NLP feature | as-code |
|:------------|:--------|
| Project name, description | 3.0 |
| Split across several files (`includes`) | 3.0 |
| Dynatrace integration settings (`dynatrace.enabled` / `url` / `token`) | 3.0 |
| qTest integration settings (`qtest.*`) | 3.0 |
| Certificates (`CertificateSettings`) | — |
| Web Services security / WSS (`WSSSettings`) | — |
| Page naming policy (`PageNamingSettings`) | — |
| JavaScript policy (`JSPolicySettings`) | — |
| Error handling policy (`ErrorHandlingSettings`) | — |
| Global assertion settings (`GlobalAssertionSettings`) | — |
| Request settings — keep-alive, timeouts, encoding (`RequestsSettings`) | — |
| Performance and statistics settings | — |
| Media streaming settings | — |
| AMF and Oracle Forms settings | — |
| Plugin identification settings | — |
| REST APIs settings, SSH security settings | — |
| Team Server settings | — |
| AppDynamics, Introscope (CA APM), Nudge settings | — |
| Recorder and proxy settings | — |
| Advanced project settings | — |

## Servers

| NLP feature | as-code |
|:------------|:--------|
| Name, host, port, scheme | 3.0 |
| Basic / NTLM / Negotiate authentication | 3.0 |
| Server description | — (dropped by `ServerDeserializer`) |
| URL rewriting — session argument name, path separator, path extension (`HttpServer`) | — |

## Variables

The GUI offers fourteen types (`VariablesPane#initialize`).

| NLP type | as-code |
|:---------|:--------|
| Constant | 3.0 |
| File | 3.0 |
| Counter | 3.0 |
| Random number | 3.0 |
| JavaScript | 3.0 |
| List | 3.1 |
| Random string | 3.1 |
| Random UUID | 3.1 |
| Shared queue | 3.1 |
| Date | gap |
| Current date | gap |
| SQL | gap |
| Password | gap |
| Key vault / secret vault | gap |

## User Path structure

| NLP feature | as-code |
|:------------|:--------|
| Init / Actions / End containers | 3.0 |
| User session reset policy (`reset_on` / `reset_off` / `reset_auto`) | 3.0 |
| Containers and transactions, nested | 3.0 |
| SLA profile on User Path container, transaction and request | 3.0 |
| Think time policy overridden per User Path (`getThinkTimePolicy`) | — |
| Error policy per User Path (`getErrorPolicy`) | — |
| Failed-assertion policy per User Path (`getFailedAssertionPolicy`) | — |
| Action enabled / disabled flag | — |
| Breakpoints | — |
| Shared elements (`SharedElementsContainer`) | — |

## User Path — logical actions

The GUI action palette (`ActionTreeModel#getActionNodes`) has fourteen entries plus the advanced
and protocol action catalogues.

| NLP action | as-code |
|:-----------|:--------|
| Container | 3.0 (`transaction`) |
| Delay, in delay mode and in think time mode | 3.0 (`delay` and `think_time`) |
| Loop | 3.0 |
| While | 3.0 |
| If … Then … Else | 3.0 |
| Switch | 3.0 |
| JavaScript | 3.0 |
| Advanced / SAP GUI / Citrix / RealBrowser / RTE actions | 3.0 (`custom_action`) |
| Try … Catch | 3.1 |
| Go to next iteration | 3.1 |
| Stop Virtual User | 3.1 |
| Fork | 3.1 |
| Variable modifier | 3.1 |
| Wait until | gap |
| Rendezvous | gap (reverted from 3.1) |

## User Path — requests and protocols

| NLP feature | as-code |
|:------------|:--------|
| HTTP/S request — URL, server, method, headers, text body, extractors | 3.0 |
| Binary body | 3.0 (`bodybinary`, undeclared in the published schema) |
| Follow redirects | 3.0 (`followRedirects`, undeclared in the published schema) |
| HTTP Page (a page and its dynamic resources) | gap (`http_page`) |
| Multipart / form-data body | gap (`parts` in the schema; the binding fix is on the branch) |
| Keep-alive, charset, content-type override, referer | — |
| Response storage to a file or a variable | — |
| Static request optimisation | — |
| Raw request | — |
| SOAP and Silverlight SOAP requests | — |
| WebSocket channel, WebSocket request, push message | — |
| Media / RTMP, RTMPT requests | — |
| Siebel request | — |
| Recorded artifacts (screenshots, recorded response) | — |

## Assertions and validation

| NLP feature | as-code |
|:------------|:--------|
| Content assertion (`contains`, `regexp`, `xpath`, `jsonpath`, `not`) | 3.0 |
| Assertions on request, transaction, container and User Path | 3.0 |
| Size assertion | gap |
| Duration assertion | gap |
| JSON assertions (`JsonContentAssertion`, `JsonResponseAssertion`) | — |
| Plugin assertions (`PluginContentAssertion`, `PluginResponseAssertion`) | — |
| Response assertion (`ResponseAssertion`) | — |
| Global assertion settings at project level | — |

## Variable extraction

| NLP feature | as-code |
|:------------|:--------|
| Extractor — `from`, `xpath`, `jsonpath`, `regexp`, `match_number`, `template`, `decode`, `extract_once`, `default`, `throw_assertion_error` | 3.0 |
| Frameworks / dynamic parameters (builtin and custom) | gap |
| Search and replace rules | — |

## Populations

| NLP feature | as-code |
|:------------|:--------|
| Population name, description | 3.0 |
| User Path list with `distribution` percentage | 3.0 |

## Scenarios and load policies

| NLP feature | as-code |
|:------------|:--------|
| Scenario name, description, SLA profile | 3.0 |
| Constant, ramp-up, peaks and custom load policies | 3.0 |
| `start_after` / `stop_after` / `rampup` per population | 3.0 |
| Duration in time or in iterations | 3.0 |
| Monitoring time before first VU and after last VU | 3.0 |
| Rendezvous policies (`name`, `when`, `timeout`) | 3.0 |
| Excluded URLs | 3.0 |
| Store variables for raw data | 3.0 |
| Load generators and zones per population (`getLgHostReferences`, `getExistingZones`) | — |
| Virtual User start delay and start mode (`getVuStartDelay`, `isVuStartModeSimultaneous`) | — |
| Debug policy — Virtual User logging mode (`getDebugPolicy`) | — |
| Pre-bench and post-bench Virtual User (`getPrebenchVU`, `getPostbenchVU`) | — |
| Data filters and auto data filters | — |
| Custom dashboards and graphs | — |
| Terminal services configuration | — |

## SLA

| NLP feature | as-code |
|:------------|:--------|
| SLA profile with KPI thresholds, `per test` and `per interval` scopes | 3.0 |
| `warn` / `fail` conditions with units | 3.0 |
| SLA profile applied to scenario, User Path container, transaction and request | 3.0 |

## Monitoring

No NeoLoad monitor can be defined in as-code, in any version. The GUI ships around fifty monitor
types (`com.neotys.nl.gui.design.monitors.MonitorType`) plus the pluggable connectors under
`neoload-monitoring/`. This is the single largest functional area missing from the DSL.

| Family | NLP monitors | as-code |
|:-------|:-------------|:--------|
| Operating systems | Linux, Windows, Solaris, AIX, HP-UX, RSTAT, SNMP, System | — |
| Web servers | Apache (HTTP), IIS | — |
| Java application servers | Tomcat, Tomcat 6, Tomcat 7, JBoss, JBoss 6, JBoss 7.1, WebLogic, WebLogic 9, WebLogic 10, WebLogic 11, WebLogic 12, WebSphere, WebSphere Liberty, GlassFish, JOnAS, OAS, OAS HTTP, NetWeaver 7.0, NetWeaver 7.1 | — |
| Databases | Oracle, MySQL, SQL Server, PostgreSQL, PostgreSQL 9.2, DB2, DB2 10.5, MongoDB | — |
| Other | .NET, VMware, LCDS, Kaazing, JMX custom, Java URL, JSON, imported monitors, Prometheus, REST API, generic JSON | — |

APM integrations:

| NLP feature | as-code |
|:------------|:--------|
| Dynatrace tags on a scenario | 3.0 (`apm_configuration.dynatrace_tags`) |
| Dynatrace anomaly rules | 3.0 (`apm_configuration.dynatrace_anomaly_rules`) |
| Dynatrace connection settings | 3.0 (`project_settings.dynatrace.*`) |
| AppDynamics | — |
| CA APM / Introscope | — |
| Datadog, Prometheus and the other APM connectors | — |

## Design tooling with no as-code equivalent

These are GUI-only workflows rather than project data, listed for completeness: recorder and
recording settings, redesign, flagging, compare with recorded, missing element repair, team
collaboration (Team Server, shared elements), project-level search and replace, sanity check.
