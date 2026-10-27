# Real Browser actions
_Real Browser steps_ are supported in NeoLoad from version 2026.3 onwards (`schemaVersion: "3.1"`).

Each Real Browser action of NeoLoad GUI has its own step, named after the action: `browser_open`, `browser_click`...
The step name gives the type of the action, so there is no `type` property. The parameter names and descriptions are
the ones displayed in NeoLoad GUI. The JSON Schema suggests the parameter names of the action, rejects the names the action does not
know, and requires the parameters marked as required.

A Real Browser action can still be written as a generic [custom_action](custom_action.md) with its `type` (`BrowserClick`...):
it is read the same way, but its parameters are not checked. Writing a project always produces the `browser_*` step.

#### Available settings are

| Name          | Description                                                                         | Accept variable | Required | Since |
|:--------------|:------------------------------------------------------------------------------------|:---------------:|:--------:|:-----:|
| name          | The name of the action. Defaults to the action type (`BrowserClick`, `BrowserOpen`...), see the table below |        -        |    -     |       |
| description   | The description of the action                                                       |        -        |    -     |       |
| parameters    | The action parameter list, see each action below. Each entry has a `name`, a `value` and an optional `type` (`TEXT` or `PASSWORD`), like the [custom_action parameters](custom_action.md#parameters) |    &#x2713;     | When the action has a required parameter | |
| asRequest     | Consider the executions as requests in the calculation of the statistics and graphs |        -        |    -     |       |
| [duration_assertion](duration_assertion.md) | Checks that the action completed within a given duration             |        -        |    -     |       |
| [size_assertion](size_assertion.md) | The assertion to validate the response size                   |        -        |    -     |       |

An action without any setting, such as `browser_open_tab`, can be written as a bare scalar: `- browser_open_tab`.
This is not possible for an action with a required parameter.

All values are written as text, booleans included (`"true"` or `"false"`). A parameter can be repeated when its
description says so, for example `selector`, `header` or `resourceType`.

#### Example

```yaml
- browser_open:
    parameters:
    - name: browser
      value: chromium
    - name: resolution
      value: 1920x1080
- browser_navigate:
    name: Go to home
    asRequest: true
    parameters:
    - name: url
      value: https://www.tricentis.com/
- browser_type:
    name: Log in
    parameters:
    - name: selector
      value: "#password"
    - name: encryptedText
      value: myPassword
      type: PASSWORD
- browser_upload_file:
    name: Upload files
    parameters:
    - name: selector
      value: "input[type=file]"
    - name: filePaths
      value: "${NL-CustomResources}/a.txt;${NL-CustomResources}/b.txt"
- browser_open_tab
- browser_close
```

## Actions

| Step | Type |
|:-----|:-----|
| [browser_open](#browser_open) | `BrowserOpen` |
| [browser_close](#browser_close) | `BrowserClose` |
| [browser_navigate](#browser_navigate) | `BrowserNavigate` |
| [browser_click](#browser_click) | `BrowserClick` |
| [browser_type](#browser_type) | `BrowserType` |
| [browser_press](#browser_press) | `BrowserPress` |
| [browser_select_option](#browser_select_option) | `BrowserSelectOption` |
| [browser_mouse_over](#browser_mouse_over) | `BrowserMouseOver` |
| [browser_drag_and_drop](#browser_drag_and_drop) | `BrowserDragAndDrop` |
| [browser_scroll](#browser_scroll) | `BrowserScroll` |
| [browser_read](#browser_read) | `BrowserRead` |
| [browser_wait_selector](#browser_wait_selector) | `BrowserWaitSelector` |
| [browser_evaluate_javascript](#browser_evaluate_javascript) | `BrowserEvaluateJavascript` |
| [browser_upload_file](#browser_upload_file) | `BrowserUploadFile` |
| [browser_handle_dialog](#browser_handle_dialog) | `BrowserHandleDialog` |
| [browser_open_tab](#browser_open_tab) | `BrowserOpenTab` |
| [browser_switch_tab](#browser_switch_tab) | `BrowserSwitchTab` |
| [browser_close_tab](#browser_close_tab) | `BrowserCloseTab` |
| [browser_capture_requests](#browser_capture_requests) | `BrowserCaptureRequests` |
| [browser_capture_web_vitals](#browser_capture_web_vitals) | `BrowserCaptureWebVitals` |

## browser_open

Type `BrowserOpen`. Open a new browser.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `browser` | Browser type, possible values are: chromium, firefox, webkit. Default value is "chromium". | &#x2713; |
| `headless` | Whether to run browser in headless mode. | - |
| `defaultTimeout` | Sets the default timeout for every action in ms. Default value is 30000, maximum value is 200000. | - |
| `defaultNavigationTimeout` | Sets the default timeout for every navigation in ms. Default value is 20000, maximum value is 200000. | - |
| `resolution` | Resolution (width x height) of browser. Example: 1920x1080 | - |
| `locale` | Locale of the browser. Example: en-GB | - |
| `timezone` | Timezone of the context | - |
| `blockRoute` | Block all resources related to the given route. Example: `https:\/\/neoload-api\.saas\.neotys\.com\/v3\/.*` to block all v3 api requests from https://neoload-api.saas.neotys.com/v3. `.*\.(png\|jpg\|jpeg)` to block all images resources. Note: images embedded in html (SVG, base64 etc...) won't be blocked | - |
| `userAgent` | User agent of the browser | - |
| `browserArguments` | Additional arguments to pass to the browser instance. Advanced parameter, hidden by default in NeoLoad GUI. | - |
| `header` | Adds the specified header to each requests sent by the browser. You can add as many "header" parameter as you need. This parameter expected format is "(HeaderHost) HeaderName=HeaderValue" or "HeaderName=HeaderValue". If HeaderHost is empty, header will affect every requests. Otherwise, header will only affect requests addressed to this host | - |
| `ignoreSSLCertificateErrors` | Ignore SSL certificate errors (expired, untrusted, wrong host certificate, etc.). It is not recommended for security matters. Default value is "false". | - |
| `bypassCsp` | Set this parameter to "true" to bypass all content security policies in the browser context. Default value is "false". | - |
| `geolocation` | Overrides the detected geolocation in the browser. Expected format is "latitude;longitude;accuracy". Values should be in decimal format. | - |
| `permissions` | Adds the specified permissions in the current browser context. The expected value is a comma-separated list of permissions. Example "geolocation, notifications, camera, microphone" | - |
| `channel` | Browser distribution channel, possible values are: chrome, chrome-beta, chrome-dev, chrome-canary, msedge, msedge-beta, msedge-dev, msedge-canary, chromium | - |
| `executablePath` | Path to a browser executable to run instead of the bundled one. Note that Playwright only works with the bundled Chromium, Firefox or WebKit, use at your own risk. | - |
| `httpCredentials.login` | Login for HTTP authentication. 'httpCredentials.origin' parameter is required to restrict sending credentials to specific hosts. | - |
| `httpCredentials.password` | Password for HTTP authentication. 'httpCredentials.origin' parameter is required to restrict sending credentials to specific hosts. Set the parameter `type` to `PASSWORD` to store the value encrypted. | - |
| `httpCredentials.origin` | Origin to which credentials for HTTP authentication will be sent. Ex: 'https://tricentis.com'. | - |
| `httpCredentials.disableOriginRestrictions` | (UNSAFE) Set to true to allow specifying no origin for httpCredentials. This means that login & password will be sent to any server that sends a 401 challenge in response to a request sent by the browser. Advanced parameter, hidden by default in NeoLoad GUI. | - |
| `httpCredentials.allowedOrigin` | Deprecated parameter. Use httpCredentials.origin instead. Advanced parameter, hidden by default in NeoLoad GUI. | - |
| `ignoreScreenshots` | Disable all screenshots during Check VU or Debug. Default value is "false". | - |
| `ignoreErrors` | Set this option to true to ignore errors for this action. This is useful for handling conditional actions without having to resort to a "If...Then...Else" or a "Try...Catch" block. Default value is "false". | - |

## browser_close

Type `BrowserClose`. Close the browser. This action is useful for avoiding stacking browser instances on a single VU. The recommended configurations is : If the OpenBrowser action is at the beginning of the ACTIONS container, put the CloseBrowser action at end of the ACTIONS container.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `ignoreErrors` | Set this option to true to ignore errors for this action. This is useful for handling conditional actions without having to resort to a "If...Then...Else" or a "Try...Catch" block. Default value is "false". | - |
| `shouldClearProcesses` | This parameter is only applicable when the action is in the Actions container (ignored otherwise). Default is false, the internal processes are persisted in the Virtual user instance to be reused from one iteration to another. Setting the value to true will ensure all external processes are closed and recreated for each iteration, at the cost of increasing the response time and CPU consumption of this CloseBrowser and the next OpenBrowser actions. Default value is "false". | - |

## browser_navigate

Type `BrowserNavigate`. Navigate to an url.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `url` | URL to visit. The URL must start with an http or https scheme, for example "https://". | &#x2713; |
| `unsafeUrl` | URL to visit without scheme restriction, for example a local file. Always ignored on a NeoLoad Cloud Load Generator. Advanced parameter, hidden by default in NeoLoad GUI. | - |
| `waitForNavigation.url` | URL pattern targeted, ex:http://tricentis.com/.* . The URL being navigated to must match this pattern. Enables a wait for page navigation after this action has been executed. | - |
| `waitForNavigation.timeout` | If applicable, the navigation must occur within the specified timeout in ms. By default the OpenBrowser defaultNavigationTimeout is used. Enables a wait for page navigation after this action has been executed. Maximum value is 200000. | - |
| `waitForNavigation.until` | Allowed values are : LOAD, DOMCONTENTLOADED, NETWORKIDLE, COMMIT. Default value is LOAD. The navigation will be considered done when the specified state is reached. Enables a wait for page navigation after this action has been executed. | - |
| `ignoreErrors` | Set this option to true to ignore errors for this action. This is useful for handling conditional actions without having to resort to a "If...Then...Else" or a "Try...Catch" block. Default value is "false". | - |

## browser_click

Type `BrowserClick`. Click a selector.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `selector` | The selector is a string that point to the elements in the page. Several types of selector are supported: text, CSS, XPath... You can specify several selectors by adding multiple selector parameters to this action. The targeted element will match any of the selectors (OR operator). | - |
| `coordinates` | Coordinates of the click, relative to the selector if specified, absolute otherwise. If no coordinates are specified with selector, the click will be performed on the middle of the element. Example: 100.0;200.0 | - |
| `force` | true or false. Forces the click, even if the target element is obscured by other elements. Default value is "false". | - |
| `mouseButton` | LEFT, MIDDLE, RIGHT, BACK or FORWARD. Default to LEFT. | - |
| `doubleClick` | true or false. Performs a double click. Default value is "false". | - |
| `modifiers` | Modifier keys to press separated by "+" like Control+Shift. | - |
| `timeout` | Forces the timeout for that action in ms. By default the OpenBrowser defaultTimeout is used. Maximum value is 200000. | - |
| `frameIndex` | Frame index. | - |
| `frameName` | Frame name. | - |
| `frameSelector` | Frame selector. | - |
| `waitForNavigation.url` | URL pattern targeted, ex:http://tricentis.com/.* . The URL being navigated to must match this pattern. Enables a wait for page navigation after this action has been executed. | - |
| `waitForNavigation.timeout` | If applicable, the navigation must occur within the specified timeout in ms. By default the OpenBrowser defaultNavigationTimeout is used. Enables a wait for page navigation after this action has been executed. Maximum value is 200000. | - |
| `waitForNavigation.until` | Allowed values are : LOAD, DOMCONTENTLOADED, NETWORKIDLE, COMMIT. Default value is LOAD. The navigation will be considered done when the specified state is reached. Enables a wait for page navigation after this action has been executed. | - |
| `waitForDownload` | Enable waiting for a download if the parameter is set to true. Default value is "false". | - |
| `waitForDownload.path` | The path where to save the downloaded file. Can include the filename, i.e : /home/my-downloads/my-downloaded-file.txt | - |
| `waitForDownload.timeout` | If applicable, the download must start within the specified timeout in ms. By default the OpenBrowser defaultNavigationTimeout is used. Maximum value is 200000. If the timeout is set to 0, the download will be waited indefinitely. | - |
| `ignoreErrors` | Set this option to true to ignore errors for this action. This is useful for handling conditional actions without having to resort to a "If...Then...Else" or a "Try...Catch" block. Default value is "false". | - |

## browser_type

Type `BrowserType`. Type some text inside an element.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `selector` | The selector is a string that point to the elements in the page. Several types of selector are supported: text, CSS, XPath... You can specify several selectors by adding multiple selector parameters to this action. The targeted element will match any of the selectors (OR operator). | &#x2713; |
| `text` | Text to type | - |
| `encryptedText` | Text to type on a password input. Set the parameter `type` to `PASSWORD` to store the value encrypted. | - |
| `timeout` | Forces the timeout for that action in ms. By default the OpenBrowser defaultTimeout is used. Maximum value is 200000. | - |
| `delay` | Adds delay between each keydown/keyup sequence in ms. When this parameter is absent, the text will be inserted in the field as a whole, without triggering any keydown/keyup events. | - |
| `frameIndex` | Frame index. | - |
| `frameName` | Frame name. | - |
| `frameSelector` | Frame selector. | - |
| `waitForNavigation.url` | URL pattern targeted, ex:http://tricentis.com/.* . The URL being navigated to must match this pattern. Enables a wait for page navigation after this action has been executed. | - |
| `waitForNavigation.timeout` | If applicable, the navigation must occur within the specified timeout in ms. By default the OpenBrowser defaultNavigationTimeout is used. Enables a wait for page navigation after this action has been executed. Maximum value is 200000. | - |
| `waitForNavigation.until` | Allowed values are : LOAD, DOMCONTENTLOADED, NETWORKIDLE, COMMIT. Default value is LOAD. The navigation will be considered done when the specified state is reached. Enables a wait for page navigation after this action has been executed. | - |
| `waitForDownload` | Enable waiting for a download if the parameter is set to true. Default value is "false". | - |
| `waitForDownload.path` | The path where to save the downloaded file. Can include the filename, i.e : /home/my-downloads/my-downloaded-file.txt | - |
| `waitForDownload.timeout` | If applicable, the download must start within the specified timeout in ms. By default the OpenBrowser defaultNavigationTimeout is used. Maximum value is 200000. If the timeout is set to 0, the download will be waited indefinitely. | - |
| `ignoreErrors` | Set this option to true to ignore errors for this action. This is useful for handling conditional actions without having to resort to a "If...Then...Else" or a "Try...Catch" block. Default value is "false". | - |

## browser_press

Type `BrowserPress`. Press a key on a selector.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `selector` | The selector is a string that point to the elements in the page. Several types of selector are supported: text, CSS, XPath... You can specify several selectors by adding multiple selector parameters to this action. The targeted element will match any of the selectors (OR operator). | &#x2713; |
| `key` | Key to press, can be a simple key, example : 'A' Or a combination with modifiers: 'Control+Shift+C' or 'ArrowLeft+Control'. The list of keys are standard values for the KeyboardEvent.key property from the Browser Keyboard API. | &#x2713; |
| `delay` | Delay in ms between each keydown/keyup sequence. | - |
| `timeout` | Forces the timeout for that action in ms. By default the OpenBrowser defaultTimeout is used. Maximum value is 200000. | - |
| `frameIndex` | Frame index. | - |
| `frameName` | Frame name. | - |
| `frameSelector` | Frame selector. | - |
| `waitForNavigation.url` | URL pattern targeted, ex:http://tricentis.com/.* . The URL being navigated to must match this pattern. Enables a wait for page navigation after this action has been executed. | - |
| `waitForNavigation.timeout` | If applicable, the navigation must occur within the specified timeout in ms. By default the OpenBrowser defaultNavigationTimeout is used. Enables a wait for page navigation after this action has been executed. Maximum value is 200000. | - |
| `waitForNavigation.until` | Allowed values are : LOAD, DOMCONTENTLOADED, NETWORKIDLE, COMMIT. Default value is LOAD. The navigation will be considered done when the specified state is reached. Enables a wait for page navigation after this action has been executed. | - |
| `waitForDownload` | Enable waiting for a download if the parameter is set to true. Default value is "false". | - |
| `waitForDownload.path` | The path where to save the downloaded file. Can include the filename, i.e : /home/my-downloads/my-downloaded-file.txt | - |
| `waitForDownload.timeout` | If applicable, the download must start within the specified timeout in ms. By default the OpenBrowser defaultNavigationTimeout is used. Maximum value is 200000. If the timeout is set to 0, the download will be waited indefinitely. | - |
| `ignoreErrors` | Set this option to true to ignore errors for this action. This is useful for handling conditional actions without having to resort to a "If...Then...Else" or a "Try...Catch" block. Default value is "false". | - |

## browser_select_option

Type `BrowserSelectOption`. Select one or several options of a select element.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `selector` | The selector is a string that point to the elements in the page. Several types of selector are supported: text, CSS, XPath... You can specify several selectors by adding multiple selector parameters to this action. The targeted element will match any of the selectors (OR operator). | &#x2713; |
| `options[N].text` | Select the option with a matching text. Multiple options can be selected by adding more parameters with a different index. | - |
| `options[N].value` | Select the option with a matching value. Multiple options can be selected by adding more parameters with a different index. | - |
| `options[N].index` | Select the option with a matching index. Multiple options can be selected by adding more parameters with a different index. | - |
| `timeout` | Forces the timeout for that action in ms. By default the OpenBrowser defaultTimeout is used. Maximum value is 200000. | - |
| `frameIndex` | Frame index. | - |
| `frameName` | Frame name. | - |
| `frameSelector` | Frame selector. | - |
| `waitForNavigation.url` | URL pattern targeted, ex:http://tricentis.com/.* . The URL being navigated to must match this pattern. Enables a wait for page navigation after this action has been executed. | - |
| `waitForNavigation.timeout` | If applicable, the navigation must occur within the specified timeout in ms. By default the OpenBrowser defaultNavigationTimeout is used. Enables a wait for page navigation after this action has been executed. Maximum value is 200000. | - |
| `waitForNavigation.until` | Allowed values are : LOAD, DOMCONTENTLOADED, NETWORKIDLE, COMMIT. Default value is LOAD. The navigation will be considered done when the specified state is reached. Enables a wait for page navigation after this action has been executed. | - |
| `ignoreErrors` | Set this option to true to ignore errors for this action. This is useful for handling conditional actions without having to resort to a "If...Then...Else" or a "Try...Catch" block. Default value is "false". | - |

## browser_mouse_over

Type `BrowserMouseOver`. Positions the mouse over the specified coordinates or element location. Scrolls the window when that position is out of the current viewport. Both parameters can be set at the same time, in that case the mouse will be positioned at the specified coordinates relative to the located element instead of the page origin.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `selector` | The selector is a string that point to the elements in the page. Several types of selector are supported: text, CSS, XPath... You can specify several selectors by adding multiple selector parameters to this action. The targeted element will match any of the selectors (OR operator). | - |
| `coordinates` | Coordinates of the mouse, relative to the selector if specified, absolute otherwise. If no coordinates are specified with selector, the mouse will be placed at the middle of the element. Example: 100.0;200.0 | - |
| `timeout` | Forces the timeout for that action in ms. By default the OpenBrowser defaultTimeout is used. Maximum value is 200000. | - |
| `frameIndex` | Frame index. | - |
| `frameName` | Frame name. | - |
| `frameSelector` | Frame selector. | - |
| `waitForNavigation.url` | URL pattern targeted, ex:http://tricentis.com/.* . The URL being navigated to must match this pattern. Enables a wait for page navigation after this action has been executed. | - |
| `waitForNavigation.timeout` | If applicable, the navigation must occur within the specified timeout in ms. By default the OpenBrowser defaultNavigationTimeout is used. Enables a wait for page navigation after this action has been executed. Maximum value is 200000. | - |
| `waitForNavigation.until` | Allowed values are : LOAD, DOMCONTENTLOADED, NETWORKIDLE, COMMIT. Default value is LOAD. The navigation will be considered done when the specified state is reached. Enables a wait for page navigation after this action has been executed. | - |
| `ignoreErrors` | Set this option to true to ignore errors for this action. This is useful for handling conditional actions without having to resort to a "If...Then...Else" or a "Try...Catch" block. Default value is "false". | - |

## browser_drag_and_drop

Type `BrowserDragAndDrop`. Drag and drop an element onto another element.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `sourceSelector` | The Source Selector identifies the element to drag. The selector is a string that point to the elements in the page. Several types of selector are supported: text, CSS, XPath... You can specify several selectors by adding multiple sourceSelector parameters to this action. The targeted element will match any of the selectors (OR operator). | &#x2713; |
| `targetSelector` | The target selector identifies where the element dragged should be dropped. The selector is a string that point to the elements in the page. Several types of selector are supported: text, CSS, XPath... You can specify several selectors by adding multiple targetSelector parameters to this action. The targeted element will match any of the selectors (OR operator). | &#x2713; |
| `sourcePosition` | A position relative to the top left corner of the source element where to perform the click initiating the drag action. Example : 200;350 | - |
| `targetPosition` | A position relative to the top left corner of the target element where to release the click finalizing the drop action. Example : 200;350 | - |
| `frameIndex` | Frame index. | - |
| `frameName` | Frame name. | - |
| `frameSelector` | Frame selector. | - |
| `force` | true or false. Forces the click, even if the target element is obscured by other elements. Default value is "false". | - |
| `timeout` | Forces the timeout for that action in ms. By default the OpenBrowser defaultTimeout is used. Maximum value is 200000. | - |
| `waitForDownload` | Enable waiting for a download if the parameter is set to true. Default value is "false". | - |
| `waitForDownload.path` | The path where to save the downloaded file. Can include the filename, i.e : /home/my-downloads/my-downloaded-file.txt | - |
| `waitForDownload.timeout` | If applicable, the download must start within the specified timeout in ms. By default the OpenBrowser defaultNavigationTimeout is used. Maximum value is 200000. If the timeout is set to 0, the download will be waited indefinitely. | - |
| `ignoreErrors` | Set this option to true to ignore errors for this action. This is useful for handling conditional actions without having to resort to a "If...Then...Else" or a "Try...Catch" block. Default value is "false". | - |

## browser_scroll

Type `BrowserScroll`. Scrolls the window to a particular place in the document.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `X` | The amount of pixels to scroll on the X axis. Positive values emulate a scroll right and negative values a scroll left event. Default value is "0". | - |
| `Y` | The amount of pixels to scroll on the Y axis. Positive values emulate a scroll down and negative values a scroll top event. Default value is "0". | - |
| `waitForNavigation.url` | URL pattern targeted, ex:http://tricentis.com/.* . The URL being navigated to must match this pattern. Enables a wait for page navigation after this action has been executed. | - |
| `waitForNavigation.timeout` | If applicable, the navigation must occur within the specified timeout in ms. By default the OpenBrowser defaultNavigationTimeout is used. Enables a wait for page navigation after this action has been executed. Maximum value is 200000. | - |
| `waitForNavigation.until` | Allowed values are : LOAD, DOMCONTENTLOADED, NETWORKIDLE, COMMIT. Default value is LOAD. The navigation will be considered done when the specified state is reached. Enables a wait for page navigation after this action has been executed. | - |
| `waitForDownload` | Enable waiting for a download if the parameter is set to true. Default value is "false". | - |
| `waitForDownload.path` | The path where to save the downloaded file. Can include the filename, i.e : /home/my-downloads/my-downloaded-file.txt | - |
| `waitForDownload.timeout` | If applicable, the download must start within the specified timeout in ms. By default the OpenBrowser defaultNavigationTimeout is used. Maximum value is 200000. If the timeout is set to 0, the download will be waited indefinitely. | - |
| `ignoreErrors` | Set this option to true to ignore errors for this action. This is useful for handling conditional actions without having to resort to a "If...Then...Else" or a "Try...Catch" block. Default value is "false". | - |

## browser_read

Type `BrowserRead`. Read the text content of an Element.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `selector` | The selector is a string that point to the elements in the page. Several types of selector are supported: text, CSS, XPath... You can specify several selectors by adding multiple selector parameters to this action. The targeted element will match any of the selectors (OR operator). | &#x2713; |
| `variableName` | The variable name of the text result. | - |
| `attribute` | If specified, returns the attribute's value instead of the text value of the selected element | - |
| `timeout` | Forces the timeout for that action in ms. By default the OpenBrowser defaultTimeout is used. Maximum value is 200000. | - |
| `frameIndex` | Frame index. | - |
| `frameName` | Frame name. | - |
| `frameSelector` | Frame selector. | - |
| `ignoreErrors` | Set this option to true to ignore errors for this action. This is useful for handling conditional actions without having to resort to a "If...Then...Else" or a "Try...Catch" block. Default value is "true". | - |

## browser_wait_selector

Type `BrowserWaitSelector`. Wait for a selector.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `selector` | The selector is a string that point to the elements in the page. Several types of selector are supported: text, CSS, XPath... You can wait for several elements at the same time by adding multiple "selector" parameters to this action. | &#x2713; |
| `timeout` | Forces the timeout for that action in ms. By default the OpenBrowser defaultTimeout is used. Maximum value is 200000. | - |
| `state` | attached, detached, visible, hidden. Defaults to 'visible'. | - |
| `frameIndex` | Frame index. | - |
| `frameName` | Frame name. | - |
| `frameSelector` | Frame selector. | - |
| `ignoreErrors` | Set this option to true to ignore errors for this action. This is useful for handling conditional actions without having to resort to a "If...Then...Else" or a "Try...Catch" block. Default value is "false". | - |
| `selectorLogicalOperator` | Set this option with AND or OR to change the logical operator between the selectors. Default value is "AND". | - |

## browser_evaluate_javascript

Type `BrowserEvaluateJavascript`. Executes some arbitrary Javascript code in the current page context.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `script` | A javascript function or expression to execute | &#x2713; |
| `variableName` | The name of the variable where the evaluation result will be stored. | - |
| `frameIndex` | Frame index. | - |
| `frameName` | Frame name. | - |
| `frameSelector` | Frame selector. | - |
| `waitForNavigation.url` | URL pattern targeted, ex:http://tricentis.com/.* . The URL being navigated to must match this pattern. Enables a wait for page navigation after this action has been executed. | - |
| `waitForNavigation.timeout` | If applicable, the navigation must occur within the specified timeout in ms. By default the OpenBrowser defaultNavigationTimeout is used. Enables a wait for page navigation after this action has been executed. Maximum value is 200000. | - |
| `waitForNavigation.until` | Allowed values are : LOAD, DOMCONTENTLOADED, NETWORKIDLE, COMMIT. Default value is LOAD. The navigation will be considered done when the specified state is reached. Enables a wait for page navigation after this action has been executed. | - |
| `waitForDownload` | Enable waiting for a download if the parameter is set to true. Default value is "false". | - |
| `waitForDownload.path` | The path where to save the downloaded file. Can include the filename, i.e : /home/my-downloads/my-downloaded-file.txt | - |
| `waitForDownload.timeout` | If applicable, the download must start within the specified timeout in ms. By default the OpenBrowser defaultNavigationTimeout is used. Maximum value is 200000. If the timeout is set to 0, the download will be waited indefinitely. | - |
| `ignoreErrors` | Set this option to true to ignore errors for this action. This is useful for handling conditional actions without having to resort to a "If...Then...Else" or a "Try...Catch" block. Default value is "false". | - |

## browser_upload_file

Type `BrowserUploadFile`. Upload files to an input element.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `selector` | The selector is a string that point to the elements in the page. Several types of selector are supported: text, CSS, XPath... You can specify several selectors by adding multiple selector parameters to this action. The targeted element will match any of the selectors (OR operator). | &#x2713; |
| `filePaths` | A list of file paths separated by semicolons ';'. Use the variable '${NL-CustomResources}' to access on the Load Generator the synchronized resources located in the 'custom-resources' folder of the project. | &#x2713; |
| `timeout` | Forces the timeout for that action in ms. By default the OpenBrowser defaultTimeout is used. Maximum value is 200000. | - |
| `frameIndex` | Frame index. | - |
| `frameName` | Frame name. | - |
| `frameSelector` | Frame selector. | - |
| `waitForNavigation.url` | URL pattern targeted, ex:http://tricentis.com/.* . The URL being navigated to must match this pattern. Enables a wait for page navigation after this action has been executed. | - |
| `waitForNavigation.timeout` | If applicable, the navigation must occur within the specified timeout in ms. By default the OpenBrowser defaultNavigationTimeout is used. Enables a wait for page navigation after this action has been executed. Maximum value is 200000. | - |
| `waitForNavigation.until` | Allowed values are : LOAD, DOMCONTENTLOADED, NETWORKIDLE, COMMIT. Default value is LOAD. The navigation will be considered done when the specified state is reached. Enables a wait for page navigation after this action has been executed. | - |
| `ignoreErrors` | Set this option to true to ignore errors for this action. This is useful for handling conditional actions without having to resort to a "If...Then...Else" or a "Try...Catch" block. Default value is "false". | - |

## browser_handle_dialog

Type `BrowserHandleDialog`. Set dialog behavior.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `behavior` | The action to take for the next confirm or prompt dialog that appears on the page (alert dialogs are always dismissed). Possible actions are "DISMISS" and "ACCEPT". Default value is "DISMISS". | &#x2713; |
| `promptText` | The text to type in the prompt dialog input. | - |
| `times` | The number of times this behavior will be repeated throughout the user path. Default is 1. | - |

## browser_open_tab

Type `BrowserOpenTab`. Open a new tab at the rightmost position.

This action takes no parameter.

## browser_switch_tab

Type `BrowserSwitchTab`. Switch tab to the given index.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `index` | The 0-based index of the tab | &#x2713; |
| `ignoreErrors` | Set this option to true to ignore errors for this action. This is useful for handling conditional actions without having to resort to a "If...Then...Else" or a "Try...Catch" block. Default value is "false". | - |

## browser_close_tab

Type `BrowserCloseTab`. Close tab at the given index.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `index` | The 0-based index of the tab. If empty closes the current tab. | - |
| `ignoreErrors` | Set this option to true to ignore errors for this action. This is useful for handling conditional actions without having to resort to a "If...Then...Else" or a "Try...Catch" block. Default value is "false". | - |

## browser_capture_requests

Type `BrowserCaptureRequests`. Configure the capture of HTTP requests metrics for analysis purpose. It allows to capture the metrics (duration, TTFB, success...) of the requests sent and responses received between the browser and the server. By default when no regexp/url/resourceType parameter is specified, then all the HTTP request metrics are aggregated in a single group. When a matching criteria is defined (regexp/url/resourceType), the metrics of all HTTP requests matching it are aggregated in a group, and the non-matching one in a different group (for example with regexp (.*)\.html then requests will be split in two different groups: one for all HTML pages and another one for all other resources). When multiple matching criteria are defined (regexp/url/resourceType), they are applied in order they are defined.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `regexp` | The regular expression to group the requests (the matching check is applied on the full request URL: scheme, host name, port number, path and URL parameters). Default value is ".*". | - |
| `url` | The URL to group the requests (excluding query parameters). | - |
| `resourceType` | The resource type to group the requests (possible values are document, stylesheet, image, media, font, script, texttrack, xhr, fetch, eventsource, websocket, manifest, ping, other). | - |
| `includeCache` | By default, only the requests sent over the network are captured (including the requests with 304 'Not Modified' response status since it includes network exchange). Set the value to true to capture also the requests locally loaded from the browser memory or disk cache (no network interaction). Default value is "false". | - |
| `includeFailedRequests` | By default, failed requests (network errors, connection refused, etc.) are excluded from captured results. Set the value to true to include them. Default value is "false". | - |
| `ignoreSizesForRegexp` | Mitigates thread hanging issues when accessing the request sizes. Regular expression applied to the full request URL: when a request URL matches, response sizes are not computed. Leave empty to compute sizes for all requests. Advanced parameter, hidden by default in NeoLoad GUI. | - |

## browser_capture_web_vitals

Type `BrowserCaptureWebVitals`. Captures Core Web Vitals metrics during Virtual User navigation. This helps detect performance issues that affect the user experience. NeoLoad collects the following metrics for each defined URL: Largest Contentful Paint (LCP), Interaction to Next Paint (INP), Cumulative Layout Shift (CLS). If you don't define a URL, NeoLoad aggregates metrics across all visited pages. To collect all metrics, place this action before the first Navigate action. The Virtual User needs to stay a minimum amount of time and perform at least one interaction to ensure Web Vitals were collected for a given page.

| Parameter | Description | Required |
|:----------|:------------|:--------:|
| `url` | A url (excluding query parameters) to group Web Vitals metrics data under. | - |

