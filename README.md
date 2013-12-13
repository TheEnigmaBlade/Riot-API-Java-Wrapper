Java Wrapper for the Riot API
=============================

A powerful and easy-to-use wrapper for the [Riot Developer API](https://developer.riotgames.com/).

Requires Java 7 or higher.

Key Features
------------

* **Request rate limiting**: requests are limited based on the requests per 10 seconds value. If you only have 5 requests per 10 seconds, requests will be limited to 1 request every 2 seconds.
* **Request caching**: requests are cached using LRU replacement. This means the most frequent requests will be stored and not requested from the server. Caching can be bypassed.
* **Two distinct styles of API usage**: you can follow the API style and call individual methods and operations, or you can take advantage of object-oriented principles and make requests directly from an object (such as a champion or summoner).

Examples
--------

###Style 1: API Style

This style is most similar to [the layout of the API its documentation](https://developer.riotgames.com/api/methods). Requests are handled through methods and their operations.

```java
RiotApi api = new RiotApi(apiKey, userAgent);

//Make one request to get my summoner ID
Summoner me = api.getSummonerMethod().getSummonerByName(Region.NA, "TheEnigmaBlade");

//Make another request to get my match history
List<Game> myMatchHistory = api.getGameMethd().getMatchHistory(Region.NA, me.getId());
```

###Style 2: Object-oriented Style

This style utilizes object-oriented design to create an easy-to-use API interface. Requests are handled through convenience methods provided in the wrapper's structures.

```java
RiotApi api = new RiotApi(apiKey, userAgent);

//Make one request to get my summoner ID
Summoner me = api.getSummoner(Region.NA, "TheEnigmaBlade");

//Make another request to get my match history
List<Game> myMatchHistory = me.getMatchHistory();
```

API Support
-----------

###API Specifications

| Method        | Operation                 | Supported |
| ------------- | ------------------------- | :-------: |
| champion-v1.1 | /                         | **Yes**   |
| game-v1.1     | /by-summoner/{id}/recent  | **Yes**   |
| league-v2.1   | /by-summoner/{id}         | **Yes**   |
| stats-v1.1    | /by-summoner/{id}/summary | **Yes**   |
|               | /by-summoner/{id}/ranked  | **Yes** |
| summoner-v1.1 | /by-name/{name}           | **Yes**   |
|               | /{id}                     | **Yes**   |
|               | /{ids}/name               | **Yes**   |
|               | /{id}/masteries           | **Yes**   |
|               | /{id}/runes               | **Yes**   |
| team-v2.1     | /by-summoner/{id}         | No (soon) |

###Object-oriented Usage

| Type     | Operation               |
| -------- | ----------------------- |
| Summoner | getMatchHistory()       |
|          | getLeagues()            |
|          | getLeague(QueueType)    |
|          | getMasteryPages()       |
|          | getCurrentMasteryPage() |
|          | getRunePages()          |
|          | getCurrentRunePage()    |

Legal stuff
-----------

Copyright EnigmaBlade.net, 2013

Distributed under the Boost Software License, Version 1.0

See LICENSE.txt or at http://www.boost.org/LICENSE_1_0.txt

*This product is not endorsed, certified or otherwise approved in any way by Riot Games, Inc. or any of its affiliates.*