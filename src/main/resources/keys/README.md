# RSA Keys for JWT RS256

This directory holds **development-only** RSA keys.

**Never commit real production keys to version control.**

## Generate a key pair (requires OpenSSL)

```bash
# 1. Generate PKCS#8 private key (2048-bit RSA)
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 \
  -out src/main/resources/keys/private.pem

# 2. Extract the public key
openssl rsa -in src/main/resources/keys/private.pem \
  -pubout -out src/main/resources/keys/public.pem
```

In production, provide the key paths via environment variables:

```
JWT_PRIVATE_KEY_PATH=/run/secrets/jwt_private.pem
JWT_PUBLIC_KEY_PATH=/run/secrets/jwt_public.pem
```
