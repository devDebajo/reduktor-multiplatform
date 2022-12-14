# Reduktor
Implementation of Unidirectional Data Flow for Android and iOS with Kotlin Multiplatform and Coroutines

# Principle of working
<img src="img/Diagram.png" alt="Diagram"/>

# Install

You need to clone this repository, publish to Maven Local by command:
```bash
./gradlew publishToMavenLocal
```
then add dependency in your project:
```gradle
implementation("ru.debajo.reduktor:reduktor-shared:<version>")
```

License
-------

    Copyright (C) 2022 debajo

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.