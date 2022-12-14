# Play Pet Store

An implementation of [Java Pet Store](https://www.oracle.com/technical-resources/articles/javaee/pet-store-application.html) application using Play Framework for Java.

## Getting Started
1.) Create a new database in your Postgres instance.

2.) Create the file application-local.conf in /conf.  Copy the following into it, substituting the appropriate details for your target database:
```
# Database Connection Configuration
db.default.url                             = "jdbc:postgresql://localhost/play_pet_store"
db.default.username                        = "ppsuser"
db.default.password                        = "ppspass"
db.default.logSql                          = false
db.default.databaseName                    = "play_pet_store"

#  This is a development environment, disable CSRF filtering
play.filters.disabled += "play.filters.csrf.CSRFFilter"
```


### Prerequisites

* JDK 11.x
* sbt 1.7.1
* Postgres 9.6

### Running the App

To build the project, cd to the project's base directory and run:

```
sbt run
```

This should start the application, which will be accessible at http://localhost:9000


## Built With

* [sbt](https://www.scala-sbt.org/) - Build tool
* [Play Framework](https://www.playframework.com/) - Web Application Framework
* [Bootstrap](https://getbootstrap.com/) - Front-End Framework
* [Bootstrap Icons](https://icons.getbootstrap.com/) - Icons
* [Ebean](https://ebean.io/) - ORM
* [caffeine](https://github.com/ben-manes/caffeine) - High-performance caching
* [Toastify](https://github.com/apvarun/toastify-js/) - Client-side notifications
* Pictures from the [Pinnacle 21](https://www.pinnacle21.com/) pet family!

## Contributing
This is a demonstration project meant for a technical talk.  If you'd like to contribute, you are welcome to by
emailing the author.  However, this project is not slated for future development by the author.

## Authors

* **Gregory Taylor**

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments