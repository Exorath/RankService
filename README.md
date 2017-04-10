# RankService
The service that provides tiered ranks and permissions, backed in mongodb.

## Endpoints

### /ranks [GET]:
#### Gets all rankIds

**Response**:
```json
{"ranks": ["vip", "premium", "admin", "mod"]}
```

### /ranks/{rankId} [GET]:
#### Gets information about the rank

**Response**:
```json
{
"name": "PREMIUM",
"inheritance" : "VIP"
}
```

### /ranks/{rankId} [PUT]:
#### Updates information about the rank

**Request**:
```json
{
"name": "PREMIUM",
"inheritance" : "VIP"
}
```

**Response**:
```json
{
"success": true
}
```

### /players/{playerId} [GET]:
#### Gets the player's rank

**Response**:
```json
{
"rank": "PREMIUM"
}
```

### /players/{playerId} [PUT]:
#### Sets the player's rank

**Request**:
```json
{
"rank": "PREMIUM"
}
```

**Response**:
```json
{
"success": true
}
```
### /players/{playerId}/inherits/{rankId} [PUT]:
#### Gets whether or not the players rank inherits from the rankId

**Response**:
```json
{
"inherits": true
}
```

## Environment
| Name | Value |
| --------- | --- |
| MONGO_URI | {mongo_uri} |
| DB_NAME | {db name to store data} |
