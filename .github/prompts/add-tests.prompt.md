---
mode: agent
description: Add missing tests for a service or controller
---

Add tests for: ${input:target:file or class name, e.g. VatValidationServiceImpl}

- Use JUnit 5 + Mockito, matching the naming style already used in
  the test suite (`shouldX_whenY`)
- Mock any external client calls (VIES lookups, etc.) — no real
  network calls
- Cover: the happy path, at least one invalid-input case, and any
  branch that throws a custom exception
- If the class has no tests yet, create the test file in the
  matching package under `src/test/java`
- Run the test suite after writing tests and fix any failures
  before finishing
