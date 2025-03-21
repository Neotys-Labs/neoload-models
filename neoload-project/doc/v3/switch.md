# Switch

The Switch action is used to execute conditional actions based on a selection control mechanism. It allows the Switch value to change the control flow of the User Path execution via a multiway branch, named Case statements.
The Switch action is fall-through, which means that if a Break flag is disabled on a Case, then the next Case statement is executed.

Switches are supported in NeoLoad from version 2025.1 onwards.

#### Available settings

| Name          | Description                                     | Accept variable | Required | Since |
|:--------------|:------------------------------------------------|:---------------:|:--------:|:-----:|
| name          | The name of the Switch                          |        -        |    -     |   -   |
| description   | The description of the Switch                   |        -        |    -     |   -   |
| value         | The value of the Switch                         |    &#x2713;     | &#x2713; |   -   |
| [case](#case) | The case statement list                         |        -        |    -     |   -   |
| default       | The default statement [container](container.md) |        -        | &#x2713; |   -   |

## case

| Name              | Description                 | Accept variable | Required | Since |
|:------------------|:----------------------------|:---------------:|:--------:|:-----:|
| value             | The value of the case       |        -        | &#x2713; |       |
| break             | Enable the break            |        -        |          |       |
| description       | The description of the case |        -        |    -     |       |
| [steps](steps.md) | The steps to be executed    |        -        | &#x2713; |       |

#### Example
A switch with 2 cases:
- the first case has the break so only the page1 is executed
- the second case has no break so the default statement will be executed as well

```yaml
  actions:
    steps:
    - switch:
       name: MySwitch
       value: ${pageNumber}
       case:
       - value: '0'
         break: true
         steps:
         - request:
            url: http://${var_host}:${var_port}/page0
         - delay: 3ms
       - value: '1'
         steps:
         - request:
            url: http://${var_host}:${var_port}/page1
         - delay: 6ms
       default:
         steps:
         - delay: 100ms
```