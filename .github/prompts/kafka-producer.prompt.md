---
mode: agent
description: Add a Kafka producer for a new event type
---

Add a Kafka producer for the event: ${input:event:e.g. "invoice.validated"}

Follow the Kafka conventions in copilot-instructions.md:

1. Define the event payload as a Java record in a `messaging.event`
   package, with a `schemaVersion` field
2. Create (or extend) a `@Service` producer class dedicated to this
   topic family — don't call `KafkaTemplate` from business logic
   directly
3. Wire the topic name as a constant, not a magic string
4. Add the topic to the local Kafka setup (docker-compose topic
   list or an admin bean, whichever the project already uses)
5. Write a unit test that mocks `KafkaTemplate` and asserts the
   right topic/payload is sent
6. If this is the first producer in the project, also add an
   `@EmbeddedKafka` integration test proving the message round-trips

Ask before adding a new dependency. Don't touch TLS config unless
that's explicitly what I asked for.
