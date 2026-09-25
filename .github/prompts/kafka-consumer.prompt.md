---
mode: agent
description: Add a Kafka consumer for an existing topic
---

Add a Kafka consumer for the topic: ${input:topic:e.g. "invoice.submitted"}

Follow the Kafka conventions in copilot-instructions.md:

1. `@KafkaListener` method stays thin: deserialize, delegate to a
   service, ack
2. Configure a dead-letter topic (`<topic>.dlt`) for messages that
   fail deserialization or processing — don't let a bad message
   block the consumer group
3. The actual processing logic goes in a service class, not in the
   listener method
4. Write a unit test for the service logic (mock inputs), plus an
   `@EmbeddedKafka` test that publishes a message and asserts the
   consumer processes it (and a second test that a malformed
   message lands on the DLT)

Ask before adding a new dependency. Don't touch TLS config unless
that's explicitly what I asked for.
