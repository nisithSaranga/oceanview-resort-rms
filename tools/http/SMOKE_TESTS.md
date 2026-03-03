# HTTP smoke tests (OceanView RMS)

Quick manual checks for servlet endpoints during development.

## Login
```bash
GET /login (should return help JSON)
curl.exe -i "http://localhost:8080/oceanview-resort-rms/login"

POST /login with empty body (should return 400)
curl.exe -i -X POST "http://localhost:8080/oceanview-resort-rms/login"

POST /login with bad JSON (should return 400)
 Create tools/http/login_bad_json.json with invalid JSON (example below), then run
curl.exe -i -X POST "http://localhost:8080/oceanview-resort-rms/login" -H "Content-Type: application/json" --data-binary "@tools/http/login_bad_json.json"
  
POST /login with invalid credentials (should return 401)
curl.exe -i -X POST "http://localhost:8080/oceanview-resort-rms/login" -H "Content-Type: application/json" --data-binary "@tools/http/login_invalid_creds.json"

POST /login with valid credentials (should return 200)
 Edit tools/http/login_valid_local.json locally (do NOT commit real creds), then run:
curl.exe -i -X POST "http://localhost:8080/oceanview-resort-rms/login" -H "Content-Type: application/json" --data-binary "@tools/http/login_valid_local.json"
```

## Logout
```bash
POST /logout (invalidates session)
curl.exe -i -X POST "http://localhost:8080/oceanview-resort-rms/logout"

Session test (login -> save cookie -> logout using cookie)
curl.exe -i -c tools/http/cookies.txt -X POST "http://localhost:8080/oceanview-resort-rms/login" -H "Content-Type: application/json" --data-binary "@tools/http/login_valid_local.json"
curl.exe -i -b tools/http/cookies.txt -X POST "http://localhost:8080/oceanview-resort-rms/logout"

