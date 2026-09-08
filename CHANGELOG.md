# NeoLoad as-code — changelog

All notable changes to the **released** NeoLoad as-code (YAML/JSON) DSL, newest first. Work still on
a feature branch does not belong here; it is tracked in
[FEATURE-COVERAGE.md](FEATURE-COVERAGE.md), which also holds the feature-by-feature comparison
against a NeoLoad GUI `.nlp` project.

---

## [3.1] — Upcoming version in 2026

Shipped in `neoload-models` **x.x.x**. Requires **NeoLoad 2026.3**, **CheckVU CLI 2026.3** and
**NeoLoad Web On-premise 2026.3** or later; NeoLoad Web SaaS accepts it as soon as it is deployed.
Declare it with `schemaVersion: "3.1"`.

A file without `schemaVersion` is still read as 3.0, so **existing projects keep working
unchanged**.

### Added

#### Schema contract

The root of an as-code file now takes two optional keys. `schemaVersion` declares the contract the
file targets, so a product can tell you up front that it is too old to read it. `$schema` points at
the published JSON Schema so editors validate and autocomplete as you type.

```yaml
$schema: https://raw.githubusercontent.com/Neotys-Labs/neoload-models/refs/heads/v3/schemas/v3.1/as-code.schema.json
schemaVersion: "3.1"
name: MyProject
```

The schemas are published at the repository root — `schemas/v3.0/as-code.schema.json` and
`schemas/v3.1/as-code.schema.json` — next to `schemas/compatibility.json`, which states the
product version each contract needs.

#### Steps

Six logical actions that already existed in the NeoLoad GUI are now writable in as-code.

**`try_catch`** — runs the `try` container and, when a caught exception occurs, the `catch`
container instead. `caught_exceptions` selects `errors` (default), `assertions` or `all`.

```yaml
- try_catch:
    caught_exceptions: all
    try:
      steps:
      - request:
          url: https://www.tricentis.com/
    catch:
      steps:
      - delay: 1s
```

**`fork`** — runs steps in a thread parallel to the Virtual User main chain. `copy_variables`
isolates the variable values from the other threads.

```yaml
- fork:
    copy_variables: true
    steps:
    - request:
        url: https://www.tricentis.com/
```

**`variable_modifier`** — changes a variable value outside of its change policy. With
`category: predefined` it takes `next_value` or `init_value`; with `category: shared_queue` it
takes `add_shared_queue_value` or `poll_shared_queue`.

```yaml
- variable_modifier:
    variable_name: MyVariableToModify     # predefined, next_value

- variable_modifier:
    category: shared_queue
    mode: add_shared_queue_value
    variable_name: MySharedQueue
    value: ${MyVariable}
```

**`go_to_next_iteration`** — interrupts the current iteration. It has no property, so it is written
as a bare scalar.

```yaml
- go_to_next_iteration
```

**`stop_vu`** — stops the current Virtual User. By default a replacement is started so the running
Virtual User count stays stable; set `start_new_vu: false` to stop without a replacement.

```yaml
- stop_vu

- stop_vu:
    start_new_vu: false
```

**`debug_logger`** — writes a line to a log file, `logs/runTimeLog.txt` unless `file` says
otherwise.

```yaml
- debug_logger:
    text: "Current user: ${user_id}"
    file: logs/custom.txt
```

#### Variables

Four variable types that already existed in the NeoLoad GUI are now writable in as-code.

**`list`** — a table of values written inline in the YAML, with the same distribution options as a
file variable.

```yaml
- list:
    name: cities_list
    column_names: ["City", "Country"]
    values:
    - ["Paris", "France"]
    - ["London", "UK"]
    change_policy: each_iteration
    order: random
```

**`random_string`** — a random alphanumeric string whose length falls in a range.

```yaml
- random_string:
    name: random_string_variable
    min_length: 10
    max_length: 20
```

**`random_uuid`** — a random UUID, optionally uppercase.

```yaml
- random_uuid:
    name: random_uuid_variable
    upper_case: true
```

**`shared_queue`** — a producer/consumer queue shared between Virtual Users, driven by the
`variable_modifier` step, with optional persistence to a swap file.

```yaml
- shared_queue:
    name: MySharedQueue
    queue_size: 5000
    consumer_timeout: 2000
    swap_file:
      path: data/my_queue.csv
      delimiter: ","
      load_from_file: true
      save_to_file: false
```

#### Validation

Names are now validated the way the GUI validates them, so a project that as-code accepts is a
project the GUI accepts.

- **Project name** — identifier characters only (letters, digits, `$`, `_`), 100 characters
  maximum.
- **Element names** (populations, scenarios, User Paths, servers, variables, SLA profiles, and so
  on) — 100 characters maximum, and the forbidden character set is rejected. See
  [the naming rules](neoload-project/doc/v3/README.md#naming-rules).

### Changed

- **Variable options are now restricted to the types they apply to.** In 3.0, `change_policy`,
  `scope`, `order` and `out_of_value` were declared on the shared variable base and were therefore
  accepted on every variable type, including the ones that ignore them. Each type now exposes only
  the ones that mean something:

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

- **Written files no longer repeat default values.** Exporting a project to YAML or JSON now omits
  every property left at its default, so generated files are much shorter and a round trip no
  longer inflates them.

### Fixed

- The JSON Schema restricted `method` to a fixed enum and therefore rejected a custom HTTP method,
  which the model has always accepted. It now accepts any string, with `GET` as the default.

---

## [3.0] — up to NeoLoad 2026.2

The initial as-code contract, shipped up to and including `neoload-models` 3.3.7. Accepted by every
NeoLoad, CheckVU CLI and NeoLoad Web version. This is the contract assumed when a file declares no
`schemaVersion`.

Surface, for reference:

- **Project** — `name`, `includes`, `project_settings` (Dynatrace and qTest integration keys).
- **Servers** — `host`, `port`, `scheme`, plus Basic, NTLM and Negotiate authentication.
- **Variables** — `constant`, `file`, `counter`, `random_number`, `javascript`.
- **User Paths** — `init` / `actions` / `end` containers, `user_session` reset policy.
- **Steps** — `transaction`, `request`, `delay`, `think_time`, `javascript`, `if`, `loop`, `while`,
  `switch`, `custom_action`.
- **Requests** — URL, server, method, headers, body, `extractors`, `assertions`, `sla_profile`.
- **Populations** — User Path list with a `distribution` percentage.
- **Scenarios** — constant, ramp-up, peaks and custom load policies, `start_after` / `stop_after` /
  `rampup`, `monitoring`, `rendezvous_policies`, `excluded_urls`, `store_variables_for_raw_data`,
  `apm_configuration` for Dynatrace.
- **SLA** — profiles and KPI thresholds, `per test` and `per interval`.

Full reference: [neoload-project/doc/v3](neoload-project/doc/v3/README.md).
