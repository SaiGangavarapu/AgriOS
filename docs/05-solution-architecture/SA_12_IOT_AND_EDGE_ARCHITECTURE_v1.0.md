# SA 12 — IoT and Edge Architecture

## Device layer

- sensors
- actuators
- gateways
- weather stations
- flow meters
- pump controllers

## Connectivity

- LoRaWAN
- NB-IoT
- LTE-M
- 4G
- Wi-Fi
- Bluetooth provisioning

## Edge gateway

- authenticate device
- buffer telemetry
- normalize timestamps
- validate ranges
- run approved local rules
- control actuators
- enforce safety interlocks
- synchronize configuration

## Cloud flow

```text
Device → Gateway → Ingestion → Validation → Time-Series Store
       → Rule Engine → Advisory / Alert / Command
```

## Safety

- manual override
- maximum runtime
- dry-run protection
- flow confirmation
- rain lockout
- tank-level check
- leak detection
- command expiry
- audit
