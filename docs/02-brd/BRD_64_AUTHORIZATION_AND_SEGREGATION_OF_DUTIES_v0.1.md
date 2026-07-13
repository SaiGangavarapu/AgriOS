# BRD 64 — Authorization and Segregation of Duties

## 1. Objective

Ensure users can perform only authorized actions and that high-risk processes do not depend on one uncontrolled actor.

## 2. Authorization dimensions

- role
- organization
- tenant
- programme
- geography
- farmer
- farm
- field
- crop cycle
- data category
- action
- time period
- consent
- verification level

## 3. Privileged actions

- profile merge
- consent override where legally permitted
- knowledge publication
- treatment approval
- automation-rule approval
- data export
- policy configuration
- user suspension
- audit access
- financial-evidence release

## 4. Segregation examples

- author and approver of agronomy knowledge should differ
- device installer and commissioning verifier may differ for high-risk devices
- claim preparer and final submitter may differ
- security administrator and audit reviewer should differ where feasible
- commercial seller shall not approve independent agronomic advice

## 5. Emergency access

Emergency access shall be:

- justified
- time-bound
- limited
- visibly marked
- fully audited
- reviewed afterward

## 6. Business requirements

- BR-288: Authorization shall support contextual scope beyond static roles.
- BR-289: Privileged actions shall require explicit permission.
- BR-290: High-risk workflows shall support segregation of duties.
- BR-291: Emergency access shall be time-bound and reviewed.
- BR-292: Commercial roles shall not approve independent agronomy content.
