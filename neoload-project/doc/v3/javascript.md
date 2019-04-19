# Javascript
Execution of a Javascript script.

#### Available settings
| Name        | Description                                 | Required | Since |
|:----------- |:------------------------------------------- |:--------:|:-----:|
| name        | The Javascript name                         | &#x2713; |6.10|
| description |  The Javascript description                 |          |6.10|
| script      |  The Javascript script                      | &#x2713; |6.10|


#### Example
Defining a Javascript which get a variable from the VariableManager and log the value as debug.
```yaml
- javascript:
    name: My Javascript
    description: My description
    script: |
      // Get variable value from VariableManager
      var myVar = context.variableManager.getValue("CounterVariable_1");
      logger.debug("ComputedValue="+myVar);
```