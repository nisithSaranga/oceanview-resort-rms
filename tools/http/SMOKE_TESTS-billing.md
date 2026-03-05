# Billing smoke tests
base URL: http://localhost:8080/oceanview-resort-rms

*0) Pre-req*
✅ must already have a valid reservationNo (from Reservation POST), status ideally CREATED or CONFIRMED.

---

**1) Generate invoice (POST)**
 
Option A (use file):
```
curl.exe -i -X POST "http://localhost:8080/oceanview-resort-rms/billing" -H "Content-Type: application/json" --data-binary "@$PWD\tools\http\billing_generate.json"
```

Option B (inline JSON):
```
curl.exe -i -X POST "http://localhost:8080/oceanview-resort-rms/billing" -H "Content-Type: application/json" --data-raw "{"reservationNo":"RES-PASTE-HERE"}"
```

✅ Expect: 200 with JSON containing "invoiceId":..., "reservationNo":"RES-...", "totalAmount":..., "message":"Invoice generated" (or similar)

---

**2) Get invoice by reservationNo (GET)**
```
curl.exe -i "http://localhost:8080/oceanview-resort-rms/billing?reservationNo=RES-PASTE-HERE"
```
✅ Expect: 200 with invoice details for that reservation (invoiceId/totalAmount/etc.)

---

**3) Duplicate protection (POST same reservationNo again)**
```
curl.exe -i -X POST "http://localhost:8080/oceanview-resort-rms/billing" -H "Content-Type: application/json" --data-raw "{"reservationNo":"RES-PASTE-HERE"}"
```
✅ Expect: Either
- 200 with "message":"Invoice already exists" (or similar), *OR*
- 400/409 depending on your implementation  
  (Important: should NOT create a second invoice for the same reservationNo)

---

**4) Negative test (reservationNo missing / blank)**
```
curl.exe -i -X POST "http://localhost:8080/oceanview-resort-rms/billing" -H "Content-Type: application/json" --data-raw "{}"
```

✅ Expect: 400 with "message":"reservationNo is required" (or similar)