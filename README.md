# ImgTools

Created by: [Gustavo Araújo](https://github.com/GustavoHGAraujo) (gustavo.hg.araujo@gmail.com)
Contributor: [Daniel Gunna](https://github.com/DanielGunna)

## Features

This module has already implemented the functions and procedures related to images that are currently used in Android apps, such as:

- Download images using a URL
- Setting images into ImageView (when a ProgressBar is passed with the ImageView, it is automatically hidden when the image is visible)
- Crop image into square and circle shapes

## How to use

On `build.gradle` at project level:

```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

On `build.gradle` at module level:

```
dependencies {
  compile 'com.github.GustavoHGAraujo:imgtools:v0.2'
}
```

Because all the functions are static, to use them simply call using `com.gustavogoma.utils.imgtools.ImgTools.<method_name>`
