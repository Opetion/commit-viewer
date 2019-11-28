# Commit Viewer (Seagit)
_This project was developed in the context of an exercise._  

## Description
The goal of this project is to provide a git commit history.  

There are 2 implemented parsers for this:
- Github API;  
- Git CLI;  
  
## Dev Guide
### Commits 
Notice the commits are following [Conventional Commits](https://www.conventionalcommits.org) standards while using [GitMoji](https://gitmoji.carloscuesta.me/) for the type of commit. 

### Code Conventions
[Spotless](https://github.com/diffplug/spotless) is being used to format the code and to help enforce the conventions being followed we provide a [pre-commit hook](/scripts/pre-commit) that enforces the plugin usage. 

## Project Installation
### Pre-requirements
This project is built using language features of Java 11 and so you must install a JDK from one of the many providers.

If unsure of which one to choose I suggest [AdoptOpenJDK](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot) which provides prebuilt openjdk binaries for many platforms.

It is also required to have a git client installed and available on the system path to be used by the server. 

### Build/Run
To create a runnable jar we only need to execute `./gradlew bootJar` that will provide a runnable jar at `/build/libs` that can be ran with `java -jar seagit-<version>.jar`.

### Endpoints
Currently we provide a [Postman file](scripts/Seagit.postman_collection.json) with the available endpoints-
Currently the application provides the following endpoints:  
  
*\[GET] Index - Repositories*  
url: /api/repositories  
description: Provides a list of repositories     
   
*\[POST] Create - Repository*  
url: /api/repositories  
description: Clones a repository  
Expects the body with:  
```json
{
  "url":"url of a git repository"
}
``` 

*\[GET] Index -  Repository Commits*  
url: /api/{id}/commits  
description: Visualize the commits of a repository  
request parameters:  
size (int) - elements per page  
page (int) - number of page  

## Future Work

- Reuse the workspace created to deal with server restart or use a database.  
- Filter by branch and/or show results from all branches:  
  - CLI Client - It should be possible to show results from all branches with `--all` for the cli client.  
  - Github API - There is no easy way to do this. So it would require to fetch all branches and mix the different commits.  
- git clone --depth to have a faster first result and unshallow on the background.  
- Have a background process fetching the latest commits to avoid CLI being stuck on the past.  
