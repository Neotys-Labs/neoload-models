# Custom Action

Custom action can be an Advanced Action, a SAP GUI Action, a Citrix Action, a RealBrowser Action or an RTE Action.
A Custom Advanced action can also be developed using Java.

#### Available settings are

| Name          | Description                                                                         | Accept variable | Required | Since |
|:--------------|:------------------------------------------------------------------------------------|:---------------:|:--------:|:-----:|
| name          | The name of the Custom Action                                                       |        -        | &#x2713; |       |
| description   | The description of the Custom Action                                                |        -        |    -     |       |
| [type](#type) | The type of the Action                                                              |        -        | &#x2713; |       |
| parameters    | The action parameter list                                                           |    &#x2713;     |    -     |       |
| asRequest     | Consider the executions as requests in the calculation of the statistics and graphs |        -        |    -     |       |
| libraryPath   | The path to the JAR file containing the Custom Advanced Action                      |        -        |    -     |       |

## type

The type of the action is an internal value, not displayed in NeoLoad GUI, used to identify the action.
This can be an Advanced Action:
- SQL Connection
- SQL
- SQL Disconnection
- Command line Action
- KafkaSendAction
- KafkaReceiveAction
- ...

A SAP GUI Action:
- SapConnect
- SapClick
- SapSetText
- ...

A Citrix Action:
- CitrixConnect
- CitrixMouseClick
- CitrixKeyType
- CitrixDisconnect
- ...

A RealBrowser Action:
- BrowserOpen
- BrowserClick
- BrowserType
- BrowserClose
- ...

An RTE Action:
- RTEConnect
- RTESendKey
- RTERead
- DisconnectAction

This can also be the type of Custom Advanced Action, developed using Java.


#### Example

Defining a SQL Advanced action.

```yaml
- custom_action:
    name: sql action
    type: SQL
    parameters:
      - name: connectionURL
        value: jdbc:mysql://localhost:3306/
      - name: connection.user
        value: admin
      - name: connection.password
        value: myPassword
        type: PASSWORD
      - name: type
        value: QUERY
      - name: sqlStatement
        value: select * from table
```



