# Fork

The Fork action is used to play actions in a different execution thread to the current one. This execution thread is parallel to the main chain used by the Virtual User. When the current iteration of the Virtual User stops, all the threads created using Fork actions in the Actions Container are immediately halted.

#### Available settings
| Name              | Description                                                                                        | Accept variable | Required           | Since |
|:----------------- |:-------------------------------------------------------------------------------------------------- |:---------------:|:------------------:|:-----:|
| name              | The name of the Fork                                                                               | -               | -                  | -     |
| description       | The description of the Fork                                                                        | -               | -                  | -     |
| copy_variables    | When `true`, existing values of variables are copied locally so that modifications in another thread do not affect this Fork. Default is `false`. | - | - | - |
| [steps](steps.md) | The steps to be executed in the parallel thread                                                    | -               | &#x2713;           | -     |


#### Example
Execute a request in a parallel thread while the Virtual User continues its main execution.
```yaml
  actions:
    steps:
    - fork:
        name: My Fork
        description: My description
        copy_variables: true
        steps:
        - request:
            url: https://www.tricentis.com/
```
