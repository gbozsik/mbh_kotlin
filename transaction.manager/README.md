# Service Descrioption

### Usage
Application simply can be run in ide

* It will run on the default port: 8080
* The swagger is reachable on http://localhost:8080/swagger-ui/index.html#/

### Behavior of account creation
When creating account it calls the background-security-check service asynchronously with the fire and forget approach,
using coroutine.

### Database
* The DB is simulated with a ConcurrentHashMap.
  * So it works more like a NoSql DB or a cache, since there is a key value pair, and it can be queried only by the key or let's say id.
* There is two store or "table". One for the accounts and the other for the transactions

### Exception handling
Exceptions are handled in the GlobalExceptionHandler class, via @ControllerAdvice annotation provided by Spring.
In case of errors it responds only error codes because of security reasons.