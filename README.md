# Android Template

Android template project - A simple movie client that queries themoviedb.org 

<br />

<img src="https://github.com/webfactorymk/android-template/blob/master/diagrams/screenshot.jpg" alt="Light theme" width="330">


## Data Management

![Alt text][high_lvl_diagram]

[high_lvl_diagram]: diagrams/high_lvl_diagram.png "High level diagram"


### MovieDataSource

`/data/repository/movie`

This is the main entry point for accessing and manipulating movies data:

Implementations:
- **MovieRemoteDataSource** - Uses the ApiService to contact a remote server;
- **MovieCacheDataSource** - Uses in-memory cache to retrieve items;
- **MovieRepository** - Uses both *MovieRemoteDataSource* and *MovieCacheDataSource* to fetch cached data when available;


### ApiService

`/network`

Abstraction over the API communication that defines (all) endpoints. 
This templates uses [Chopper], an http client generator, to make network requests.

- **UserApiService** - User related endpoints
- **MovieApiService** - Movie related endpoints


## Dependency Management

`/di`

Dagger Hilt is used to manage 2 scopes: App and (custom) User scope
