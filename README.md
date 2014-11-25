# Workday calendar

In the danish gas and electricity and gas market many business processes
is required to be instantiated or finised within a time limit calculated
in "workdays".

Non workdays are more that just weekends because danish hollydays needs
to be taken into account as well. Energinet has specified non workdays
in the D1 regulation.

This can be used as a clojure lib:

``` clojure
(nth-workday (t/local-date 2015 4 19) 10)
#<LocalDate 2015-05-05>
```

or as a webservice:

http://localhost:3000/date-subsequent-to-workdays/?ref-date=2015-04-19&workdays=10

2015-05-05


## License

Copyright © 2014

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
