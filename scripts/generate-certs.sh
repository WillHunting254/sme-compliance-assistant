#!/usr/bin/env bash
# Generates a local dev CA + certs for Kafka broker/client and the
# Spring Boot REST API. NOT for production use — passwords are
# hardcoded on purpose for local dev convenience.
set -euo pipefail

CERT_DIR="$(cd "$(dirname "$0")/.." && pwd)/docker/certs"
PASSWORD="${PASSWORD:-${CERT_PASSWORD:-changeit-local-dev}}"
DAYS=365

mkdir -p "$CERT_DIR"
cd "$CERT_DIR"

printf '%s\n' "$PASSWORD" > cert_creds

echo "== Generating CA =="
openssl req -new -x509 -keyout ca-key.pem -out ca-cert.pem -days "$DAYS" \
  -subj "/CN=sme-compliance-local-ca/OU=dev/O=local/C=BE" \
  -passout "pass:$PASSWORD"

echo "== Generating Kafka broker keystore =="
keytool -genkey -noprompt -alias kafka-broker \
  -dname "CN=localhost, OU=dev, O=local, C=BE" \
  -keystore kafka.broker.keystore.jks \
  -keyalg RSA -storepass "$PASSWORD" -keypass "$PASSWORD"

keytool -keystore kafka.broker.keystore.jks -alias kafka-broker \
  -certreq -file broker-cert-req.csr -storepass "$PASSWORD"

openssl x509 -req -CA ca-cert.pem -CAkey ca-key.pem -in broker-cert-req.csr \
  -out broker-cert-signed.pem -days "$DAYS" -CAcreateserial \
  -passin "pass:$PASSWORD"

keytool -keystore kafka.broker.keystore.jks -alias CARoot \
  -importcert -file ca-cert.pem -storepass "$PASSWORD" -noprompt
keytool -keystore kafka.broker.keystore.jks -alias kafka-broker \
  -importcert -file broker-cert-signed.pem -storepass "$PASSWORD" -noprompt

echo "== Generating shared truststore (broker + clients) =="
keytool -keystore kafka.truststore.jks -alias CARoot \
  -importcert -file ca-cert.pem -storepass "$PASSWORD" -noprompt

echo "== Generating REST API keystore (for Spring Boot server.ssl) =="
keytool -genkey -noprompt -alias sme-api \
  -dname "CN=localhost, OU=dev, O=local, C=BE" \
  -keystore api.keystore.p12 -storetype PKCS12 \
  -keyalg RSA -storepass "$PASSWORD" -keypass "$PASSWORD" \
  -ext "SAN=DNS:localhost,IP:127.0.0.1"

echo "Done. Certs written to $CERT_DIR"
echo "Shared password for all stores: $PASSWORD (local dev only)"
