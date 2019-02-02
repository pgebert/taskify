# Taskify

[![project in progress](https://img.shields.io/badge/state-in%20progress-blue.svg)]()
![screenshot](https://user-images.githubusercontent.com/6838540/52164866-a005f280-26f8-11e9-9945-b25d102a5b15.png)


Taskify is a ToDo list demo application build with java and vaadin. The web application covers the following features:

- ToDo List
- User management
- Task analysis
- Calendar

## Installation

Clone the taskify repository and start a local web server (e.g. Tomcat) or run with jetty. To user jetty, run the following command:

```
mvn vaadin:compile jetty:run
```
To use with Tomcat, run the following command:

```
mvn vaadin:compile package
```

## Development

### Local development with automatic refresh

`mvn -Djetty.reload=automatic -Djetty.scanIntervalSeconds=2 jetty:run`

### Missing vaadin widgetset

`mvn vaadin:compile`

### Deploy on Heroku

```
heroku create
git push heroku master
```


