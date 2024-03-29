# Service Descrioption

### Usage
Application simply can be run in ide

* It will run on the default port: 8080
* The swagger is reachable on http://localhost:8080/swagger-ui/index.html#/

### Behavior of account creation
When creating account it calls the background-security-check service asynchronously with the fire and forget approach,
using coroutine.

### Database
* The DB is simulated with a ConcurrentHashMap, so it works more like a NoSql DB.
* There is two store or "table" for the accounts and for the transaction
