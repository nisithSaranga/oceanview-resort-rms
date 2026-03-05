# Reservation smoke tests
base URL: http://localhost:8080/oceanview-resort-rms

**1)Create a reservation (POST)**
  
 Option A (use file):
   ```
   curl.exe -i -X POST "http://localhost:8080/oceanview-resort-rms/reservations" -H "Content-Type: application/json" --data-binary "@$PWD\tools\http\res_create.json"
   ```
   Option B (inline JSON, PowerShell-safe single line):
   ```
   curl.exe -i -X POST "http://localhost:8080/oceanview-resort-rms/reservations" -H "Content-Type: application/json" --data-raw "{\"fullName\":\"Test Guest\",\"address\":\"Test Address\",\"contactNumber\":\"0771234567\",\"roomType\":\"DELUXE\",\"checkIn\":\"2026-03-10\",\"checkOut\":\"2026-03-12\"}"
   ```
   ✅ Expect: 200 with JSON containing "reservationNo":"RES-...", "status":"CREATED", "roomId":..., "message":"Reservation created"

**2) Get reservation (GET)**
```
 curl.exe -i "http://localhost:8080/oceanview-resort-rms/reservations?reservationNo=RES-PASTE-HERE"
   ```
✅ Expect: 200 and reservation details.

**3) Relocate room (PUT update roomType)**
```
   curl.exe -i -X PUT "http://localhost:8080/oceanview-resort-rms/reservations" -H "Content-Type: application/json" --data-raw "{\"reservationNo\":\"RES-PASTE-HERE\",\"roomType\":\"SUITE\"}"
   ```
   ✅ Expect: 200 with "message":"Reservation updated" and the roomId changes (and DB: old room available=1, new room available=0).

**4) Cancel reservation (PUT status=CANCELLED)**
```
   curl.exe -i -X PUT "http://localhost:8080/oceanview-resort-rms/reservations" -H "Content-Type: application/json" --data-raw "{\"reservationNo\":\"RES-PASTE-HERE\",\"status\":\"CANCELLED\"}"
   ```
   ✅ Expect: 200 with "message":"Reservation cancelled" and "status":"CANCELLED".
   DB check: room becomes available again.

**5) Delete reservation (DELETE)**
   ```
   curl.exe -i -X DELETE "http://localhost:8080/oceanview-resort-rms/reservations?reservationNo=RES-PASTE-HERE"
   ```
   ✅ Expect: 200 with "message":"Reservation deleted".
   Negative test (required behavior): Try delete without cancelling first → should return 400 **(rule: delete only if CANCELLED)**.