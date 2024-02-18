# SeleniumHelper

SeleniumHelper is a Java library that provides a framework for Selenium automation testing. It includes utilities for logging with Log4j, handling JSON data with json-simple, and capturing screenshots during test execution. Additionally, it offers wrappers around Selenium functions to simplify test development and enhance test reporting.

## Usage

### Maven Repository

To use SeleniumHelper in your Maven project, add the following repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <name>SeleniumHelper Package</name>
        <url>https://github.com/creator54/seleniumhelper/releases/latest/download/selenium-helper-1.0-SNAPSHOT.jar</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

### Dependency

Add the SeleniumHelper dependency to your pom.xml:

```xml
<dependency>
    <groupId>dev.creator54</groupId>
    <artifactId>selenium-helper</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Example Usage
Here's an example of how to use SeleniumHelper in your test code:
```java
import dev.creator54.seleniumhelper.JSONHelper;
import dev.creator54.seleniumhelper.SeleniumHelper;
import org.openqa.selenium.WebElement;

public class MyTest {
	public static void main(String[] args) {
		// Initialize SeleniumHelper
		SeleniumHelper seleniumHelper = SeleniumHelper().getInstance();

		// Initialize JSONHelper 
		// JSONHelper looks for a config.json file for configuration at root level
		JSONHelper jsonHelper = new JSONHelper();

		// Path to configfile can also be specified by passing as an argument to the constructor

		// get a value from the configfile using JSONHelper like
		String site = jsonHelper.getValue("site");

		// nested values can also be accessed like
		String options = jsonHelper.getValue("site#options");

		// locators can directly be inferred from the json config like
		WebElement button = jsonHelper.get("site#button");
        
		try {
			// Example usage of SeleniumHelper methods
			seleniumHelper.get(site);

			System.out.println(options);
            
            seleniumHelper.clickElement(button);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close browser
			SeleniumHelper.quit();
		}
	}
}
```

## Features
- Automated logging with Log4j integration 
- JSON data handling with json-simple library 
- Screenshot capture utility for each action
- Simplified Selenium function wrappers using `config.json` for faster and resilient automation

## LICENSE
```MIT
The MIT License (MIT)

Copyright (c) 2024 Creator54<hi.creator54@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```