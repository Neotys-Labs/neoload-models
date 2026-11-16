# neoload-models

Java model, JSON schema and docs of the NeoLoad as-code YAML DSL. Consumed by `neoload-root` (NLGui converters YAML ⇄ legacy) through the `neoload-models.version` Maven property.

- Default branch: `v3`.

## Scope

| Area | Rule |
|---|---|
| Modules | Change `neoload-project` only. Touch `neoload-writer`, `models-readers`, `neoload-project-model` only when they stop compiling because of a `neoload-project` change. |
| JSON schema | Change `schemas/v3.1/as-code.schema.json` only. Older schemas are frozen. |
| Docs | `neoload-project/doc/v3/*.md` is updated in the same change as the model and the schema. |
| Object references | Cross-object checks (a population policy naming a population, a shared element reference…) live in `neoload-root`, after NLP + YAML files are merged into one project. This repo validates each element on its own. |

## Contract rules

- **Defaults live in the as-code model.** Declare every default here, from the runtime behaviour, not from NLGui widgets (some GUI defaults are only set when a screen loads). Converters in `neoload-root` take the model value as-is.
- **Defaults are never written.** A value left at its default stays absent after read + write: `false` by default ⇒ written only once the user sets `true`.
- **Backward compatible.** Every YAML accepted before stays accepted. Renamed keys stay readable as deprecated aliases (`binding/io/DeprecatedKeys.java`); old and new key together is a validation error; write emits the new key.
- **Schema parity.** The Java code does not read the JSON schema, so the schema re-implements the Java validation rules. Rules too complex for JSON schema stay Java-only.
- **Stricter than the GUI.** NLGui often validates nothing; as-code rejects values the runtime would ignore or misread.

## Tests

Every validation made in this repo is tested in this repo, by loading real YAML files from `neoload-project/src/test/resources/`:

- Nominal and every error case: one YAML fixture each (plus its `.json` twin for the schema tests).
- Read then write gives back the same YAML (`IORoundTripTest`, `XXXRoundTripTest`).
- Defaults are not written back: a fixture with defaults omitted round-trips unchanged.
- Voluntarily invalid fixtures go in `IOSchema3Dot1ValidationTest.EXCLUDED_FIXTURES`.
- Assert inside the test method: Sonar does not see assertions hidden in helpers.
