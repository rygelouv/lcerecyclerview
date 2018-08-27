# Introduction

 

## Add it to your project

In your project root build.gradle with:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
and in the app or module build.gradle:

```gradle
dependencies {
    implementation 'com.github.rygelouv:lcerecyclerview:0.0.4'
}
```

## Credits

Author: Rygel Louv [https://medium.com/@rygel](https://medium.com/@rygel)


License
--------

    Copyright 2017 Rygelouv.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.