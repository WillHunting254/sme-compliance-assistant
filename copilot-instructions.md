# Copilot instructions — SME Compliance Assistant

## Project context
Event-driven Spring Boot system for Belgian SME invoice compliance:
an HTTPS REST API accepts invoice submissions, validates them
(VAT number via EU VIES, invoice numbering, MAR account category
suggestion), and publishes the result as Kafka events for
downstream consumers. Java 21, Maven, Spring Web, Spring Kafka,
Spring Validation. A companion MCP server (in /mcp-server) exposes
the compliance checks as callable tools. Kafka and the REST API
both run over TLS.

## Architecture
- Layered: Controller -> Service -> (Client / Repository / Kafka producer)
- Controllers are thin: validate input, delegate to a service, map
  to a response DTO. No business logic or Kafka calls directly in
  controllers.
- Services hold business logic and are interface + impl. External
  I/O (VIES lookups, Kafka producers/consumers) lives in dedicated
  `client` / `messaging` packages, never inline in a service that
  also has business logic.
- DTOs are Java records. Never expose JPA entities or Kafka event
  payloads directly as API responses — map explicitly.

## Kafka conventions
- Topics: `invoice.submitted`, `invoice.validated`, `invoice.rejected`.
  Name new topics `<entity>.<past-tense-event>`.
- Events are JSON, versioned with a top-level `schemaVersion` field.
  Never change an existing field's meaning — add a new field and
  bump the version instead.
- Producers: one `@Service` per topic family (e.g.
  `InvoiceEventProducer`), never call `KafkaTemplate` directly from
  business logic.
- Consumers: `@KafkaListener` methods stay thin — deserialize,
  delegate to a service, ack. Handle deserialization/processing
  errors with a dead-letter topic (`<topic>.dlt`), don't let a bad
  message block the consumer group.
- All producer/consumer configs must load TLS settings (keystore/
  truststore paths, passwords) from environment variables /
  `application.yml`, never hardcoded.

## TLS conventions
- The REST API uses HTTPS only — no plaintext HTTP profile checked
  into the repo. Local dev cert lives in `docker/certs/`, generated
  by `scripts/generate-certs.sh`, never commit real production
  certs or passwords.
- Kafka client TLS config (`ssl.truststore.location`,
  `ssl.keystore.location`, etc.) goes in `application.yml` under a
  `kafka.ssl` block, values sourced from env vars with
  `${VAR:default}` syntax so local dev has sane defaults but prod
  overrides cleanly.

## Testing
- JUnit 5 + Mockito for unit tests. Mock Kafka producers/consumers
  and the VIES client — no real network or broker calls in unit
  tests.
- Use `@EmbeddedKafka` (Spring Kafka test) for integration tests of
  producer/consumer flows; skip TLS in the embedded broker unless
  a test is specifically about TLS config.

## What NOT to do
- Don't add a new dependency without flagging it in the PR
  description.
- Don't invent Belgian VAT/invoicing rules — leave a
  `// TODO: confirm rule` comment if unsure.
- Don't disable TLS verification (`ssl.endpoint.identification.algorithm=`)
  anywhere outside of test config — flag it explicitly if you think
  it's needed.
