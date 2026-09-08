# NeoLoad as-code — changelog and NLP coverage

This document tracks the NeoLoad as-code (YAML/JSON) DSL:

- **Part 1** — what changed between as-code **3.0** and **3.1**, grouped by functional area.
- **Part 2** — what a NeoLoad GUI project (`.nlp` XML) can express that as-code **cannot** yet,
  grouped by functional area. This is the backlog to prioritise for 3.2.

Keep it up to date with every DSL change: a feature that is not listed here is invisible to the
people who plan the next schema version.

## Reference points

| Name | Meaning | Supported by | Reference |
|:-----|:--------|:-------------|:----------|
| as-code **3.0** | DSL as shipped up to NeoLoad 2026.2 | all NLG and all NLW versions | `neoload-models` release **3.3.7** (`dbf642b9`) |
| as-code **3.1** | DSL as shipped with NeoLoad 2026.3 | NLG `>= 2026.3`, NLW `>= 2026.3` | `neoload-models` release **3.4.6** (`4493c0f3`), branch `v3` |
| **gap closure** | Merged in neither of the above, in flight | not released | branch `featuregroup/as-code-gap-closure` |
| **NLP** | NeoLoad GUI project, default XML format | - | `neoload-root`, branch `develop` |

The **Supported by** column is a summary of `schemas/compatibility.json`, which is the source of
truth and states the constraint per product:

| Contract | NeoLoad (NLG) | NeoLoad CheckVU CLI | NeoLoad Web On-premise | NeoLoad Web SaaS |
|:---------|:--------------|:--------------------|:-----------------------|:-----------------|
| 3.0 | all | all | all | all |
| 3.1 | `>= 2026.3` | `>= 2026.3` | `>= 2026.3` | all (always on the current version) |

Scope for the NLP comparison: the **Design** and **Runtime** tabs of NeoLoad GUI. The **Results**
tab is out of scope.

`schemaVersion` declares which contract a file targets. It does **not** gate the parser: the model
accepts every construct it knows about regardless of the declared version, and only checks that the
declared version is one this build knows (`supported-schemas.json`). Version gating happens in the
published JSON Schemas (`schemas/v3.0/`, `schemas/v3.1/`), which editors use.

---

# Part 1 — Changelog from as-code 3.0 to 3.1

## Schema contract

- **`schemaVersion`** — new optional root key declaring the schema contract the file targets.
  Defaults to `3.0` when absent. Validated against the versions this build supports.
- **`$schema`** — root key holding the URL of the JSON Schema, for editor validation.
- **Published schemas** — `schemas/v3.0/as-code.schema.json` and `schemas/v3.1/as-code.schema.json`
  at the repository root, plus `schemas/compatibility.json` describing which product version
  accepts which contract (3.1 requires NeoLoad, CheckVU CLI and NeoLoad Web On-premise `>= 2026.3`).
- **`supported-schemas.json`** — resource embedded in the build, listing the contracts this
  `neoload-models` can parse.

## User Path steps

Six logical actions became available in as-code. All of them already existed in the GUI.

| Step | Description |
|:-----|:------------|
| [`try_catch`](neoload-project/doc/v3/try_catch.md) | Runs the `try` container and, when a caught exception type occurs, the `catch` container. `caught_exceptions` selects `errors`, `assertions` or `all`. |
| [`fork`](neoload-project/doc/v3/fork.md) | Runs `steps` in a thread parallel to the Virtual User main chain. `copy_variables` isolates variable values from the other threads. |
| [`variable_modifier`](neoload-project/doc/v3/variable_modifier.md) | Changes a variable value outside its change policy. `category: predefined` supports `next_value` / `init_value`; `category: shared_queue` supports `add_shared_queue_value` / `poll_shared_queue`. |
| [`go_to_next_iteration`](neoload-project/doc/v3/go_to_next_iteration.md) | Interrupts the current iteration. Serialised as a bare scalar since it has no property. |
| [`debug_logger`](neoload-project/doc/v3/debug_logger.md) | Writes `text` to a log file, `logs/runTimeLog.txt` by default. |
| [`stop_vu`](neoload-project/doc/v3/stop_vu.md) | Stops the current Virtual User. `start_new_vu` (default `true`) controls whether a replacement is started. Serialised as a bare scalar when it keeps the default. |

## Variables

Four variable types became available in as-code. All of them already existed in the GUI.

| Type | Description |
|:-----|:------------|
| `list` | Table of values written inline in the YAML: `column_names` + `values`, plus `start_from_line`, `change_policy`, `scope`, `order`, `out_of_value`. |
| `random_string` | Random alphanumeric string. `min_length` (5), `max_length` (10), `predictable` (`false`). |
| `random_uuid` | Random UUID. `upper_case` (`false`), `predictable` (`false`). |
| `shared_queue` | Producer/consumer queue shared between Virtual Users. `queue_size` (10000), `consumer_timeout` (5000 ms) and a nested `swap_file` (`path`, `delimiter`, `load_from_file`, `save_to_file`) for persistence. |

**Behaviour change — variable traits are now per type.** In 3.0, `change_policy`, `scope`, `order`
and `out_of_value` were declared on the common `Variable` base and were therefore accepted on every
variable type, including the ones they do not apply to. In 3.1 they moved to dedicated interfaces
and each type only exposes the ones that make sense:

| Type | `change_policy` | `scope` | `order` | `out_of_value` |
|:-----|:---------------:|:-------:|:-------:|:--------------:|
| `constant` | - | - | - | - |
| `file` | yes | yes | yes | yes |
| `list` | yes | yes | yes | yes |
| `counter` | yes | yes | - | yes |
| `random_number` | yes | - | - | - |
| `random_string` | yes | - | - | - |
| `random_uuid` | yes | - | - | - |
| `javascript` | yes | - | - | - |
| `shared_queue` | - | - | - | - |

## Naming and validation

- **Project name** — only identifier characters (letters, digits, `$`, `_`), at most 100 characters.
  Mirrors the GUI rule, with an added length cap.
- **Element names** (populations, scenarios, User Paths, servers, variables, SLA profiles, and so
  on) — at most 100 characters, and the forbidden character set listed in
  [the naming rules](neoload-project/doc/v3/README.md#naming-rules) is rejected. Mirrors the GUI
  `NameValidator`, with an added length cap.

## Reverted before release

- **`rendezvous` step** — implemented under LOAD-38588 and reverted in `4239a72c` before 3.4.6.
  It is **not** part of 3.1. The scenario-level
  [`rendezvous_policies`](neoload-project/doc/v3/rendezvous_policy.md), which already existed in
  3.0, is unaffected.

## Known inconsistencies in 3.1

These are real defects in the shipped 3.1 surface. They should be fixed before or with 3.2.

- **`schemas/v3.1/as-code.schema.json` does not describe the 3.1 features.** It was branched before
  the 2026.3 work landed, so `try_catch`, `fork`, `variable_modifier`, `go_to_next_iteration`,
  `debug_logger`, `stop_vu`, `list`, `random_string`, `random_uuid` and `shared_queue` are absent
  from it. The runtime copy `neoload-project/src/main/resources/as-code.latest.schema.json` is the
  one that reflects the model. An editor validating against the published v3.1 schema rejects
  valid 3.1 files.
- **`request.followRedirects` and `request.bodybinary`** are accepted by the model but are declared
  in no published schema.
- **NeoLoad GUI does not consume 3.1 yet.** `neoload-root` `develop` pins
  `neoload-models.version = 3.4.5`, which predates all the features above. Its as-code readers
  (`com.neotys.nl.api.converter.v3.functions.StepConverter` and `VariableConverter`) handle only
  the 3.0 steps (container, request, delay, think_time, javascript, if, loop, while, switch,
  custom_action) and five variable types (constant, file, counter, random_number, javascript), and
  throw `IllegalStateException` on anything else. Its exporter
  (`com.neotys.nl.api.converter.v3.toascode.ActionConverter`) covers even less. Bumping
  `neoload-models` to 3.4.6 and extending both converters is a prerequisite for 3.1 to be usable
  end to end from the GUI.

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

---

# Part 2 — Backlog: NLP features not available in as-code

Legend: **3.0** / **3.1** = available in that contract; **gap** = implemented on
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
