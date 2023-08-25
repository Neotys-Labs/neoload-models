# Loop

A repetition structure which executes steps sequentially a specific number of times. 

#### Available settings
| Name              | Description                                                                                        | Accept variable | Required           | Since |
|:----------------- |:-------------------------------------------------------------------------------------------------- |:---------------:|:------------------:|:-----:|
| name              | The name of the Loop                                                                               | -               | -                  | 7.2   |
| description       | The description of the Loop                                                                        | -               | -                  | 7.2   |
| count             | The number of times to execute                                                                     | -               | &#x2713;           | 7.2   |
| [steps](steps.md) | The steps to be executed                                                                           | -               | &#x2713;           | 7.2   |


#### Example
Execute a request three times.
```yaml
  actions:
    steps:
    - loop:
        name: My Loop
        description: My description
        count: 3
        steps:
        - request:
          url: https://www.tricentis.com/
```