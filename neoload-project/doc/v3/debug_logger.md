# Debug Logger

The Debug Logger Action writes a text entry to a log file, for debugging purposes.

#### Available settings
| Name        | Description                         | Accept variable | Required | Since |
|:----------- |:------------------------------------|:---------------:|:--------:|:-----:|
| text        | The text to log                     | &#x2713;         | &#x2713; |       |
| file        | The path of the log file            | &#x2713;         |    -     |       |

#### Default value
When `file` is not set, the text is written to `logs/runTimeLog.txt`.

#### Example
Logging the current user id to a custom log file.
```yaml
- debug_logger:
    text: "Current user: ${user_id}"
    file: logs/custom.txt
```
