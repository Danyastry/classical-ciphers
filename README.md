# Encryption

> My previous repository was removed by github, this is a recreation with a few improvements: every cipher now has a UI.

## Ciphers

| Family | Ciphers |
|---|---|
| Substitution | Caesar, ROT13/ROT47, Atbash, Affine, Simple substitution, Bacon |
| Polyalphabetic | Vigenère, Beaufort, Autokey, One-time pad, XOR |
| Transposition | Rail fence, Columnar, Scytale |
| Squares and matrices | Polybius, Playfair, Two-square, Four-square, Bifid, Trifid, ADFGVX, Hill |
| Machines | Enigma I |

Caesar and Simple substitution can be cracked with frequency analysis, Vigenère with the Kasiski method.

## Modules

- `app` – catalog and navigation
- `core` – shared cipher code, UI components, test helpers
- `feature` – one module per cipher
