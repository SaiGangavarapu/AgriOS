# SRS 06 — Farmer Onboarding

## FR-FARMER-001 Self-registration

The system shall allow a farmer to initiate registration using a supported mobile channel.

Preconditions:

- registration enabled for tenant and programme
- supported geography

System behavior:

- capture mandatory profile data
- capture preferred language
- verify contact when required
- create Farmer in Registered state
- create or link UserAccount where applicable
- record source channel
- record audit event

## FR-FARMER-002 Assisted registration

The system shall allow an authorized field officer or organization representative to create a farmer profile on behalf of the farmer.

Additional rules:

- assisted actor identity shall be recorded
- farmer remains the data subject
- consent explanation method shall be recorded
- no permanent delegated access is implied

## FR-FARMER-003 Draft registration

The system shall support draft registration when connectivity or required evidence is incomplete.

## FR-FARMER-004 Duplicate screening

The system shall screen registration against configurable duplicate indicators.

## FR-FARMER-005 Registration status

The system shall expose registration status and next required action.

## VR-FARMER-001 Mandatory fields

Mandatory fields shall include name, preferred language, location, and at least one permitted contact or assisted-registration reference.

## VR-FARMER-002 Mobile format

Mobile format shall be validated using configured country rules.

## SR-FARMER-001 Actor scope

Assisted registration shall require explicit onboarding permission.

## DR-FARMER-001 Registration provenance

The system shall retain source channel, actor, timestamp, tenant, programme, and imported identifiers.
