# Custom Action

Custom action can be an Advanced Action, a SAP GUI Action, a Citrix Action, a RealBrowser Action or an RTE Action.
A Custom Advanced action can also be developed using Java.

Custom actions are supported in NeoLoad from version 2025.1 onwards.

#### Available settings are

| Name          | Description                                                                         | Accept variable | Required | Since |
|:--------------|:------------------------------------------------------------------------------------|:---------------:|:--------:|:-----:|
| name          | The name of the Custom Action                                                       |        -        | &#x2713; |       |
| description   | The description of the Custom Action                                                |        -        |    -     |       |
| [type](#type) | The type of the Action                                                              |        -        | &#x2713; |       |
| parameters    | The action parameter list                                                           |    &#x2713;     |    -     |       |
| asRequest     | Consider the executions as requests in the calculation of the statistics and graphs |        -        |    -     |       |
| libraryPath   | The path to the JAR file containing the Custom Advanced Action                      |        -        |    -     |       |
| [size_assertion](size_assertion.md) | The assertion to validate the response size                   |        -        |    -     | 2026.3 |

## type

The type of the action is an internal value, not displayed in NeoLoad GUI, used to identify the action.

This can be an Advanced Action:
- SQL Connection
- SQL
- SQL Disconnection
- Executable Test Script Action
- Java Test Script Action
- Command line Action
- Java Action
- amqp-close-channel
- amqp-create-channel
- amqp-connect
- amqp-disconnect
- amqp-declare-exchange
- amqp-delete-exchange
- amqp-publish
- amqp-consume
- amqp-declare-queue
- amqp-delete-queue
- Connect
- Disconnect
- Receive from Queue
- Send To Queue
- Send And Receive
- Publish to Topic
- Receive from Topic
- Subscribe To Topic
- Unsubscribe From Topic
- KafkaReceiveAction
- KafkaSendAction
- MQTT Connect
- MQTT Disconnect
- MQTT publish
- MQTT receive messages on a topic
- MQTT subscribe to topic
- MQTT unsubscribe
- CustomMeasurementAction
- Store External Data Entries
- Store External Data Entry

A SAP GUI Action:
- SapClick
- SapClose 
- SapCloseAllSessions
- SapCollapse
- SapConnect
- SapDoubleClick
- SapEvent
- SapExpand
- SapIsAvailable
- SapIsChangeable
- SapIsSelected
- SapPress
- SapRead
- SapResize
- SapRightClick
- SapSelect
- SapFocus
- SapSetText
- SapUnselect

A Citrix Action:
- CitrixConnect
- CitrixConnectICA
- CitrixDisconnect
- CitrixKeyType
- CitrixMouseClick
- CitrixMouseMove
- CitrixScreenshotGet
- CitrixScreenshotWait
- CitrixTextGet
- CitrixTextType
- CitrixTextWait
- CitrixWindowActivate
- CitrixWindowWait

A RealBrowser Action:
- BrowserCaptureRequests
- BrowserClick
- BrowserClose
- BrowserCloseTab
- BrowserDragAndDrop
- BrowserEvaluateJavascript
- BrowserHandleDialog
- BrowserMouseOver
- BrowserNavigate
- BrowserOpen
- BrowserOpenTab
- BrowserPress
- BrowserRead
- BrowserScroll
- BrowserSelectOption
- BrowserSwitchTab
- BrowserType
- BrowserUploadFile
- BrowserWaitSelector

An RTE Action:
- RTEConnect
- RTEDisconnect
- RTERead
- RTESendKey

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



