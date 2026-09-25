---
mode: agent
description: Scaffold a new REST endpoint following project conventions
---

Create a new endpoint for: ${input:feature:describe the feature, e.g. "check invoice numbering format"}

Follow the layered architecture and conventions in
copilot-instructions.md:

1. Request/response DTOs as Java records in the `dto` package
2. Service interface + impl in the `service` package, with
   constructor injection
3. Controller method in the appropriate controller (create one if
   none fits), thin, delegates to the service
4. A specific unchecked exception if the input can be invalid,
   handled in the existing `@ControllerAdvice`
5. Unit test for the service (mock any external client)
6. `@WebMvcTest` for the controller

Ask me before adding any new Maven dependency. Don't guess at
Belgian legal/VAT rules — leave a `// TODO: confirm rule` comment
if you're not certain.
