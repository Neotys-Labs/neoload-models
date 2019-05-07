# Include
A YAML file can include other YAML files. Included YAML files must declare top level [Project](project.md) elements such as SLA profiles, variables, servers, User Paths, Populations and Scenarios as for separate YAML files.

Each `include` element defines a relative path from the project folder. 

The included YAML files are loaded first. The top level [Project](project.md) elements are then loaded.


#### Example

Here is an example of project folder containing YAML files for two different execution environments:

```
common/populations.yaml
common/scenarios.yaml
common/user_paths.yaml
preprod/servers.yaml
preprod/slas.yaml
preprod/variables.yaml
staging/servers.yaml
staging/slas.yaml
staging/variables.yaml
preprod.yaml
staging.yaml
```

Defining the content of the `preprod.yaml` file:
```yaml
name: preprod
includes:
- preprod/slas.yaml
- preprod/variables.yaml
- preprod/servers.yaml
- common/user_paths.yaml
- common/populations.yaml
- common/scenarios.yaml
```

Defining the content of the `staging.yaml` file:
```yaml
name: staging
includes:
- staging/slas.yaml
- staging/variables.yaml
- staging/servers.yaml
- common/user_paths.yaml
- common/populations.yaml
- common/scenarios.yaml
```

