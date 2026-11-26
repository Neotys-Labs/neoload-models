See [Project](project.md)

See the [changelog](../../../CHANGELOG.md) for what is new in each as-code version, and the
[feature coverage](../../../FEATURE-COVERAGE.md) for the list of NeoLoad GUI features that as-code
cannot express yet.

## Naming rules

- **Project name** (`name` at the root of the as-code file): only letters, digits, `$` or `_`, up to 100 characters.
- **Unique names**: within each list of the project (`servers`, `variables`, `user_paths`, `populations`, `scenarios`, `sla_profiles`, `shared_elements`, the `populations` of a scenario, the Web Services Security profiles), names must be unique. A duplicate name is rejected when the project is loaded.
- **Element names** (`name` of populations, scenarios, user paths, servers, variables, SLA profiles, thresholds, etc.): any characters except the following are allowed, up to 100 characters: `£` `€` `$` `"` `[` `]` `<` `>` `|` `*` `¤` `?` `§` `µ` `#` (backtick) `@` `^` `²` `°` `¨` `\`.
