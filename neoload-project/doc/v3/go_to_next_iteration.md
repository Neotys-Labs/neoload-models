# Go to next iteration

The Go to next iteration action interrupts the Virtual User runtime and makes it go to the next iteration. The way this 
action behaves depends on where it is placed within the Virtual User:
* Placed in an Init Container: The Virtual User interrupts its initialization and starts its first iteration by running the Actions Container;
* Placed in an Actions Container: The Virtual User interrupts its current iteration and runs (depending on the load policy):
  - a new iteration by running the Actions Container;
  - its End Container.
* Placed in an End Container: There is no interruption; the Virtual User continues to run its End Container and an error message is written to the log files.

The Go to next iteration logical action can be particularly useful in combination with an If ... Then ... Else or Try...Catch action. For example, you can:
* avoid running the Init Container for a Virtual User that is already logged on to the application;
* re-start a Virtual User iteration if an error occurs.

#### Available settings

This action has no configurable properties.

#### Example

```yaml
  actions:
    steps:
    - go_to_next_iteration
```
