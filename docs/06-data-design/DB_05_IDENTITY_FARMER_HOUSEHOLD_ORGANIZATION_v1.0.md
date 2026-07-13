# DB 05 — Identity, Farmer, Household, and Organization

## Identity tables

- `identity.user_account`
- `identity.authentication_factor`
- `identity.user_session`
- `identity.role`
- `identity.permission`
- `identity.user_role`
- `identity.delegated_access_grant`

## Farmer tables

- `farmer.farmer`
- `farmer.farmer_contact`
- `farmer.farmer_verification`
- `farmer.farmer_alias`
- `farmer.farmer_correction_history`

## Household tables

- `household.household`
- `household.household_member`

## Organization tables

- `organization.organization`
- `organization.organization_member`
- `organization.organization_verification`

## Key rules

- Farmer and UserAccount are separate
- alias maps merged profiles to canonical farmer
- household membership does not imply consent authority
- organization membership does not imply data access
