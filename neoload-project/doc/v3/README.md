See [Project](project.md)

See the [changelog](../../../CHANGELOG.md) for what is new in each as-code version, and the
[feature coverage](../../../FEATURE-COVERAGE.md) for the list of NeoLoad GUI features that as-code
cannot express yet.

## Naming rules

- **Project name** (`name` at the root of the as-code file): only letters, digits, `$` or `_`, up to 100 characters.
- **Element names** (`name` of populations, scenarios, user paths, servers, variables, SLA profiles, thresholds, etc.): any characters except the following are allowed, up to 100 characters: `£` `€` `$` `"` `[` `]` `<` `>` `|` `*` `¤` `?` `§` `µ` `#` (backtick) `@` `^` `²` `°` `¨` `\`.
