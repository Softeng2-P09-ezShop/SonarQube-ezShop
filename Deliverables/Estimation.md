# Project Estimation  
Authors: Claudio Tancredi (s292523), Ehsan Ansari Nejad (s288903), Takla Trad (s289222)

Date: 30/04/2021

Version: 1.0
## Contents
- [Estimation approach](#estimation-approach)
- [Estimate by product decomposition](#estimate-by-product-decomposition)
- [Estimate by activity decomposition](#estimate-by-activity-decomposition)
- [Considerations](#considerations)
### Estimation approach
This estimation is not based on past projects and past team performance, as it should be, since we're missing historical data about it.

### Estimate by product decomposition
|             | Estimate                        |             
| ----------- | :-------------------------------: |  
| NC =  Estimated number of classes to be developed   |               18   |             
|  A = Estimated average size per class, in LOC       |             220               | 
| S = Estimated size of project, in LOC (= NC * A) | 3960 |
| E = Estimated effort, in person hours (here use productivity 10 LOC per person hour)  |                   396                   |   
| C = Estimated cost, in euro (here use 1 person hour cost = 30 euro) |11880 | 
| Estimated calendar time, in calendar weeks (Assume team of 4 people, 8 hours per day, 5 days per week) |           Almost 2 and a half weeks         |  

Since the system includes hardware (bar code reader, printer, credit card reader), cost of hardware should be around 200-250 €. 

### Estimate by activity decomposition 
|         Activity name    | Estimated effort (person hours)   |             
| ----------- | :-------------------------------: | 
| Introductory meeting | 8 |
| Acquisition | 24 |
| Supply | 32 |
| Training | 32 |
| Configuration management |32 |
| Requirements | 36 |
| V&V on requirements document | 8 |
| GUI prototype | 20 |
| Architecture and design | 48 |
| V&V on design document | 12 |
| Coding| 68 |
|Unit testing| 20 |
|Integration testing | 76|
| GUI testing | 16 |
|System testing | 92 |
| Documentation | 64 |
| **Total** |588 |

Training, configuration management and documentation are supporting/organisational processes that can be performed in parallel with the other sequential processes.  

```plantuml
title Gantt chart
Project starts the 1st of april 2021
saturday are closed
sunday are closed
[Introductory meeting] as [IM] starts 2021-04-02 and lasts 1 days
[Acquisition] as [A] lasts 1 days
[A] starts 1 day before [IM]'s end
[Supply] as [S] lasts 1 days
[A] ->[S]
-- Requirement engineering --
[Requirements] as [R] lasts 2 days
[V&V on requirements document] as [VVRD] lasts 1 days
[GUI prototype] as [GUIP] lasts 1 days
[S]->[R]
[R] starts 2021-04-7
[VVRD] starts 1 day before [R]'s end
[Requirements document completed] happens at [VVRD]'s end
[GUIP] starts 1 day before [VVRD]'s end 
-- Architecture and design --
[Architecture and design] as [AD] lasts 2 days
[V&V on design document] as [VVDD] lasts 1 days
[GUIP]->[AD]
[AD] starts 2021-04-12
[VVDD] starts 1 day before [AD]'s end 
[Design document completed] happens at [VVDD]'s end
-- Implementation --
[Coding] as [C] lasts 3 days
-- Testing --
[Unit testing] as [UT] lasts 1 days
[Integration testing] as [IT] lasts 3 days
[GUI testing] as [GUIT] lasts 1 days
[System testing] as [ST] lasts 4 days
[C] starts 1 day before [VVDD]'s end
[C]->[UT]
[IT] starts 1 day before [UT]'s end
[IT]->[GUIT]
[ST] starts 1 day before [GUIT]'s end
[Software system completed] happens at [ST]'s end
-- Supporting/Organisational processes --
[Training] as [T] starts 2021-04-5 and lasts 9 days
[Documentation] as [D] starts 2021-04-16 and lasts 9 days
[Configuration Management] as [CM] starts 2021-04-7 and lasts 14 days
```

There is a space between Supply and Requirements, it's used to model the time necessary for the Training process.
There is also a space between GUI prototype and Architecture and design, it's used to model the time necessary for the Configuration Management process.

### Considerations
The two estimates don't match, it's probably due to the lack of past information. The estimate by activity decomposition may be slightly more accurate and realistic, with an estimated calendar time of 3 and a half weeks, wrt to the 2 and a half weeks estimated by product decomposition (that seem too short).