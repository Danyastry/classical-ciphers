# Encryption

> My previous repository was removed by github, this is a recreation with a few improvements: every cipher now has a UI.

## Ciphers

| Family | Ciphers |
|---|---|
| Substitution | Caesar, ROT13/ROT47, Atbash, Affine, Simple substitution, Bacon |
| Polyalphabetic | Vigenere, Beaufort, Autokey, One-time pad, XOR |
| Transposition | Rail fence, Columnar, Scytale |
| Squares and matrices | Polybius, Playfair, Two-square, Four-square, Bifid, Trifid, ADFGVX, Hill |
| Machines | Enigma I |

Caesar and Simple substitution can be cracked with frequency analysis, Vigenere with the Kasiski method.

## Screenshots

<img src="docs/screenshots/catalog-1.png" width="240">

### Substitution

- **Caesar** – brute force marks the most likely shift

  <img src="docs/screenshots/caesar-1.png" width="240"> <img src="docs/screenshots/caesar-2.png" width="240">

- **ROT13 / ROT47**

  <img src="docs/screenshots/rot-1.png" width="240">

- **Atbash**

  <img src="docs/screenshots/atbash-1.png" width="240">

- **Affine**

  <img src="docs/screenshots/affine-1.png" width="240">

- **Simple substitution** – cracking with frequency analysis

  <img src="docs/screenshots/substitution-1.png" width="240"> <img src="docs/screenshots/substitution-2.png" width="240">

- **Bacon** – hides the message in letter case

  <img src="docs/screenshots/bacon-1.png" width="240"> <img src="docs/screenshots/bacon-2.png" width="240">

### Polyalphabetic

- **Vigenere** – cracked with the Kasiski method

  <img src="docs/screenshots/vigenere-1.png" width="240"> <img src="docs/screenshots/vigenere-2.png" width="240">

- **Beaufort**

  <img src="docs/screenshots/beaufort-1.png" width="240">

- **Autokey**

  <img src="docs/screenshots/autokey-1.png" width="240">

- **One-time pad** – any message is possible without the key

  <img src="docs/screenshots/onetimepad-1.png" width="240"> <img src="docs/screenshots/onetimepad-2.png" width="240">

- **XOR** – known-plaintext attack

  <img src="docs/screenshots/xor-1.png" width="240"> <img src="docs/screenshots/xor-2.png" width="240">

### Transposition

- **Rail fence**

  <img src="docs/screenshots/railfence-1.png" width="240">

- **Columnar**

  <img src="docs/screenshots/columnar-1.png" width="240">

- **Scytale**

  <img src="docs/screenshots/scytale-1.png" width="240">

### Squares and matrices

- **Polybius**

  <img src="docs/screenshots/polybius-1.png" width="240">

- **Playfair**

  <img src="docs/screenshots/playfair-1.png" width="240"> <img src="docs/screenshots/playfair-2.png" width="240">

- **Two-square**

  <img src="docs/screenshots/twosquare-1.png" width="240"> <img src="docs/screenshots/twosquare-2.png" width="240">

- **Four-square**

  <img src="docs/screenshots/foursquare-1.png" width="240"> <img src="docs/screenshots/foursquare-2.png" width="240">

- **Bifid**

  <img src="docs/screenshots/bifid-1.png" width="240"> <img src="docs/screenshots/bifid-2.png" width="240">

- **Trifid**

  <img src="docs/screenshots/trifid-1.png" width="240"> <img src="docs/screenshots/trifid-2.png" width="240">

- **ADFGVX**

  <img src="docs/screenshots/adfgvx-1.png" width="240"> <img src="docs/screenshots/adfgvx-2.png" width="240">

- **Hill**

  <img src="docs/screenshots/hill-1.png" width="240">

### Machines

- **Enigma I** – decrypts a real message from 1941

  <img src="docs/screenshots/enigma-1.png" width="240"> <img src="docs/screenshots/enigma-2.png" width="240">

## Modules

- `app` – catalog and navigation
- `core` – shared cipher code, UI components, test helpers
- `feature` – one module per cipher
