# NeoLoad As-Code — Plan d'implémentation des types de variables manquants

**Epic:** LOAD-36914
**Date:** 2026-04-08

---

## Vue d'ensemble

5 types de variables existent dans le designer NeoLoad mais n'ont aucune représentation dans le DSL YAML as-code. Chaque type nécessite des modifications dans **neoload-models** et **neoload-root**.

### Pattern d'implémentation (par type)

**neoload-models** (4 fichiers) :
1. Créer l'interface dans `neoload-project/src/main/java/.../project/variable/XxxVariable.java`
2. Enregistrer dans `Variable.java` → ajouter `@JsonSubTypes.Type`
3. Mettre à jour `as-code.latest.schema.json` → `$ref` dans la liste + définition `allOf` avec `generic`
4. Tests : IO test + fichiers YAML/JSON de test

**neoload-root** (2 fichiers) :
5. Ajouter le bloc `instanceof XxxVariable` dans `VariableConverter.java`
6. Test du converter

**Référence :** `RandomNumberVariable.java` + son entrée schema + `IOVariableTest.java`

---

## 1. random_string (LOAD-36951) — Simple

**Classe designer :** `RandomStringVariable.java`
- `min_length` (int, requis) — XML: `min-lenght` (typo historique dans le code)
- `max_length` (int, requis) — XML: `max-lenght` (typo historique)
- `predictable` (boolean, optionnel, défaut false) — XML: `random-seed`

**YAML cible :**
```yaml
- random_string:
    name: my_random_string
    min_length: 5
    max_length: 20
    predictable: false
    change_policy: each_use
```

**Interface :**
```java
public interface RandomStringVariable extends Variable {
    String MIN_LENGTH = "min_length";
    String MAX_LENGTH = "max_length";
    String PREDICTABLE = "predictable";

    @JsonProperty(MIN_LENGTH) @RequiredCheck(groups={NeoLoad.class}) int getMinLength();
    @JsonProperty(MAX_LENGTH) @RequiredCheck(groups={NeoLoad.class}) int getMaxLength();
    @JsonProperty(PREDICTABLE) @Value.Default default boolean isPredictable() { return false; }
}
```

**Schema :**
```json
"random_string": {
    "$id": "#/definitions/variables/random_string",
    "title": "Random String",
    "type": "object",
    "xrequired": ["min_length", "max_length"],
    "allOf": [
        { "$ref": "#/definitions/variables/generic" },
        {
            "properties": {
                "min_length": { "type": "number" },
                "max_length": { "type": "number" },
                "predictable": { "type": "boolean" }
            }
        }
    ],
    "x-d7nosupport-additionalProperties": false
}
```

**Converter :** `RandomStringVariable` du designer → `setMinLenght()`, `setMaxLenght()`, `setSeedProvided()`

---

## 2. password (LOAD-36952) — Simple

**Classe designer :** `PasswordVariable.java` (extends `ConstantVariable`)
- `value` (String, requis) — chiffré avec `PBECryptProject.encode()`

**YAML cible :**
```yaml
- password:
    name: my_password
    value: "s3cr3t"
```

**Interface :**
```java
public interface PasswordVariable extends Variable {
    String VALUE = "value";

    @JsonProperty(VALUE) @RequiredCheck(groups={NeoLoad.class}) String getValue();
}
```

**Schema :**
```json
"password": {
    "$id": "#/definitions/variables/password",
    "title": "Password",
    "type": "object",
    "xrequired": ["value"],
    "allOf": [
        { "$ref": "#/definitions/variables/generic" },
        {
            "properties": {
                "value": { "$ref": "#/definitions/common/text" }
            }
        }
    ],
    "x-d7nosupport-additionalProperties": false
}
```

**Converter :** Instancier `PasswordVariable` designer, la valeur sera chiffrée au `writeXML`.

**Attention :** Le chiffrement utilise une clé hardcodée `PBECryptProject.encode(decode, PASSWORD_KEY)`. La valeur en YAML est en clair, chiffrée seulement lors de la conversion interne.

---

## 3. date (LOAD-36950) — Moyenne

**Classe designer :** `DateVariable.java`
- `pattern` (String, requis) — format Java SimpleDateFormat
- `start_date` (String, optionnel) — date de départ formatée selon le pattern
- `inc_type` (int, optionnel) — 0=second, 1=minute, 2=hour, 3=day, 4=month, 5=year
- `inc_value` (int, optionnel) — incrément entre itérations

**YAML cible :**
```yaml
- date:
    name: my_date
    pattern: "yyyy-MM-dd HH:mm:ss"
    start_date: "2026-01-01 00:00:00"
    inc_type: day
    inc_value: 1
    change_policy: each_iteration
```

**Choix de design pour `inc_type` :** Utiliser un enum lisible (`second`, `minute`, `hour`, `day`, `month`, `year`) plutôt que les entiers 0-5 du designer. La conversion vers int se fait dans le converter.

**Interface :**
```java
public interface DateVariable extends Variable {
    String PATTERN = "pattern";
    String START_DATE = "start_date";
    String INC_TYPE = "inc_type";
    String INC_VALUE = "inc_value";

    enum IncType {
        @JsonProperty("second") SECOND,
        @JsonProperty("minute") MINUTE,
        @JsonProperty("hour") HOUR,
        @JsonProperty("day") DAY,
        @JsonProperty("month") MONTH,
        @JsonProperty("year") YEAR
    }

    @JsonProperty(PATTERN) @RequiredCheck(groups={NeoLoad.class}) String getPattern();
    @JsonProperty(START_DATE) Optional<String> getStartDate();
    @JsonProperty(INC_TYPE) @Value.Default default IncType getIncType() { return IncType.DAY; }
    @JsonProperty(INC_VALUE) @Value.Default default int getIncValue() { return 1; }
}
```

**Schema :**
```json
"date": {
    "$id": "#/definitions/variables/date",
    "title": "Date",
    "type": "object",
    "xrequired": ["pattern"],
    "allOf": [
        { "$ref": "#/definitions/variables/generic" },
        {
            "properties": {
                "pattern": { "$ref": "#/definitions/common/text" },
                "start_date": { "$ref": "#/definitions/common/text" },
                "inc_type": {
                    "type": "string",
                    "enum": ["second", "minute", "hour", "day", "month", "year"],
                    "default": "day"
                },
                "inc_value": { "type": "number", "default": 1 }
            }
        }
    ],
    "x-d7nosupport-additionalProperties": false
}
```

**Converter :** `IncType.ordinal()` → int pour `setIncType()`. Parser `start_date` avec `SimpleDateFormat(pattern)`.

---

## 4. list (LOAD-36949) — Moyenne

**Classe designer :** `ListVariable.java` (extends `MultiValuedVariable`)
- `values` (String[][], requis) — tableau 2D de valeurs
- Hérite des column names et des propriétés communes

**YAML cible :**
```yaml
- list:
    name: my_list
    column_names: ["city", "country"]
    values:
    - ["Paris", "France"]
    - ["London", "UK"]
    - ["Berlin", "Germany"]
    change_policy: each_iteration
    scope: global
    order: sequential
    out_of_value: cycle
```

**Interface :**
```java
public interface ListVariable extends Variable {
    String COLUMN_NAMES = "column_names";
    String VALUES = "values";

    @JsonProperty(COLUMN_NAMES) List<String> getColumnNames();
    @JsonProperty(VALUES) @RequiredCheck(groups={NeoLoad.class}) List<List<String>> getValues();
}
```

**Schema :**
```json
"list": {
    "$id": "#/definitions/variables/list",
    "title": "List",
    "type": "object",
    "xrequired": ["values"],
    "allOf": [
        { "$ref": "#/definitions/variables/generic" },
        {
            "properties": {
                "column_names": {
                    "type": "array",
                    "items": { "type": "string" }
                },
                "values": {
                    "type": "array",
                    "items": {
                        "type": "array",
                        "items": { "type": "string" }
                    }
                }
            }
        }
    ],
    "x-d7nosupport-additionalProperties": false
}
```

**Converter :** `List<List<String>>` → `String[][]`, puis `new ListVariable(name, values, order)`. Gérer les `column_names`.

---

## 5. sql (LOAD-36953) — Complexe

**Classe designer :** `SqlVariable.java` (extends `FileVariable`)
- `query` (String, requis)
- Connection via `SqlDatabaseConnection` :
  - `driver` (String) — ex: `com.mysql.jdbc.Driver`
  - `url` (String) — ex: `jdbc:mysql://localhost:3306/mydb` (mode manuel)
  - Ou bien : `host`, `port`, `name`, `protocol` (mode décomposé)
  - `login` (String)
  - `password` (String) — chiffré

**YAML cible :**
```yaml
- sql:
    name: my_sql_var
    query: "SELECT username, email FROM users"
    driver: "com.mysql.jdbc.Driver"
    url: "jdbc:mysql://localhost:3306/mydb"
    login: admin
    password: pass
    column_names: ["username", "email"]
    change_policy: each_iteration
    scope: global
    order: sequential
    out_of_value: cycle
```

**Choix de design :** Mode `url` manuel uniquement (pas de décomposition host/port/name/protocol). C'est plus simple et couvre le cas d'usage principal.

**Interface :**
```java
public interface SqlVariable extends Variable {
    String QUERY = "query";
    String DRIVER = "driver";
    String URL = "url";
    String LOGIN = "login";
    String PASSWORD = "password";
    String COLUMN_NAMES = "column_names";

    @JsonProperty(QUERY) @RequiredCheck(groups={NeoLoad.class}) String getQuery();
    @JsonProperty(DRIVER) @RequiredCheck(groups={NeoLoad.class}) String getDriver();
    @JsonProperty(URL) @RequiredCheck(groups={NeoLoad.class}) String getUrl();
    @JsonProperty(LOGIN) Optional<String> getLogin();
    @JsonProperty(PASSWORD) Optional<String> getPassword();
    @JsonProperty(COLUMN_NAMES) List<String> getColumnNames();
}
```

**Schema :**
```json
"sql": {
    "$id": "#/definitions/variables/sql",
    "title": "SQL",
    "type": "object",
    "xrequired": ["query", "driver", "url"],
    "allOf": [
        { "$ref": "#/definitions/variables/generic" },
        {
            "properties": {
                "query": { "$ref": "#/definitions/common/text" },
                "driver": { "$ref": "#/definitions/common/text" },
                "url": { "$ref": "#/definitions/common/text" },
                "login": { "$ref": "#/definitions/common/text" },
                "password": { "$ref": "#/definitions/common/text" },
                "column_names": {
                    "type": "array",
                    "items": { "type": "string" }
                }
            }
        }
    ],
    "x-d7nosupport-additionalProperties": false
}
```

**Converter :** Instancier `SqlVariable` + `SqlDatabaseConnection` en mode manuel (`manualMode=true`, `manualUrl=url`). Gérer le chiffrement du password via `PBECryptProject`.

---

## Ordre d'implémentation recommandé

1. **random_string** (LOAD-36951) — le plus simple, pattern identique à `random_number`
2. **password** (LOAD-36952) — simple aussi, pattern identique à `constant`
3. **date** (LOAD-36950) — enum `inc_type` à mapper
4. **list** (LOAD-36949) — tableau 2D à gérer
5. **sql** (LOAD-36953) — le plus complexe (connexion DB, chiffrement password)

---

## Fichiers impactés (récap)

### neoload-models

| Fichier | Action |
|---------|--------|
| `variable/RandomStringVariable.java` | Créer |
| `variable/PasswordVariable.java` | Créer |
| `variable/DateVariable.java` | Créer |
| `variable/ListVariable.java` | Créer |
| `variable/SqlVariable.java` | Créer |
| `variable/Variable.java` | Modifier (5 `@JsonSubTypes.Type`) |
| `as-code.latest.schema.json` | Modifier (5 `$ref` + 5 définitions) |
| `test-variable-only-required.yaml` | Modifier (ajouter les 5 types) |
| `test-variable-only-required.json` | Modifier |
| `IOVariableTest.java` | Modifier (ajouter les 5 builders) |

### neoload-root

| Fichier | Action |
|---------|--------|
| `VariableConverter.java` | Modifier (5 blocs `instanceof`) |
| Tests converter | Ajouter |