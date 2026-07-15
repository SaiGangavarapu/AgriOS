# What Changed — Market Intelligence, Price Discovery, Sales Planning, and Marketplace Integration Core v1.0

Added the Market bounded context.

Market prices are normalized from provider-specific feeds into a stable AgriOS
contract. Sales planning uses farmer, crop-cycle, lot, quantity, price floor,
target price, sales window, and selling strategy.

Buyer offers are validated against plan availability and minimum acceptable
price. Marketplace integration remains adapter-neutral and uses connector and
listing contracts with idempotency.
