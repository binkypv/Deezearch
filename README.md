## Deezearch
Small Android app using the Deezer API, for searching artists and their discography.

### Overview
![](https://github.com/binkypv/Deezearch/blob/main/readmeimgs/DeezearchExample.gif?raw=true)

### Features
- Search artists
- Search albums
- List album tracks with cover art

### Code

#### Layer and module interaction
![](https://github.com/binkypv/Deezearch/blob/main/readmeimgs/modulediagram.png?raw=true)


![](https://github.com/binkypv/Deezearch/blob/main/readmeimgs/layerinteractions.png?raw=true)

#### Frameworks
For Dependency Injection, I have chosen Koin (could have used Dagger, but it would be overkill, too powerful for a small project).

Also, coroutines for threading, and the usual libraries and frameworks: Glide for images, Palette for image processing, Retrofit and OkHttp for network calls.

For navigation between screens, I used Navigation Component and, of course, for the MVVM pattern, LiveData.

#### Testing
For testing, aside from JUnit and Espresso, my choice for mocks was Mockk, since it's quite straightforward to work with coroutines and the whole project is written in Kotlin.

### Considerations
This app follows clean architecture with MVVM, but some of the parts have been skipped. There is no need for use cases, since there is no business logic or important transformation to the data that comes from the API.

The data shown comes only from the API, so there is no offline mode, database or memory storage (Room or similar libraries were not used).

There has been no branching from main because I am the only one working in the app and see it all as a main feature.

UI Tests must be run on API 28, due to known limitations of the Mockk library used, which are listed here:
[Issue 182](https://github.com/mockk/mockk/issues/182 "Issue 182")
[Issue 297](https://github.com/mockk/mockk/issues/297 "Issue 297")
