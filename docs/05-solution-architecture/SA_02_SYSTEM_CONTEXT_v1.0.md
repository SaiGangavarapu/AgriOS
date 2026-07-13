# SA 02 — System Context

## Primary users

- Farmer
- Household Operator
- Farm Worker
- Field Officer
- Agronomist
- Laboratory Operator
- FPO Manager
- Buyer
- Insurer
- Lender
- Platform Administrator
- Support Operator

## External systems

- Identity providers
- IMD and weather providers
- Soil and water laboratories
- Government agriculture systems
- GIS and map providers
- IoT gateways
- Notification providers
- Market-price sources
- Insurer and lender systems
- Object storage
- AI model providers

## High-level context

```text
Users and Devices
       |
 Mobile/PWA/Web/Field Officer Apps
       |
 API Gateway / BFF
       |
 AgriOS Modular Monolith
       |
 -----------------------------------------
 | DB | PostGIS | Time Series | Search |
 | Object Storage | Cache | Event Broker |
 -----------------------------------------
       |
 External Adapters and Partner Systems
```
