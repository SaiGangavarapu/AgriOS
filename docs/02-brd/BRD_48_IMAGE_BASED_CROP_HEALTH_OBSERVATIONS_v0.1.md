# BRD 48 — Image-Based Crop-Health Observations

## 1. Objective

Use images as supporting evidence for crop-health assessment without treating computer vision as an unquestioned diagnosis.

## 2. Image-capture guidance

The platform should guide users to capture:

- full plant
- affected plant part
- front and back of leaf
- field-wide distribution
- stem, fruit, root, or soil where relevant
- reference object for scale

## 3. Image metadata

- farmer or observer
- field and crop cycle
- crop stage
- date and time
- location
- device
- lighting
- plant part
- symptom description
- image quality
- consent status

## 4. AI-assisted classification

The system may return:

- possible classes
- confidence
- visual regions of interest
- missing evidence
- alternative diagnoses
- recommendation to obtain expert review

## 5. Restrictions

The system shall not independently prescribe a pesticide solely from an image classification.

## 6. Data governance

Images may contain:

- people
- location
- property
- documents
- neighbouring fields

Retention, research use, and sharing require appropriate consent and minimization.

## 7. Business requirements

- BR-208: The platform shall support guided crop-health image capture.
- BR-209: Image metadata and quality shall be recorded.
- BR-210: AI classifications shall expose confidence and alternatives.
- BR-211: Image classification alone shall not authorize high-risk treatment.
- BR-212: Image use shall respect consent and retention policy.
