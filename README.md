# Workday calendar

In the danish gas and electricity and gas market many business processes
is required to be instantiated or finised within a time limit calculated
in "workdays".

Non workdays are more that just weekends because danish hollydays needs
to be taken into account as well. Energinet has specified non workdays
in the D1 regulation.


## Usage

This can be used as a clojure lib:

``` clojure
(nth-workday (t/local-date 2015 4 19) 10)
#<LocalDate 2015-05-05>
```

or as a webservice:

http://localhost:3000/date-subsequent-to-workdays/?ref-date=2015-04-19&workdays=10

HTTP body: 2015-05-05

The webservice validates both ref-date and workdays and returns 400 bad
request if one or both provided parameters uses a wrong format.
An error message can be found in the HTTP body.

ref-date must be a valid iso 8601 string while workdays must be an integer
(both positive and negative numbers are allowed).


## Build and run

``` bash
lein ring uberjar
java -jar target/workdays-1.1.0-standalone.jar
```

To use an alternative port for Jetty (3000 is default) just set the
environment variable PORT ie.

``` bash
export PORT=8080
java -jar target/workdays-1.1.0-standalone.jar
```


Resulting in a startup looking like the following:

```
2015-04-23 00:10:11.965:INFO:oejs.Server:jetty-7.x.y-SNAPSHOT
2015-04-23 00:10:12.002:INFO:oejs.AbstractConnector:Started SelectChannelConnector@0.0.0.0:8080
Started server on port 8080
```

## License

Copyright © 2014

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
